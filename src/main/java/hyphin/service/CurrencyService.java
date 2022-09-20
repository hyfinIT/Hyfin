package hyphin.service;

import hyphin.model.currency.CurrencyExchangeRate;
import hyphin.model.currency.OperationAudit;
import hyphin.repository.CurrencyExchangeRateRepository;
import hyphin.repository.OperationAuditRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CurrencyService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final CurrencyExchangeRateRepository currencyExchangeRateRepository;
    private final OperationAuditRepository operationAuditRepository;
    private static final String EUR_USD_URL = "https://uk.finance.yahoo.com/quote/EURUSD%3DX/history?p=EURUSD%3DX";
    private static final String GBP_USD_URL = "https://uk.finance.yahoo.com/quote/GBPUSD%3DX/history?p=GBPUSD%3DX";
    private static final String USD_JPY_URL = "https://uk.finance.yahoo.com/quote/JPY%3DX/history?p=JPY%3DX";

    private static final Map<Integer, String> CCY_PAIRS_DICTIONARY = new HashMap<>();

    static {
        CCY_PAIRS_DICTIONARY.put(0, "EUR/USD");
        CCY_PAIRS_DICTIONARY.put(1, "GBP/USD");
        CCY_PAIRS_DICTIONARY.put(2, "USD/JPY");
    }

    @Scheduled(cron = "0 0 1 * * *")
    public void scheduledMethod() {
        OperationAudit operationAudit = new OperationAudit();
        operationAudit.setId(operationAuditRepository.maxId().orElse(0L) + 1L);
        operationAudit.setName("Currency exchange rates fetching");
        operationAudit.setDateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        List<CurrencyExchangeRate> yesterdayEntry = currencyExchangeRateRepository.getYesterdayEntry(LocalDate.now().minusDays(1L).toString());
        if (!yesterdayEntry.isEmpty()) {
            operationAudit.setStatus("ALREADY UP TO DATE");
            operationAuditRepository.save(operationAudit);
            return;
        }

        try {
            ResponseEntity<String> eurUsdResult = restTemplate.getForEntity(EUR_USD_URL, String.class);
            ResponseEntity<String> gbpUsdResult = restTemplate.getForEntity(GBP_USD_URL, String.class);
            ResponseEntity<String> usdIpyResult = restTemplate.getForEntity(USD_JPY_URL, String.class);

            List<CurrencyExchangeRate> currencyExchangeRates = processResults(eurUsdResult.getBody(), gbpUsdResult.getBody(), usdIpyResult.getBody());

            if (currencyExchangeRates.isEmpty()) {
                operationAudit.setStatus("NO YESTERDAY_DATA");
            } else {
                currencyExchangeRateRepository.save(currencyExchangeRates);
                operationAudit.setStatus("SUCCESS");
            }
            operationAuditRepository.save(operationAudit);
        } catch (Exception e) {
            log.error("Operation error: " + e);
            operationAudit.setStatus("FAIL");
            operationAuditRepository.save(operationAudit);
        }
    }

    private List<CurrencyExchangeRate> processResults(String... rates) {
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

            if (Integer.valueOf(split[1].substring(0, 2)).equals(LocalDate.now().getDayOfMonth() - 1)) {
                arrayStartIndex = 2;
            } else if (Integer.valueOf(split[7].substring(0, 2)).equals(LocalDate.now().getDayOfMonth() - 1)) {
                arrayStartIndex = 8;
            } else {
                continue;
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
