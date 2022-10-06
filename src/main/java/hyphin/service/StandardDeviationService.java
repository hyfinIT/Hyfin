package hyphin.service;

import hyphin.model.currency.Blend;
import hyphin.model.currency.BlendEurUsd;
import hyphin.model.currency.BlendGbpUsd;
import hyphin.model.currency.BlendUsdJpy;
import hyphin.model.currency.OperationAudit;
import hyphin.model.currency.StandardDeviation;
import hyphin.repository.currency.BlendEurUsdRepository;
import hyphin.repository.currency.BlendGbpUsdRepository;
import hyphin.repository.currency.BlendUsdJpyRepository;
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
    private final BlendEurUsdRepository eurUsdRepository;
    private final BlendGbpUsdRepository gbpUsdRepository;
    private final BlendUsdJpyRepository usdJpyRepository;

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

    @Scheduled(cron = "0 0 3 * * *")
    public void createStandardDeviation() {
        log.info("Standard deviation calculation..................");
        OperationAudit operationAudit = new OperationAudit();
        operationAudit.setId(operationAuditRepository.maxId().orElse(0L) + 1L);
        operationAudit.setName("Standard deviation calculation");
        operationAudit.setDateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));


       try {
           standardDeviationRepository.findAll();


           List<BlendEurUsd> eurUsdAll = eurUsdRepository.findAll();
           List<BlendGbpUsd> gbpUsdAll = gbpUsdRepository.findAll();
           List<BlendUsdJpy> usdJpyAll = usdJpyRepository.findAll();

           processBlends(eurUsdAll);
           processBlends(gbpUsdAll);
           processBlends(usdJpyAll);

           operationAudit.setStatus("SUCCESS");
           operationAuditRepository.save(operationAudit);
       } catch (Exception e) {
           operationAudit.setStatus("FAIL");
           operationAuditRepository.save(operationAudit);
       }
    }

    private void processBlends(List<? extends Blend> eurUsdAll) {
        eurUsdAll.sort(Comparator.comparing(Blend::getPosition));

        Blend firstPositionBlend = eurUsdAll
                .stream()
                .filter(blendEurUsd -> blendEurUsd.getPosition().equals("001"))
                .findAny()
                .orElseThrow(() -> new RuntimeException("No blend with 001 position found"));

        for (int i = 0; i < rateTypes.size(); i++) {
            StandardDeviation standardDeviation = new StandardDeviation();
            standardDeviation.setCcyPair("EUR/USD");
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

    private static void setPosition(List<? extends Blend> eurUsdAll, int i, StandardDeviation standardDeviation) {
        switch (rateTypes.get(i)) {
            case "BLENDOPEN": {
                standardDeviation.setPos001(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("001"))
                        .map(Blend::getBlendOpen)
                        .findAny().orElse(null));

                standardDeviation.setPos002(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("002"))
                        .map(Blend::getBlendOpen)
                        .findAny().orElse(null));

                standardDeviation.setPos003(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("003"))
                        .map(Blend::getBlendOpen)
                        .findAny().orElse(null));

                standardDeviation.setPos004(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("004"))
                        .map(Blend::getBlendOpen)
                        .findAny().orElse(null));

                standardDeviation.setPos005(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("005"))
                        .map(Blend::getBlendOpen)
                        .findAny().orElse(null));

                standardDeviation.setPos006(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("006"))
                        .map(Blend::getBlendOpen)
                        .findAny().orElse(null));

                standardDeviation.setPos007(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("007"))
                        .map(Blend::getBlendOpen)
                        .findAny().orElse(null));

                standardDeviation.setPos008(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("008"))
                        .map(Blend::getBlendOpen)
                        .findAny().orElse(null));

                standardDeviation.setPos009(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("009"))
                        .map(Blend::getBlendOpen)
                        .findAny().orElse(null));

                standardDeviation.setPos010(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("0010"))
                        .map(Blend::getBlendOpen)
                        .findAny().orElse(null));

                standardDeviation.setPos011(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("011"))
                        .map(Blend::getBlendOpen)
                        .findAny().orElse(null));

                standardDeviation.setPos012(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("012"))
                        .map(Blend::getBlendOpen)
                        .findAny().orElse(null));

                standardDeviation.setPos013(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("013"))
                        .map(Blend::getBlendOpen)
                        .findAny().orElse(null));

                standardDeviation.setPos014(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("014"))
                        .map(Blend::getBlendOpen)
                        .findAny().orElse(null));

                standardDeviation.setPos015(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("015"))
                        .map(Blend::getBlendOpen)
                        .findAny().orElse(null));

                standardDeviation.setPos016(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("016"))
                        .map(Blend::getBlendOpen)
                        .findAny().orElse(null));

                standardDeviation.setPos017(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("017"))
                        .map(Blend::getBlendOpen)
                        .findAny().orElse(null));

                standardDeviation.setPos018(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("018"))
                        .map(Blend::getBlendOpen)
                        .findAny().orElse(null));

                standardDeviation.setPos019(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("019"))
                        .map(Blend::getBlendOpen)
                        .findAny().orElse(null));

                standardDeviation.setPos020(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("020"))
                        .map(Blend::getBlendOpen)
                        .findAny().orElse(null));

                standardDeviation.setPos021(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("021"))
                        .map(Blend::getBlendOpen)
                        .findAny().orElse(null));

                standardDeviation.setPos022(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("022"))
                        .map(Blend::getBlendOpen)
                        .findAny().orElse(null));

                standardDeviation.setPos023(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("023"))
                        .map(Blend::getBlendOpen)
                        .findAny().orElse(null));
            }

            break;

            case "BLENDHIGH": {
                standardDeviation.setPos001(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("001"))
                        .map(Blend::getBlendHigh)
                        .findAny().orElse(null));

                standardDeviation.setPos002(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("002"))
                        .map(Blend::getBlendHigh)
                        .findAny().orElse(null));

                standardDeviation.setPos003(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("003"))
                        .map(Blend::getBlendHigh)
                        .findAny().orElse(null));

                standardDeviation.setPos004(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("004"))
                        .map(Blend::getBlendHigh)
                        .findAny().orElse(null));

                standardDeviation.setPos005(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("005"))
                        .map(Blend::getBlendHigh)
                        .findAny().orElse(null));

                standardDeviation.setPos006(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("006"))
                        .map(Blend::getBlendHigh)
                        .findAny().orElse(null));

                standardDeviation.setPos007(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("007"))
                        .map(Blend::getBlendHigh)
                        .findAny().orElse(null));

                standardDeviation.setPos008(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("008"))
                        .map(Blend::getBlendHigh)
                        .findAny().orElse(null));

                standardDeviation.setPos009(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("009"))
                        .map(Blend::getBlendHigh)
                        .findAny().orElse(null));

                standardDeviation.setPos010(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("0010"))
                        .map(Blend::getBlendHigh)
                        .findAny().orElse(null));

                standardDeviation.setPos011(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("011"))
                        .map(Blend::getBlendHigh)
                        .findAny().orElse(null));

                standardDeviation.setPos012(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("012"))
                        .map(Blend::getBlendHigh)
                        .findAny().orElse(null));

                standardDeviation.setPos013(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("013"))
                        .map(Blend::getBlendHigh)
                        .findAny().orElse(null));

                standardDeviation.setPos014(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("014"))
                        .map(Blend::getBlendHigh)
                        .findAny().orElse(null));

                standardDeviation.setPos015(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("015"))
                        .map(Blend::getBlendHigh)
                        .findAny().orElse(null));

                standardDeviation.setPos016(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("016"))
                        .map(Blend::getBlendHigh)
                        .findAny().orElse(null));

                standardDeviation.setPos017(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("017"))
                        .map(Blend::getBlendHigh)
                        .findAny().orElse(null));

                standardDeviation.setPos018(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("018"))
                        .map(Blend::getBlendHigh)
                        .findAny().orElse(null));

                standardDeviation.setPos019(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("019"))
                        .map(Blend::getBlendHigh)
                        .findAny().orElse(null));

                standardDeviation.setPos020(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("020"))
                        .map(Blend::getBlendHigh)
                        .findAny().orElse(null));

                standardDeviation.setPos021(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("021"))
                        .map(Blend::getBlendHigh)
                        .findAny().orElse(null));

                standardDeviation.setPos022(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("022"))
                        .map(Blend::getBlendHigh)
                        .findAny().orElse(null));

                standardDeviation.setPos023(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("023"))
                        .map(Blend::getBlendHigh)
                        .findAny().orElse(null));
            }

            break;

            case "BLENDLOW": {
                standardDeviation.setPos001(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("001"))
                        .map(Blend::getBlendLow)
                        .findAny().orElse(null));

                standardDeviation.setPos002(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("002"))
                        .map(Blend::getBlendLow)
                        .findAny().orElse(null));

                standardDeviation.setPos003(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("003"))
                        .map(Blend::getBlendLow)
                        .findAny().orElse(null));

                standardDeviation.setPos004(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("004"))
                        .map(Blend::getBlendLow)
                        .findAny().orElse(null));

                standardDeviation.setPos005(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("005"))
                        .map(Blend::getBlendLow)
                        .findAny().orElse(null));

                standardDeviation.setPos006(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("006"))
                        .map(Blend::getBlendLow)
                        .findAny().orElse(null));

                standardDeviation.setPos007(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("007"))
                        .map(Blend::getBlendLow)
                        .findAny().orElse(null));

                standardDeviation.setPos008(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("008"))
                        .map(Blend::getBlendLow)
                        .findAny().orElse(null));

                standardDeviation.setPos009(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("009"))
                        .map(Blend::getBlendLow)
                        .findAny().orElse(null));

                standardDeviation.setPos010(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("0010"))
                        .map(Blend::getBlendLow)
                        .findAny().orElse(null));

                standardDeviation.setPos011(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("011"))
                        .map(Blend::getBlendLow)
                        .findAny().orElse(null));

                standardDeviation.setPos012(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("012"))
                        .map(Blend::getBlendLow)
                        .findAny().orElse(null));

                standardDeviation.setPos013(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("013"))
                        .map(Blend::getBlendLow)
                        .findAny().orElse(null));

                standardDeviation.setPos014(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("014"))
                        .map(Blend::getBlendLow)
                        .findAny().orElse(null));

                standardDeviation.setPos015(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("015"))
                        .map(Blend::getBlendLow)
                        .findAny().orElse(null));

                standardDeviation.setPos016(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("016"))
                        .map(Blend::getBlendLow)
                        .findAny().orElse(null));

                standardDeviation.setPos017(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("017"))
                        .map(Blend::getBlendLow)
                        .findAny().orElse(null));

                standardDeviation.setPos018(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("018"))
                        .map(Blend::getBlendLow)
                        .findAny().orElse(null));

                standardDeviation.setPos019(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("019"))
                        .map(Blend::getBlendLow)
                        .findAny().orElse(null));

                standardDeviation.setPos020(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("020"))
                        .map(Blend::getBlendLow)
                        .findAny().orElse(null));

                standardDeviation.setPos021(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("021"))
                        .map(Blend::getBlendLow)
                        .findAny().orElse(null));

                standardDeviation.setPos022(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("022"))
                        .map(Blend::getBlendLow)
                        .findAny().orElse(null));

                standardDeviation.setPos023(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("023"))
                        .map(Blend::getBlendLow)
                        .findAny().orElse(null));
            }

            break;

            case "BLENDCLOSE": {
                standardDeviation.setPos001(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("001"))
                        .map(Blend::getBlendClose)
                        .findAny().orElse(null));

                standardDeviation.setPos002(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("002"))
                        .map(Blend::getBlendClose)
                        .findAny().orElse(null));

                standardDeviation.setPos003(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("003"))
                        .map(Blend::getBlendClose)
                        .findAny().orElse(null));

                standardDeviation.setPos004(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("004"))
                        .map(Blend::getBlendClose)
                        .findAny().orElse(null));

                standardDeviation.setPos005(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("005"))
                        .map(Blend::getBlendClose)
                        .findAny().orElse(null));

                standardDeviation.setPos006(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("006"))
                        .map(Blend::getBlendClose)
                        .findAny().orElse(null));

                standardDeviation.setPos007(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("007"))
                        .map(Blend::getBlendClose)
                        .findAny().orElse(null));

                standardDeviation.setPos008(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("008"))
                        .map(Blend::getBlendClose)
                        .findAny().orElse(null));

                standardDeviation.setPos009(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("009"))
                        .map(Blend::getBlendClose)
                        .findAny().orElse(null));

                standardDeviation.setPos010(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("0010"))
                        .map(Blend::getBlendClose)
                        .findAny().orElse(null));

                standardDeviation.setPos011(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("011"))
                        .map(Blend::getBlendClose)
                        .findAny().orElse(null));

                standardDeviation.setPos012(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("012"))
                        .map(Blend::getBlendClose)
                        .findAny().orElse(null));

                standardDeviation.setPos013(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("013"))
                        .map(Blend::getBlendClose)
                        .findAny().orElse(null));

                standardDeviation.setPos014(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("014"))
                        .map(Blend::getBlendClose)
                        .findAny().orElse(null));

                standardDeviation.setPos015(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("015"))
                        .map(Blend::getBlendClose)
                        .findAny().orElse(null));

                standardDeviation.setPos016(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("016"))
                        .map(Blend::getBlendClose)
                        .findAny().orElse(null));

                standardDeviation.setPos017(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("017"))
                        .map(Blend::getBlendClose)
                        .findAny().orElse(null));

                standardDeviation.setPos018(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("018"))
                        .map(Blend::getBlendClose)
                        .findAny().orElse(null));

                standardDeviation.setPos019(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("019"))
                        .map(Blend::getBlendClose)
                        .findAny().orElse(null));

                standardDeviation.setPos020(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("020"))
                        .map(Blend::getBlendClose)
                        .findAny().orElse(null));

                standardDeviation.setPos021(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("021"))
                        .map(Blend::getBlendClose)
                        .findAny().orElse(null));

                standardDeviation.setPos022(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("022"))
                        .map(Blend::getBlendClose)
                        .findAny().orElse(null));

                standardDeviation.setPos023(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("023"))
                        .map(Blend::getBlendClose)
                        .findAny().orElse(null));
            }

            break;

            case "DAILYRANGE": {
                standardDeviation.setPos001(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("001"))
                        .map(Blend::getDailyRange)
                        .findAny().orElse(null));

                standardDeviation.setPos002(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("002"))
                        .map(Blend::getDailyRange)
                        .findAny().orElse(null));

                standardDeviation.setPos003(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("003"))
                        .map(Blend::getDailyRange)
                        .findAny().orElse(null));

                standardDeviation.setPos004(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("004"))
                        .map(Blend::getDailyRange)
                        .findAny().orElse(null));

                standardDeviation.setPos005(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("005"))
                        .map(Blend::getDailyRange)
                        .findAny().orElse(null));

                standardDeviation.setPos006(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("006"))
                        .map(Blend::getDailyRange)
                        .findAny().orElse(null));

                standardDeviation.setPos007(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("007"))
                        .map(Blend::getDailyRange)
                        .findAny().orElse(null));

                standardDeviation.setPos008(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("008"))
                        .map(Blend::getDailyRange)
                        .findAny().orElse(null));

                standardDeviation.setPos009(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("009"))
                        .map(Blend::getDailyRange)
                        .findAny().orElse(null));

                standardDeviation.setPos010(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("0010"))
                        .map(Blend::getDailyRange)
                        .findAny().orElse(null));

                standardDeviation.setPos011(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("011"))
                        .map(Blend::getDailyRange)
                        .findAny().orElse(null));

                standardDeviation.setPos012(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("012"))
                        .map(Blend::getDailyRange)
                        .findAny().orElse(null));

                standardDeviation.setPos013(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("013"))
                        .map(Blend::getDailyRange)
                        .findAny().orElse(null));

                standardDeviation.setPos014(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("014"))
                        .map(Blend::getDailyRange)
                        .findAny().orElse(null));

                standardDeviation.setPos015(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("015"))
                        .map(Blend::getDailyRange)
                        .findAny().orElse(null));

                standardDeviation.setPos016(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("016"))
                        .map(Blend::getDailyRange)
                        .findAny().orElse(null));

                standardDeviation.setPos017(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("017"))
                        .map(Blend::getDailyRange)
                        .findAny().orElse(null));

                standardDeviation.setPos018(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("018"))
                        .map(Blend::getDailyRange)
                        .findAny().orElse(null));

                standardDeviation.setPos019(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("019"))
                        .map(Blend::getDailyRange)
                        .findAny().orElse(null));

                standardDeviation.setPos020(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("020"))
                        .map(Blend::getDailyRange)
                        .findAny().orElse(null));

                standardDeviation.setPos021(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("021"))
                        .map(Blend::getDailyRange)
                        .findAny().orElse(null));

                standardDeviation.setPos022(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("022"))
                        .map(Blend::getDailyRange)
                        .findAny().orElse(null));

                standardDeviation.setPos023(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("023"))
                        .map(Blend::getDailyRange)
                        .findAny().orElse(null));
            }

            break;

            case "LOGCHANGE": {
                standardDeviation.setPos001(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("001"))
                        .map(Blend::getLogChance)
                        .findAny().orElse(null));

                standardDeviation.setPos002(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("002"))
                        .map(Blend::getLogChance)
                        .findAny().orElse(null));

                standardDeviation.setPos003(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("003"))
                        .map(Blend::getLogChance)
                        .findAny().orElse(null));

                standardDeviation.setPos004(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("004"))
                        .map(Blend::getLogChance)
                        .findAny().orElse(null));

                standardDeviation.setPos005(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("005"))
                        .map(Blend::getLogChance)
                        .findAny().orElse(null));

                standardDeviation.setPos006(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("006"))
                        .map(Blend::getLogChance)
                        .findAny().orElse(null));

                standardDeviation.setPos007(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("007"))
                        .map(Blend::getLogChance)
                        .findAny().orElse(null));

                standardDeviation.setPos008(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("008"))
                        .map(Blend::getLogChance)
                        .findAny().orElse(null));

                standardDeviation.setPos009(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("009"))
                        .map(Blend::getLogChance)
                        .findAny().orElse(null));

                standardDeviation.setPos010(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("0010"))
                        .map(Blend::getLogChance)
                        .findAny().orElse(null));

                standardDeviation.setPos011(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("011"))
                        .map(Blend::getLogChance)
                        .findAny().orElse(null));

                standardDeviation.setPos012(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("012"))
                        .map(Blend::getLogChance)
                        .findAny().orElse(null));

                standardDeviation.setPos013(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("013"))
                        .map(Blend::getLogChance)
                        .findAny().orElse(null));

                standardDeviation.setPos014(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("014"))
                        .map(Blend::getLogChance)
                        .findAny().orElse(null));

                standardDeviation.setPos015(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("015"))
                        .map(Blend::getLogChance)
                        .findAny().orElse(null));

                standardDeviation.setPos016(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("016"))
                        .map(Blend::getLogChance)
                        .findAny().orElse(null));

                standardDeviation.setPos017(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("017"))
                        .map(Blend::getLogChance)
                        .findAny().orElse(null));

                standardDeviation.setPos018(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("018"))
                        .map(Blend::getLogChance)
                        .findAny().orElse(null));

                standardDeviation.setPos019(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("019"))
                        .map(Blend::getLogChance)
                        .findAny().orElse(null));

                standardDeviation.setPos020(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("020"))
                        .map(Blend::getLogChance)
                        .findAny().orElse(null));

                standardDeviation.setPos021(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("021"))
                        .map(Blend::getLogChance)
                        .findAny().orElse(null));

                standardDeviation.setPos022(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("022"))
                        .map(Blend::getLogChance)
                        .findAny().orElse(null));

                standardDeviation.setPos023(eurUsdAll
                        .stream()
                        .filter(blendEurUsd -> blendEurUsd.getPosition().equals("023"))
                        .map(Blend::getLogChance)
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
