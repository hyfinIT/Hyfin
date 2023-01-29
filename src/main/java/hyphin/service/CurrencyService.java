package hyphin.service;

import hyphin.exception.CurrencyRatesPageRequestException;
import hyphin.exception.CurrencyRatesParsingException;
import hyphin.model.currency.CurrencyExchangeRate;
import hyphin.model.currency.OperationAudit;
import hyphin.repository.currency.CurrencyExchangeRateRepository;
import hyphin.repository.currency.OperationAuditRepository;
import hyphin.service.operation.EcStaticDataDailyService;
import hyphin.util.HyfinUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static hyphin.util.HyfinUtils.CCY_PAIRS_DICTIONARY;

@Service
@RequiredArgsConstructor
@Slf4j
public class CurrencyService {

    @Value("${schedule.currency.retry.count}")
    private int retryCount;

    @Value("${scheduling.disable:false}")
    private Boolean disabled;

    private final RestTemplate restTemplate = new RestTemplate();

    private final CurrencyBlendService currencyBlendService;
    private final StandardDeviationService standardDeviationService;
    private final EcStaticDataDailyService ecStaticDataDailyService;

    private final CurrencyExchangeRateRepository currencyExchangeRateRepository;
    private final OperationAuditRepository operationAuditRepository;
    private static final String YAHOO_EUR_USD_URL = "https://uk.finance.yahoo.com/quote/EURUSD%3DX/history?p=EURUSD%3DX";
    private static final String YAHOO_GBP_USD_URL = "https://uk.finance.yahoo.com/quote/GBPUSD%3DX/history?p=GBPUSD%3DX";
    private static final String YAHOO_USD_JPY_URL = "https://uk.finance.yahoo.com/quote/JPY%3DX/history?p=JPY%3DX";


    private static final String MW_EUR_USD_URL = "https://www.marketwatch.com/investing/currency/eurusd/download-data?mod=mw_quote_tab";
    private static final String MW_GBP_USD_URL = "https://www.marketwatch.com/investing/currency/gbpusd/download-data?mod=mw_quote_tab";
    private static final String MW_USD_JPY_URL = "https://www.marketwatch.com/investing/currency/usdjpy/download-data?mod=mw_quote_tab";

    public String todayOperationStatus() {
        return operationAuditRepository.findOperationByDateAndName(LocalDate.now().toString(), "Currency exchange rates fetching");
    }

    @Scheduled(cron = "0 0 */3 * * ?")
    public void scheduledMethod() {
        if (disabled) {
            return;
        }

        if ("SUCCESS".equalsIgnoreCase(todayOperationStatus())) {
            return;
        }

        log.info("Fetching currency rates.................");
        OperationAudit operationAudit = new OperationAudit();
        operationAudit.setName("Currency exchange rates fetching");
        operationAudit.setDateTime(LocalDateTime.now().plusSeconds(2).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        List<CurrencyExchangeRate> yahooYesterdayEntry = currencyExchangeRateRepository.getEntriesByDateAndSource(LocalDate.now().minusDays(1L).toString(), "yahoo");
        List<CurrencyExchangeRate> mwYesterdayEntry = currencyExchangeRateRepository.getEntriesByDateAndSource(LocalDate.now().minusDays(1L).toString(), "marketwatch");

        try {
            ResponseEntity<String> yahooEurUsdResult = getResponse(YAHOO_EUR_USD_URL);
            ResponseEntity<String> yahooGbpUsdResult = getResponse(YAHOO_GBP_USD_URL);
            ResponseEntity<String> yahooUsdIpyResult = getResponse(YAHOO_USD_JPY_URL);

            ResponseEntity<String> mwEurUsdResult = getResponse(MW_EUR_USD_URL);
            ResponseEntity<String> mwGbpUsdResult = getResponse(MW_GBP_USD_URL);
            ResponseEntity<String> mwUsdJpyResult = getResponse(MW_USD_JPY_URL);

            List<CurrencyExchangeRate> currencyExchangeRates = new ArrayList<>();

            if (yahooYesterdayEntry.isEmpty()) {
                currencyExchangeRates.addAll(processYahooResults(yahooEurUsdResult.getBody(), yahooGbpUsdResult.getBody(), yahooUsdIpyResult.getBody()));
            }

            if (mwYesterdayEntry.isEmpty()) {
                currencyExchangeRates.addAll(processMwResults(mwEurUsdResult.getBody(), mwGbpUsdResult.getBody(), mwUsdJpyResult.getBody()));
            }

            Optional<Long> maxIdOptional = currencyExchangeRateRepository.maxId();
            long maxId = maxIdOptional.orElse(0L);
            long nextId = maxId;

            for (int i = 0; i < currencyExchangeRates.size(); i++) {
                nextId = nextId + 1L;
                currencyExchangeRates.get(i).setId(nextId);
            }

            if (currencyExchangeRates.isEmpty()) {
                operationAudit.setStatus("NO YESTERDAY_DATA");
            } else if (currencyExchangeRates.size() < 6) {
                operationAudit.setStatus("DATA IS INCOMPLETE");
            } else {
                currencyExchangeRateRepository.save(currencyExchangeRates);
                operationAudit.setStatus("SUCCESS");
            }
            operationAuditRepository.save(operationAudit);

        } catch (CurrencyRatesPageRequestException e) {
            log.error("Operation error: " + e);
            e.printStackTrace();
            operationAudit.setStatus("FAIL.REQUEST ERROR");
            operationAuditRepository.save(operationAudit);
        } catch (CurrencyRatesParsingException e) {
            log.error("Operation error: " + e);
            e.printStackTrace();
            operationAudit.setStatus("FAIL. NO DATA");
            operationAuditRepository.save(operationAudit);
        } catch (Exception e) {
            log.error("Operation error: " + e);
            e.printStackTrace();
            operationAudit.setStatus("FAIL. UNDEFINED ERROR");
            operationAuditRepository.save(operationAudit);
        }

        if ("SUCCESS".equalsIgnoreCase(operationAudit.getStatus())) {
            currencyBlendService.produceBlends();
            standardDeviationService.createStandardDeviation();
            ecStaticDataDailyService.doOperation();
        }
    }

    private ResponseEntity<String> getResponse(String url) throws CurrencyRatesPageRequestException {
        int counter = 0;
        ResponseEntity<String> result = restTemplate.getForEntity(url, String.class);
        while (result.getStatusCode() != HttpStatus.OK) {
            counter++;
            if (counter > retryCount) {
                log.error("No data after " + retryCount + "attempts. Url: " + url);
                throw new CurrencyRatesPageRequestException("No data after " + retryCount + "attempts. Url: " + url);
            }
            try {
                Thread.sleep(1000 * 60 * 15);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            result = restTemplate.getForEntity(url, String.class);
        }

        return result;
    }

    private List<CurrencyExchangeRate> processMwResults(String... rates) throws CurrencyRatesParsingException {
        List<CurrencyExchangeRate> result = new ArrayList<>();

        try {
            for (int i = 0; i < rates.length; i++) {
                int index = rates[i].indexOf("<th class=\"overflow__heading\"><div class=\"cell__content\">Close</div></th>");
                String substring = rates[i].substring(index, index + 2200);
                String[] split = substring.split("\"cell__content\"");

                int arrayStartIndex = 0;

                for (int j = 0; j < split.length; j++) {
                    if (split[j].contains(LocalDate.now().minusDays(1L).format(DateTimeFormatter.ofPattern("MM/dd/yyyy")))) {
                        arrayStartIndex = j + 1;
                    }
                }

                if (arrayStartIndex == 0) {
                    continue;
                }

                for (int j = 0; j < split.length; j++) {

                    if (split[j].contains("#xA5;")) {
                        split[j] = split[j].substring(split[j].indexOf("#xA5;") + 5, split[j].indexOf(".") + 3);
                        continue;
                    }

                    split[j] = split[j].substring(2, split[j].indexOf(".") + 5);
                }

                CurrencyExchangeRate currencyExchangeRate = new CurrencyExchangeRate();
                currencyExchangeRate.setDate(LocalDate.now().minusDays(1L).toString());
                currencyExchangeRate.setSourceRef("marketwatch");
                currencyExchangeRate.setOpen(Double.valueOf(split[arrayStartIndex++]));
                currencyExchangeRate.setHigh(Double.valueOf(split[arrayStartIndex++]));
                currencyExchangeRate.setLow(Double.valueOf(split[arrayStartIndex++]));
                currencyExchangeRate.setClose(Double.valueOf(split[arrayStartIndex]));
                currencyExchangeRate.setCcyPair(CCY_PAIRS_DICTIONARY.get(i));

                result.add(currencyExchangeRate);
            }
        } catch (Exception e) {
            throw new CurrencyRatesParsingException();
        }

        return result;
    }

    private List<CurrencyExchangeRate> processYahooResults(String... rates) {
        List<CurrencyExchangeRate> result = new ArrayList<>();

        Optional<Long> maxIdOptional = currencyExchangeRateRepository.maxId();
        long maxId = maxIdOptional.orElse(0L);
        long nextId = maxId;

        for (int i = 0; i < rates.length; i++) {
            int index = rates[i].indexOf("BdT Bdc($seperatorColor) Ta(end) Fz(s)");

            nextId = nextId + 1L;

            String substring = rates[i].substring(index, index + 1000);
            String[] split = substring.split("<span>");

            for (int j = 0; j < split.length; j++) {
                split[j] = split[j].substring(0, split[j].indexOf(".") + 5);
            }

            int arrayStartIndex = 0;

            int yesterdayDate = 0;

            if ((LocalDate.now().getDayOfMonth() - 1) == 0) {
                yesterdayDate = LocalDate.now().minusDays(1L).getDayOfMonth();
            } else {
                yesterdayDate = LocalDate.now().getDayOfMonth() - 1;
            }

            if (Integer.valueOf(split[1].substring(0, 2)).equals(yesterdayDate)) {
                arrayStartIndex = 2;
            } else if (Integer.valueOf(split[7].substring(0, 2)).equals(yesterdayDate)) {
                arrayStartIndex = 8;
            } else {
                continue; // HERE WE SHOULD THROW EXCEPTION I gUESS. oR ADD CHECK IF LIST.SIZE = 6
            }

            CurrencyExchangeRate currencyExchangeRate = new CurrencyExchangeRate();
            currencyExchangeRate.setDate(LocalDate.now().minusDays(1L).toString());
            currencyExchangeRate.setSourceRef("yahoo");
            currencyExchangeRate.setOpen(Double.valueOf(split[arrayStartIndex++]));
            currencyExchangeRate.setHigh(Double.valueOf(split[arrayStartIndex++]));
            currencyExchangeRate.setLow(Double.valueOf(split[arrayStartIndex++]));
            currencyExchangeRate.setClose(Double.valueOf(split[arrayStartIndex]));
            currencyExchangeRate.setCcyPair(CCY_PAIRS_DICTIONARY.get(i));
            currencyExchangeRate.setId(nextId);

            result.add(currencyExchangeRate);
        }
        return result;
    }
}
