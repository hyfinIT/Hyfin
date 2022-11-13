package hyphin.service;

import hyphin.model.currency.Blend;
import hyphin.model.currency.CurrencyRatesBlend;
import hyphin.model.currency.CurrencyRatesBlend;
import hyphin.model.currency.CurrencyRatesBlend;
import hyphin.model.currency.OperationAudit;
import hyphin.model.currency.StandardDeviation;
import hyphin.repository.currency.CurrencyRatesBlendRepository;
import hyphin.repository.currency.CurrencyRatesBlendRepository;
import hyphin.repository.currency.CurrencyRatesBlendRepository;
import hyphin.repository.currency.OperationAuditRepository;
import hyphin.repository.currency.StandardDeviationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class StandardDeviationService {

    private final StandardDeviationRepository standardDeviationRepository;
    private final CurrencyRatesBlendRepository eurUsdRepository;
    private final CurrencyRatesBlendRepository gbpUsdRepository;
    private final CurrencyRatesBlendRepository usdJpyRepository;

    private final OperationAuditRepository operationAuditRepository;

    private static final List<String> rateTypes;

    static {
        rateTypes = new ArrayList<>();
        rateTypes.add("BLENDOPEN");
        rateTypes.add("BLENDHIGH");
        rateTypes.add("BLENDLOW");
        rateTypes.add("BLENDCLOSE");
        rateTypes.add("DAILYRANGE");
        rateTypes.add("LOGCHANGE");
    }

    public void createStandardDeviation() {
        log.info("Standard deviation calculation..................");
        OperationAudit operationAudit = new OperationAudit();
        operationAudit.setId(operationAuditRepository.maxId().orElse(0L) + 1L);
        operationAudit.setName("Standard deviation calculation");
        operationAudit.setDateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));


       try {
           standardDeviationRepository.findAll();

           List<CurrencyRatesBlend> eurUsdAll = eurUsdRepository.findAll();
           List<CurrencyRatesBlend> gbpUsdAll = gbpUsdRepository.findAll();
           List<CurrencyRatesBlend> usdJpyAll = usdJpyRepository.findAll();

           processBlends("EUR/USD",  eurUsdAll);
           processBlends("GBP/USD", gbpUsdAll);
           processBlends("USD/JPY", usdJpyAll);

           operationAudit.setStatus("SUCCESS");
           operationAuditRepository.save(operationAudit);
       } catch (Exception e) {
           operationAudit.setStatus("FAIL");
           operationAuditRepository.save(operationAudit);
       }
    }

    private void processBlends(String pairName, List<CurrencyRatesBlend> eurUsdAll) {
        eurUsdAll.sort(Comparator.comparing(CurrencyRatesBlend::getPosition));

        CurrencyRatesBlend firstPositionBlend = eurUsdAll
                .stream()
                .filter(blendEurUsd -> blendEurUsd.getPosition().equals("001"))
                .findAny()
                .orElseThrow(() -> new RuntimeException("No blend with 001 position found"));

        for (int i = 0; i < rateTypes.size(); i++) {
            StandardDeviation standardDeviation = new StandardDeviation();
            standardDeviation.setCcyPair(pairName);
            standardDeviation.setDate(firstPositionBlend.getDate());
            standardDeviation.setRateType(rateTypes.get(i));

            setPosition(eurUsdAll, i, standardDeviation);

            standardDeviation.setOneMthStdDev(calculateStDev(standardDeviation));
            standardDeviation.setSpotRate(standardDeviation.getPos001());
            standardDeviation.setFiveDayHi(calculateFiveDayHigh(standardDeviation));
            standardDeviation.setFiveDayLo(calculateFiveDayLow(standardDeviation));
            standardDeviation.setOneMthHi(calculateOneMonthHigh(standardDeviation));
            standardDeviation.setOneMthLo(calculateOneMonthLow(standardDeviation));
            standardDeviation.setOneMthRvol(calculateOneMonthRvol(standardDeviation));
            standardDeviation.setId(standardDeviationRepository.maxId().orElse(0L) + 1);

            standardDeviationRepository.save(standardDeviation);
        }
    }

    private static void setPosition(List<CurrencyRatesBlend> eurUsdAll, int i, StandardDeviation standardDeviation) {
        switch (rateTypes.get(i)) {
            case "BLENDOPEN": {
                standardDeviation.setPos001(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("001"))
                        .map(CurrencyRatesBlend::getBlendOpen)
                        .filter(Objects::nonNull)
                        .findAny().orElse(null));

                standardDeviation.setPos002(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("002"))
                        .map(CurrencyRatesBlend::getBlendOpen)
                        .filter(Objects::nonNull)
                        .findAny().orElse(null));

                standardDeviation.setPos003(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("003"))
                        .map(CurrencyRatesBlend::getBlendOpen)
                        .filter(Objects::nonNull)
                        .findAny().orElse(null));

                standardDeviation.setPos004(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("004"))
                        .map(CurrencyRatesBlend::getBlendOpen)
                        .filter(Objects::nonNull)
                        .findAny().orElse(null));

                standardDeviation.setPos005(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("005"))
                        .map(CurrencyRatesBlend::getBlendOpen)
                        .filter(Objects::nonNull)
                        .findAny().orElse(null));

                standardDeviation.setPos006(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("006"))
                        .map(CurrencyRatesBlend::getBlendOpen)
                        .filter(Objects::nonNull)
                        .findAny().orElse(null));

                standardDeviation.setPos007(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("007"))
                        .map(CurrencyRatesBlend::getBlendOpen)
                        .filter(Objects::nonNull)
                        .findAny().orElse(null));

                standardDeviation.setPos008(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("008"))
                        .map(CurrencyRatesBlend::getBlendOpen)
                        .filter(Objects::nonNull)
                        .findAny().orElse(null));

                standardDeviation.setPos009(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("009"))
                        .map(CurrencyRatesBlend::getBlendOpen)
                        .filter(Objects::nonNull)
                        .findAny().orElse(null));

                standardDeviation.setPos010(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("010"))
                        .map(CurrencyRatesBlend::getBlendOpen)
                        .filter(Objects::nonNull)
                        .findAny().orElse(null));

                standardDeviation.setPos011(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("011"))
                        .map(CurrencyRatesBlend::getBlendOpen)
                        .filter(Objects::nonNull)
                        .findAny().orElse(null));

                standardDeviation.setPos012(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("012"))
                        .map(CurrencyRatesBlend::getBlendOpen)
                        .filter(Objects::nonNull)
                        .findAny().orElse(null));

                standardDeviation.setPos013(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("013"))
                        .map(CurrencyRatesBlend::getBlendOpen)
                        .filter(Objects::nonNull)
                        .findAny().orElse(null));

                standardDeviation.setPos014(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("014"))
                        .map(CurrencyRatesBlend::getBlendOpen)
                        .filter(Objects::nonNull)
                        .findAny().orElse(null));

                standardDeviation.setPos015(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("015"))
                        .map(CurrencyRatesBlend::getBlendOpen)
                        .filter(Objects::nonNull)
                        .findAny().orElse(null));

                standardDeviation.setPos016(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("016"))
                        .map(CurrencyRatesBlend::getBlendOpen)
                        .filter(Objects::nonNull)
                        .findAny().orElse(null));

                standardDeviation.setPos017(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("017"))
                        .map(CurrencyRatesBlend::getBlendOpen)
                        .filter(Objects::nonNull)
                        .findAny().orElse(null));

                standardDeviation.setPos018(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("018"))
                        .map(CurrencyRatesBlend::getBlendOpen)
                        .filter(Objects::nonNull)
                        .findAny().orElse(null));

                standardDeviation.setPos019(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("019"))
                        .map(CurrencyRatesBlend::getBlendOpen)
                        .filter(Objects::nonNull)
                        .findAny().orElse(null));

                standardDeviation.setPos020(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("020"))
                        .map(CurrencyRatesBlend::getBlendOpen)
                        .filter(Objects::nonNull)
                        .findAny().orElse(null));

                standardDeviation.setPos021(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("021"))
                        .map(CurrencyRatesBlend::getBlendOpen)
                        .filter(Objects::nonNull)
                        .findAny().orElse(null));

                standardDeviation.setPos022(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("022"))
                        .map(CurrencyRatesBlend::getBlendOpen)
                        .filter(Objects::nonNull)
                        .findAny().orElse(null));

                standardDeviation.setPos023(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("023"))
                        .map(CurrencyRatesBlend::getBlendOpen)
                        .filter(Objects::nonNull)
                        .findAny().orElse(null));
            }

            break;

            case "BLENDHIGH": {
                standardDeviation.setPos001(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("001"))
                        .map(CurrencyRatesBlend::getBlendHigh)
                        .filter(Objects::nonNull)
                        .findAny().orElse(null));

                standardDeviation.setPos002(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("002"))
                        .map(CurrencyRatesBlend::getBlendHigh)
                        .filter(Objects::nonNull)
                        .findAny().orElse(null));

                standardDeviation.setPos003(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("003"))
                        .map(CurrencyRatesBlend::getBlendHigh)
                        .filter(Objects::nonNull)
                        .findAny().orElse(null));

                standardDeviation.setPos004(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("004"))
                        .map(CurrencyRatesBlend::getBlendHigh)
                        .filter(Objects::nonNull)
                        .findAny().orElse(null));

                standardDeviation.setPos005(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("005"))
                        .map(CurrencyRatesBlend::getBlendHigh)
                        .filter(Objects::nonNull)
                        .findAny().orElse(null));

                standardDeviation.setPos006(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("006"))
                        .map(CurrencyRatesBlend::getBlendHigh)
                        .filter(Objects::nonNull)
                        .findAny().orElse(null));

                standardDeviation.setPos007(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("007"))
                        .map(CurrencyRatesBlend::getBlendHigh)
                        .filter(Objects::nonNull)
                        .findAny().orElse(null));

                standardDeviation.setPos008(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("008"))
                        .map(CurrencyRatesBlend::getBlendHigh)
                        .filter(Objects::nonNull)
                        .findAny().orElse(null));

                standardDeviation.setPos009(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("009"))
                        .map(CurrencyRatesBlend::getBlendHigh)
                        .filter(Objects::nonNull)
                        .findAny().orElse(null));

                standardDeviation.setPos010(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("010"))
                        .map(CurrencyRatesBlend::getBlendHigh)
                        .filter(Objects::nonNull)
                        .findAny().orElse(null));

                standardDeviation.setPos011(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("011"))
                        .map(CurrencyRatesBlend::getBlendHigh)
                        .filter(Objects::nonNull)
                        .findAny().orElse(null));

                standardDeviation.setPos012(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("012"))
                        .map(CurrencyRatesBlend::getBlendHigh)
                        .filter(Objects::nonNull)
                        .findAny().orElse(null));

                standardDeviation.setPos013(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("013"))
                        .map(CurrencyRatesBlend::getBlendHigh)
                        .filter(Objects::nonNull)
                        .findAny().orElse(null));

                standardDeviation.setPos014(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("014"))
                        .map(CurrencyRatesBlend::getBlendHigh)
                        .filter(Objects::nonNull)
                        .findAny().orElse(null));

                standardDeviation.setPos015(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("015"))
                        .map(CurrencyRatesBlend::getBlendHigh)
                        .filter(Objects::nonNull)
                        .findAny().orElse(null));

                standardDeviation.setPos016(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("016"))
                        .map(CurrencyRatesBlend::getBlendHigh)
                        .filter(Objects::nonNull)
                        .findAny().orElse(null));

                standardDeviation.setPos017(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("017"))
                        .map(CurrencyRatesBlend::getBlendHigh)
                        .filter(Objects::nonNull)
                        .findAny().orElse(null));

                standardDeviation.setPos018(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("018"))
                        .map(CurrencyRatesBlend::getBlendHigh)
                        .filter(Objects::nonNull)
                        .findAny().orElse(null));

                standardDeviation.setPos019(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("019"))
                        .map(CurrencyRatesBlend::getBlendHigh)
                        .filter(Objects::nonNull)
                        .findAny().orElse(null));

                standardDeviation.setPos020(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("020"))
                        .map(CurrencyRatesBlend::getBlendHigh)
                        .filter(Objects::nonNull)
                        .findAny().orElse(null));

                standardDeviation.setPos021(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("021"))
                        .map(CurrencyRatesBlend::getBlendHigh)
                        .filter(Objects::nonNull)
                        .findAny().orElse(null));

                standardDeviation.setPos022(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("022"))
                        .map(CurrencyRatesBlend::getBlendHigh)
                        .filter(Objects::nonNull)
                        .findAny().orElse(null));

                standardDeviation.setPos023(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("023"))
                        .map(CurrencyRatesBlend::getBlendHigh)
                        .filter(Objects::nonNull)
                        .findAny().orElse(null));
            }

            break;

            case "BLENDLOW": {
                standardDeviation.setPos001(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("001"))
                        .map(CurrencyRatesBlend::getBlendLow)
                        .filter(Objects::nonNull)
                        .findAny().orElse(null));

                standardDeviation.setPos002(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("002"))
                        .map(CurrencyRatesBlend::getBlendLow)
                        .filter(Objects::nonNull)
                        .findAny().orElse(null));

                standardDeviation.setPos003(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("003"))
                        .map(CurrencyRatesBlend::getBlendLow)
                        .filter(Objects::nonNull)
                        .findAny().orElse(null));

                standardDeviation.setPos004(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("004"))
                        .map(CurrencyRatesBlend::getBlendLow)
                        .filter(Objects::nonNull)
                        .findAny().orElse(null));

                standardDeviation.setPos005(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("005"))
                        .map(CurrencyRatesBlend::getBlendLow)
                        .filter(Objects::nonNull)
                        .findAny().orElse(null));

                standardDeviation.setPos006(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("006"))
                        .map(CurrencyRatesBlend::getBlendLow)
                        .filter(Objects::nonNull)
                        .findAny().orElse(null));

                standardDeviation.setPos007(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("007"))
                        .map(CurrencyRatesBlend::getBlendLow)
                        .filter(Objects::nonNull)
                        .findAny().orElse(null));

                standardDeviation.setPos008(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("008"))
                        .map(CurrencyRatesBlend::getBlendLow)
                        .filter(Objects::nonNull)
                        .findAny().orElse(null));

                standardDeviation.setPos009(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("009"))
                        .map(CurrencyRatesBlend::getBlendLow)
                        .filter(Objects::nonNull)
                        .findAny().orElse(null));

                standardDeviation.setPos010(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("010"))
                        .map(CurrencyRatesBlend::getBlendLow)
                        .filter(Objects::nonNull)
                        .findAny().orElse(null));

                standardDeviation.setPos011(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("011"))
                        .map(CurrencyRatesBlend::getBlendLow)
                        .filter(Objects::nonNull)
                        .findAny().orElse(null));

                standardDeviation.setPos012(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("012"))
                        .map(CurrencyRatesBlend::getBlendLow)
                        .filter(Objects::nonNull)
                        .findAny().orElse(null));

                standardDeviation.setPos013(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("013"))
                        .map(CurrencyRatesBlend::getBlendLow)
                        .filter(Objects::nonNull)
                        .findAny().orElse(null));

                standardDeviation.setPos014(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("014"))
                        .map(CurrencyRatesBlend::getBlendLow)
                        .filter(Objects::nonNull)
                        .findAny().orElse(null));

                standardDeviation.setPos015(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("015"))
                        .map(CurrencyRatesBlend::getBlendLow)
                        .filter(Objects::nonNull)
                        .findAny().orElse(null));

                standardDeviation.setPos016(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("016"))
                        .map(CurrencyRatesBlend::getBlendLow)
                        .filter(Objects::nonNull)
                        .findAny().orElse(null));

                standardDeviation.setPos017(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("017"))
                        .map(CurrencyRatesBlend::getBlendLow)
                        .filter(Objects::nonNull)
                        .findAny().orElse(null));

                standardDeviation.setPos018(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("018"))
                        .map(CurrencyRatesBlend::getBlendLow)
                        .filter(Objects::nonNull)
                        .findAny().orElse(null));

                standardDeviation.setPos019(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("019"))
                        .map(CurrencyRatesBlend::getBlendLow)
                        .filter(Objects::nonNull)
                        .findAny().orElse(null));

                standardDeviation.setPos020(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("020"))
                        .map(CurrencyRatesBlend::getBlendLow)
                        .filter(Objects::nonNull)
                        .findAny().orElse(null));

                standardDeviation.setPos021(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("021"))
                        .map(CurrencyRatesBlend::getBlendLow)
                        .filter(Objects::nonNull)
                        .findAny().orElse(null));

                standardDeviation.setPos022(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("022"))
                        .map(CurrencyRatesBlend::getBlendLow)
                        .filter(Objects::nonNull)
                        .findAny().orElse(null));

                standardDeviation.setPos023(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("023"))
                        .map(CurrencyRatesBlend::getBlendLow)
                        .filter(Objects::nonNull)
                        .findAny().orElse(null));
            }

            break;

            case "BLENDCLOSE": {
                standardDeviation.setPos001(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("001"))
                        .map(CurrencyRatesBlend::getBlendClose)
                        .filter(Objects::nonNull)
                        .findAny().orElse(null));

                standardDeviation.setPos002(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("002"))
                        .map(CurrencyRatesBlend::getBlendClose)
                        .filter(Objects::nonNull)
                        .findAny().orElse(null));

                standardDeviation.setPos003(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("003"))
                        .map(CurrencyRatesBlend::getBlendClose)
                        .filter(Objects::nonNull)
                        .findAny().orElse(null));

                standardDeviation.setPos004(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("004"))
                        .map(CurrencyRatesBlend::getBlendClose)
                        .filter(Objects::nonNull)
                        .findAny().orElse(null));

                standardDeviation.setPos005(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("005"))
                        .map(CurrencyRatesBlend::getBlendClose)
                        .filter(Objects::nonNull)
                        .findAny().orElse(null));

                standardDeviation.setPos006(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("006"))
                        .map(CurrencyRatesBlend::getBlendClose)
                        .filter(Objects::nonNull)
                        .findAny().orElse(null));

                standardDeviation.setPos007(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("007"))
                        .map(CurrencyRatesBlend::getBlendClose)
                        .filter(Objects::nonNull)
                        .findAny().orElse(null));

                standardDeviation.setPos008(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("008"))
                        .map(CurrencyRatesBlend::getBlendClose)
                        .filter(Objects::nonNull)
                        .findAny().orElse(null));

                standardDeviation.setPos009(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("009"))
                        .map(CurrencyRatesBlend::getBlendClose)
                        .filter(Objects::nonNull)
                        .findAny().orElse(null));

                standardDeviation.setPos010(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("010"))
                        .map(CurrencyRatesBlend::getBlendClose)
                        .filter(Objects::nonNull)
                        .findAny().orElse(null));

                standardDeviation.setPos011(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("011"))
                        .map(CurrencyRatesBlend::getBlendClose)
                        .filter(Objects::nonNull)
                        .findAny().orElse(null));

                standardDeviation.setPos012(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("012"))
                        .map(CurrencyRatesBlend::getBlendClose)
                        .filter(Objects::nonNull)
                        .findAny().orElse(null));

                standardDeviation.setPos013(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("013"))
                        .map(CurrencyRatesBlend::getBlendClose)
                        .filter(Objects::nonNull)
                        .findAny().orElse(null));

                standardDeviation.setPos014(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("014"))
                        .map(CurrencyRatesBlend::getBlendClose)
                        .filter(Objects::nonNull)
                        .findAny().orElse(null));

                standardDeviation.setPos015(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("015"))
                        .map(CurrencyRatesBlend::getBlendClose)
                        .filter(Objects::nonNull)
                        .findAny().orElse(null));

                standardDeviation.setPos016(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("016"))
                        .map(CurrencyRatesBlend::getBlendClose)
                        .filter(Objects::nonNull)
                        .findAny().orElse(null));

                standardDeviation.setPos017(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("017"))
                        .map(CurrencyRatesBlend::getBlendClose)
                        .filter(Objects::nonNull)
                        .findAny().orElse(null));

                standardDeviation.setPos018(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("018"))
                        .map(CurrencyRatesBlend::getBlendClose)
                        .filter(Objects::nonNull)
                        .findAny().orElse(null));

                standardDeviation.setPos019(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("019"))
                        .map(CurrencyRatesBlend::getBlendClose)
                        .filter(Objects::nonNull)
                        .findAny().orElse(null));

                standardDeviation.setPos020(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("020"))
                        .map(CurrencyRatesBlend::getBlendClose)
                        .filter(Objects::nonNull)
                        .findAny().orElse(null));

                standardDeviation.setPos021(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("021"))
                        .map(CurrencyRatesBlend::getBlendClose)
                        .filter(Objects::nonNull)
                        .findAny().orElse(null));

                standardDeviation.setPos022(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("022"))
                        .map(CurrencyRatesBlend::getBlendClose)
                        .filter(Objects::nonNull)
                        .findAny().orElse(null));

                standardDeviation.setPos023(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("023"))
                        .map(CurrencyRatesBlend::getBlendClose)
                        .filter(Objects::nonNull)
                        .findAny().orElse(null));
            }

            break;

            case "DAILYRANGE": {
                standardDeviation.setPos001(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("001"))
                        .map(CurrencyRatesBlend::getDailyRange)
                        .filter(Objects::nonNull)
                        .findAny().orElse(null));

                standardDeviation.setPos002(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("002"))
                        .map(CurrencyRatesBlend::getDailyRange)
                        .filter(Objects::nonNull)
                        .findAny().orElse(null));

                standardDeviation.setPos003(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("003"))
                        .map(CurrencyRatesBlend::getDailyRange)
                        .filter(Objects::nonNull)
                        .findAny().orElse(null));

                standardDeviation.setPos004(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("004"))
                        .map(CurrencyRatesBlend::getDailyRange)
                        .filter(Objects::nonNull)
                        .findAny().orElse(null));

                standardDeviation.setPos005(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("005"))
                        .map(CurrencyRatesBlend::getDailyRange)
                        .filter(Objects::nonNull)
                        .findAny().orElse(null));

                standardDeviation.setPos006(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("006"))
                        .map(CurrencyRatesBlend::getDailyRange)
                        .filter(Objects::nonNull)
                        .findAny().orElse(null));

                standardDeviation.setPos007(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("007"))
                        .map(CurrencyRatesBlend::getDailyRange)
                        .filter(Objects::nonNull)
                        .findAny().orElse(null));

                standardDeviation.setPos008(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("008"))
                        .map(CurrencyRatesBlend::getDailyRange)
                        .filter(Objects::nonNull)
                        .findAny().orElse(null));

                standardDeviation.setPos009(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("009"))
                        .map(CurrencyRatesBlend::getDailyRange)
                        .filter(Objects::nonNull)
                        .findAny().orElse(null));

                standardDeviation.setPos010(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("010"))
                        .map(CurrencyRatesBlend::getDailyRange)
                        .filter(Objects::nonNull)
                        .findAny().orElse(null));

                standardDeviation.setPos011(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("011"))
                        .map(CurrencyRatesBlend::getDailyRange)
                        .filter(Objects::nonNull)
                        .findAny().orElse(null));

                standardDeviation.setPos012(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("012"))
                        .map(CurrencyRatesBlend::getDailyRange)
                        .filter(Objects::nonNull)
                        .findAny().orElse(null));

                standardDeviation.setPos013(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("013"))
                        .map(CurrencyRatesBlend::getDailyRange)
                        .filter(Objects::nonNull)
                        .findAny().orElse(null));

                standardDeviation.setPos014(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("014"))
                        .map(CurrencyRatesBlend::getDailyRange)
                        .filter(Objects::nonNull)
                        .findAny().orElse(null));

                standardDeviation.setPos015(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("015"))
                        .map(CurrencyRatesBlend::getDailyRange)
                        .filter(Objects::nonNull)
                        .findAny().orElse(null));

                standardDeviation.setPos016(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("016"))
                        .map(CurrencyRatesBlend::getDailyRange)
                        .filter(Objects::nonNull)
                        .findAny().orElse(null));

                standardDeviation.setPos017(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("017"))
                        .map(CurrencyRatesBlend::getDailyRange)
                        .filter(Objects::nonNull)
                        .findAny().orElse(null));

                standardDeviation.setPos018(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("018"))
                        .map(CurrencyRatesBlend::getDailyRange)
                        .filter(Objects::nonNull)
                        .findAny().orElse(null));

                standardDeviation.setPos019(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("019"))
                        .map(CurrencyRatesBlend::getDailyRange)
                        .filter(Objects::nonNull)
                        .findAny().orElse(null));

                standardDeviation.setPos020(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("020"))
                        .map(CurrencyRatesBlend::getDailyRange)
                        .filter(Objects::nonNull)
                        .findAny().orElse(null));

                standardDeviation.setPos021(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("021"))
                        .map(CurrencyRatesBlend::getDailyRange)
                        .filter(Objects::nonNull)
                        .findAny().orElse(null));

                standardDeviation.setPos022(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("022"))
                        .map(CurrencyRatesBlend::getDailyRange)
                        .filter(Objects::nonNull)
                        .findAny().orElse(null));

                standardDeviation.setPos023(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("023"))
                        .map(CurrencyRatesBlend::getDailyRange)
                        .filter(Objects::nonNull)
                        .findAny().orElse(null));
            }

            break;

            case "LOGCHANGE": {
                standardDeviation.setPos001(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("001"))
                        .map(CurrencyRatesBlend::getLogChance)
                        .filter(Objects::nonNull)
                        .findAny().orElse(null));

                standardDeviation.setPos002(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("002"))
                        .map(CurrencyRatesBlend::getLogChance)
                        .filter(Objects::nonNull)
                        .findAny().orElse(null));

                standardDeviation.setPos003(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("003"))
                        .map(CurrencyRatesBlend::getLogChance)
                        .filter(Objects::nonNull)
                        .findAny().orElse(null));

                standardDeviation.setPos004(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("004"))
                        .map(CurrencyRatesBlend::getLogChance)
                        .filter(Objects::nonNull)
                        .findAny().orElse(null));

                standardDeviation.setPos005(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("005"))
                        .map(CurrencyRatesBlend::getLogChance)
                        .filter(Objects::nonNull)
                        .findAny().orElse(null));

                standardDeviation.setPos006(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("006"))
                        .map(CurrencyRatesBlend::getLogChance)
                        .filter(Objects::nonNull)
                        .findAny().orElse(null));

                standardDeviation.setPos007(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("007"))
                        .map(CurrencyRatesBlend::getLogChance)
                        .filter(Objects::nonNull)
                        .findAny().orElse(null));

                standardDeviation.setPos008(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("008"))
                        .map(CurrencyRatesBlend::getLogChance)
                        .filter(Objects::nonNull)
                        .findAny().orElse(null));

                standardDeviation.setPos009(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("009"))
                        .map(CurrencyRatesBlend::getLogChance)
                        .filter(Objects::nonNull)
                        .findAny().orElse(null));

                standardDeviation.setPos010(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("010"))
                        .map(CurrencyRatesBlend::getLogChance)
                        .filter(Objects::nonNull)
                        .findAny().orElse(null));

                standardDeviation.setPos011(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("011"))
                        .map(CurrencyRatesBlend::getLogChance)
                        .filter(Objects::nonNull)
                        .findAny().orElse(null));

                standardDeviation.setPos012(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("012"))
                        .map(CurrencyRatesBlend::getLogChance)
                        .filter(Objects::nonNull)
                        .findAny().orElse(null));

                standardDeviation.setPos013(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("013"))
                        .map(CurrencyRatesBlend::getLogChance)
                        .filter(Objects::nonNull)
                        .findAny().orElse(null));

                standardDeviation.setPos014(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("014"))
                        .map(CurrencyRatesBlend::getLogChance)
                        .filter(Objects::nonNull)
                        .findAny().orElse(null));

                standardDeviation.setPos015(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("015"))
                        .map(CurrencyRatesBlend::getLogChance)
                        .filter(Objects::nonNull)
                        .findAny().orElse(null));

                standardDeviation.setPos016(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("016"))
                        .map(CurrencyRatesBlend::getLogChance)
                        .filter(Objects::nonNull)
                        .findAny().orElse(null));

                standardDeviation.setPos017(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("017"))
                        .map(CurrencyRatesBlend::getLogChance)
                        .filter(Objects::nonNull)
                        .findAny().orElse(null));

                standardDeviation.setPos018(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("018"))
                        .map(CurrencyRatesBlend::getLogChance)
                        .filter(Objects::nonNull)
                        .findAny().orElse(null));

                standardDeviation.setPos019(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("019"))
                        .map(CurrencyRatesBlend::getLogChance)
                        .filter(Objects::nonNull)
                        .findAny().orElse(null));

                standardDeviation.setPos020(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("020"))
                        .map(CurrencyRatesBlend::getLogChance)
                        .filter(Objects::nonNull)
                        .findAny().orElse(null));

                standardDeviation.setPos021(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("021"))
                        .map(CurrencyRatesBlend::getLogChance)
                        .filter(Objects::nonNull)
                        .findAny().orElse(null));

                standardDeviation.setPos022(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("022"))
                        .map(CurrencyRatesBlend::getLogChance)
                        .filter(Objects::nonNull)
                        .findAny().orElse(null));

                standardDeviation.setPos023(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("023"))
                        .map(CurrencyRatesBlend::getLogChance)
                        .filter(Objects::nonNull)
                        .findAny().orElse(null));
            }
        }
    }


    private Double calculateStDev(StandardDeviation standardDeviation) {
        Double[] numArray = new Double[23];

        numArray[0] = standardDeviation.getPos001();
        numArray[1] = standardDeviation.getPos002();
        numArray[2] = standardDeviation.getPos003();
        numArray[3] = standardDeviation.getPos004();
        numArray[4] = standardDeviation.getPos005();
        numArray[5] = standardDeviation.getPos006();
        numArray[6] = standardDeviation.getPos007();
        numArray[7] = standardDeviation.getPos008();
        numArray[8] = standardDeviation.getPos009();
        numArray[9] = standardDeviation.getPos010();
        numArray[10] = standardDeviation.getPos011();
        numArray[11] = standardDeviation.getPos012();
        numArray[12] = standardDeviation.getPos013();
        numArray[13] = standardDeviation.getPos014();
        numArray[14] = standardDeviation.getPos015();
        numArray[15] = standardDeviation.getPos016();
        numArray[16] = standardDeviation.getPos017();
        numArray[17] = standardDeviation.getPos018();
        numArray[18] = standardDeviation.getPos019();
        numArray[19] = standardDeviation.getPos020();
        numArray[20] = standardDeviation.getPos021();
        numArray[21] = standardDeviation.getPos022();
        numArray[22] = standardDeviation.getPos023();


        Double sum = 0.0, standardDev = 0.0;
        int length = numArray.length;

        for (Double num : numArray) {
            if (Objects.nonNull(num)) {
                sum += num;
            }
        }

        Double mean = sum / length;

        for (Double num : numArray) {
            if (Objects.nonNull(num)) {
                standardDev += Math.pow(num - mean, 2);
            }
        }

        return Math.sqrt(standardDev / (length - 1));
    }

    private Double calculateFiveDayHigh(StandardDeviation standardDeviation) {
        Double[] numArray = new Double[5];

        numArray[0] = standardDeviation.getPos001();
        numArray[1] = standardDeviation.getPos002();
        numArray[2] = standardDeviation.getPos003();
        numArray[3] = standardDeviation.getPos004();
        numArray[4] = standardDeviation.getPos005();

        Double max = numArray[0];

        for (Double v : numArray) {
            if (Objects.nonNull(v)) {
                if (v > max) {
                    max = v;
                }
            }
        }

        return max;
    }

    private Double calculateFiveDayLow(StandardDeviation standardDeviation) {
        Double[] numArray = new Double[5];

        numArray[0] = standardDeviation.getPos001();
        numArray[1] = standardDeviation.getPos002();
        numArray[2] = standardDeviation.getPos003();
        numArray[3] = standardDeviation.getPos004();
        numArray[4] = standardDeviation.getPos005();

        Double min = numArray[0];

        for (Double v : numArray) {
            if (Objects.nonNull(v)) {
                if (v < min) {
                    min = v;
                }
            }
        }

        return min;
    }

    private Double calculateOneMonthHigh(StandardDeviation standardDeviation) {
        Double[] numArray = new Double[23];

        numArray[0] = standardDeviation.getPos001();
        numArray[1] = standardDeviation.getPos002();
        numArray[2] = standardDeviation.getPos003();
        numArray[3] = standardDeviation.getPos004();
        numArray[4] = standardDeviation.getPos005();
        numArray[5] = standardDeviation.getPos006();
        numArray[6] = standardDeviation.getPos007();
        numArray[7] = standardDeviation.getPos008();
        numArray[8] = standardDeviation.getPos009();
        numArray[9] = standardDeviation.getPos010();
        numArray[10] = standardDeviation.getPos011();
        numArray[11] = standardDeviation.getPos012();
        numArray[12] = standardDeviation.getPos013();
        numArray[13] = standardDeviation.getPos014();
        numArray[14] = standardDeviation.getPos015();
        numArray[15] = standardDeviation.getPos016();
        numArray[16] = standardDeviation.getPos017();
        numArray[17] = standardDeviation.getPos018();
        numArray[18] = standardDeviation.getPos019();
        numArray[19] = standardDeviation.getPos020();
        numArray[20] = standardDeviation.getPos021();
        numArray[21] = standardDeviation.getPos022();
        numArray[22] = standardDeviation.getPos023();

        Double max = numArray[0];

        for (Double v : numArray) {
            if (Objects.nonNull(v)) {
                if (v > max) {
                    max = v;
                }
            }
        }

        return max;
    }

    private Double calculateOneMonthLow(StandardDeviation standardDeviation) {
        Double[] numArray = new Double[23];

        numArray[0] = standardDeviation.getPos001();
        numArray[1] = standardDeviation.getPos002();
        numArray[2] = standardDeviation.getPos003();
        numArray[3] = standardDeviation.getPos004();
        numArray[4] = standardDeviation.getPos005();
        numArray[5] = standardDeviation.getPos006();
        numArray[6] = standardDeviation.getPos007();
        numArray[7] = standardDeviation.getPos008();
        numArray[8] = standardDeviation.getPos009();
        numArray[9] = standardDeviation.getPos010();
        numArray[10] = standardDeviation.getPos011();
        numArray[11] = standardDeviation.getPos012();
        numArray[12] = standardDeviation.getPos013();
        numArray[13] = standardDeviation.getPos014();
        numArray[14] = standardDeviation.getPos015();
        numArray[15] = standardDeviation.getPos016();
        numArray[16] = standardDeviation.getPos017();
        numArray[17] = standardDeviation.getPos018();
        numArray[18] = standardDeviation.getPos019();
        numArray[19] = standardDeviation.getPos020();
        numArray[20] = standardDeviation.getPos021();
        numArray[21] = standardDeviation.getPos022();
        numArray[22] = standardDeviation.getPos023();

        Double min = numArray[0];

        for (Double v : numArray) {
            if (Objects.nonNull(v)) {
                if (v < min) {
                    min = v;
                }
            }
        }

        return min;
    }

    private Double calculateOneMonthRvol(StandardDeviation standardDeviation) {
        return standardDeviation.getOneMthStdDev() * Math.sqrt(252);
    }
}
