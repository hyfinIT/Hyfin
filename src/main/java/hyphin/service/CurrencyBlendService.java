package hyphin.service;

import hyphin.model.currency.CurrencyRatesBlend;
import hyphin.model.currency.CurrencyExchangeRate;
import hyphin.model.currency.OperationAudit;
import hyphin.repository.currency.CurrencyRatesBlendRepository;
import hyphin.repository.currency.CurrencyExchangeRateRepository;
import hyphin.repository.currency.OperationAuditRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class CurrencyBlendService {

    private final CurrencyExchangeRateRepository currencyExchangeRateRepository;
    private final OperationAuditRepository operationAuditRepository;
    private final CurrencyRatesBlendRepository currencyRatesBlendRepository;

    private Set<String> activeCcyPairNames = Stream.of("EUR/USD", "GBP/USD", "USD/JPY")
            .collect(Collectors.toCollection(HashSet::new));

    public void produceBlends() {
        log.info("Blending..................");
        OperationAudit operationAudit = new OperationAudit();
        operationAudit.setName("Currency blends creation");
        operationAudit.setDateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        List<CurrencyExchangeRate> yesterdayEntries = currencyExchangeRateRepository.getEntriesByDate(LocalDate.now().minusDays(1L).toString());

        if (yesterdayEntries.size() < 6) {
            operationAudit.setStatus("FAIL. (NO DATA FOR BLENDING)");
            operationAuditRepository.save(operationAudit);
            return;
        }

//        Map<String, List<CurrencyRatesBlend>> collect = currencyRatesBlendRepository.findAll()
//                .stream()
//                .collect(Collectors.groupingBy(CurrencyRatesBlend::getCcyPair));

        List<CurrencyRatesBlend> allBlends = currencyRatesBlendRepository.findAll();

        try {
            if (!currencyRatesBlendRepository.findByDate(LocalDate.now().toString()).isPresent()) {
                pairProcess(yesterdayEntries, allBlends);
            }
            operationAudit.setStatus("SUCCESS");
            operationAuditRepository.save(operationAudit);
        } catch (Exception e) {
            e.printStackTrace();
            operationAudit.setStatus("FAIL");
            operationAuditRepository.save(operationAudit);
        }
    }

    private void pairProcess(List<CurrencyExchangeRate> yesterdayEntries, List<CurrencyRatesBlend> allBlends){
        for (String ccyPair : activeCcyPairNames) {
            List<CurrencyExchangeRate> todayExchangeListByPair = yesterdayEntries
                    .stream()
                    .filter(currencyExchangeRate -> currencyExchangeRate.getCcyPair().equals(ccyPair))
                    .collect(Collectors.toList());


            List<CurrencyRatesBlend> allBlendsByPair = allBlends.stream()
                    .filter(currencyRatesBlend -> currencyRatesBlend.getCcyPair().equals(ccyPair))
                    .collect(Collectors.toList());

            CurrencyRatesBlend yesterdayBlend = null;

            for (CurrencyRatesBlend currencyRatesBlend : allBlendsByPair) {
                if (currencyRatesBlend.getPosition().equals("030")) {
                    currencyRatesBlendRepository.delete(currencyRatesBlend.getId());
                }
                if (currencyRatesBlend.getPosition().equals("001")) {
                    yesterdayBlend = currencyRatesBlend;
                }
            }

            List<CurrencyRatesBlend> shiftedBlends = shiftPositions(allBlendsByPair);

            CurrencyRatesBlend blendEurUsd = new CurrencyRatesBlend();
            blendEurUsd.setCcyPair(ccyPair);
            blendEurUsd.setDate(LocalDate.now().toString());
            blendEurUsd.setPosition("001");
            blendEurUsd.setBlendOpen((todayExchangeListByPair.get(0).getOpen() + todayExchangeListByPair.get(1).getOpen()) / 2);
            blendEurUsd.setBlendHigh((todayExchangeListByPair.get(0).getHigh() + todayExchangeListByPair.get(1).getHigh()) / 2);
            blendEurUsd.setBlendLow((todayExchangeListByPair.get(0).getLow() + todayExchangeListByPair.get(1).getLow()) / 2);
            blendEurUsd.setBlendClose((todayExchangeListByPair.get(0).getClose() + todayExchangeListByPair.get(1).getClose()) / 2);
            blendEurUsd.setDailyRange(blendEurUsd.getBlendHigh() - blendEurUsd.getBlendLow());
            blendEurUsd.setSourceRef("blend");

            if (Objects.nonNull(yesterdayBlend)) {
                blendEurUsd.setLogChance(Math.log(blendEurUsd.getBlendClose() / yesterdayBlend.getBlendClose()));
            }

            shiftedBlends.add(blendEurUsd);

            currencyRatesBlendRepository.save(shiftedBlends);
        }
    }

    private List<CurrencyRatesBlend> shiftPositions(List<CurrencyRatesBlend> blendsList) {
        blendsList.sort(Comparator.comparing(CurrencyRatesBlend::getPosition));

        for (int i = 0; i < blendsList.size(); i++) {
            String newPosition = String.valueOf(Integer.parseInt(blendsList.get(i).getPosition()) + 1);

            if (newPosition.length() == 1) {
                newPosition = "00" + newPosition;
            } else if (newPosition.length() == 2) {
                newPosition = "0" + newPosition;
            }

            blendsList.get(i).setPosition(newPosition);
        }

        blendsList.removeIf(element -> element.getPosition().equals("031"));

        return blendsList;
    }
}
