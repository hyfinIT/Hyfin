package hyphin.service.operation;

import hyphin.model.*;
import hyphin.model.currency.CurrencyRatesBlend;
import hyphin.model.currency.OperationAudit;
import hyphin.repository.*;
import hyphin.repository.currency.CurrencyRatesBlendRepository;
import hyphin.repository.currency.OperationAuditRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static hyphin.util.HyfinUtils.CCY_PAIRS_DICTIONARY;
import static hyphin.util.HyfinUtils.formatDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
public class EcStaticDataDailyService {

    private final CurrencyRatesBlendRepository currencyRatesBlendRepository;
    private final EcStaticDataDailyRepository ecStaticDataDailyRepository;
    private final CcyPairRepository ccyPairRepository;
    private final AtmosphericsRepository atmosphericsRepository;
    private final InflationRateRepository inflationRateRepository;
    private final EcStaticDataRepository ecStaticDataRepository;
    private final OperationAuditRepository operationAuditRepository;

    public void doOperation() {
        List<EcStaticDataDaily> list = new ArrayList<>();

        try {
            for (int i = 0; i < CCY_PAIRS_DICTIONARY.size(); i++) {
                CurrencyRatesBlend lastCurrencyBlend = currencyRatesBlendRepository.findByPositionAndCcyPair("001", CCY_PAIRS_DICTIONARY.get(i));
                    EcStaticDataDaily ecStaticDataDaily = mapBlendToEcStaticDataDaily(lastCurrencyBlend);
                    EcStaticDataDaily existingEntity = ecStaticDataDailyRepository.getByCcyPairAndDate(ecStaticDataDaily.getCcyPair(), LocalDate.now());
                    if (Objects.isNull(existingEntity)) {
                        ecStaticDataDailyRepository.save(ecStaticDataDaily);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            operationAuditRepository.save(getOperationAuditWithStatus("ERROR"));
            return;
        }

        operationAuditRepository.save(getOperationAuditWithStatus("SUCCESS"));
    }

    private OperationAudit getOperationAuditWithStatus(String status) {
        OperationAudit operationAudit = new OperationAudit();
        operationAudit.setName("EC_STATIC_DATA_DAILY_CALCULATING");
        operationAudit.setStatus(status);
        operationAudit.setDateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        return operationAudit;
    }

    private EcStaticDataDaily mapBlendToEcStaticDataDaily(CurrencyRatesBlend currencyRatesBlend) {
        String base = currencyRatesBlend.getCcyPair().substring(0, 3);
        String term =  currencyRatesBlend.getCcyPair().substring(4);
        
        CcyPair ccyPair = ccyPairRepository.getDefaultByName(base + term);

        EcStaticDataDaily ecStaticDataDaily = new EcStaticDataDaily();
        ecStaticDataDaily.setStringDate(currencyRatesBlend.getDate());
        ecStaticDataDaily.setDate(LocalDate.parse(currencyRatesBlend.getDate()));
        ecStaticDataDaily.setRegion(ccyPair.getRegion());
        ecStaticDataDaily.setCcyPairGroup(ccyPair.getDisplayPriority());
        ecStaticDataDaily.setCcyPair(currencyRatesBlend.getCcyPair());
        ecStaticDataDaily.setCcyPairBase(ccyPair.getCurrencyPair().substring(0, 3));
        ecStaticDataDaily.setCcyPairTerm(ccyPair.getCurrencyPair().substring(3, 6));

        ecStaticDataDaily.setCcyPairSpread(ccyPair.getSpread());

        ecStaticDataDaily.setCcyPairMid(currencyRatesBlend.getBlendClose());
        //here
        ecStaticDataDaily.setCcyPairBid(ecStaticDataDaily.getCcyPairMid() - ecStaticDataDaily.getCcyPairSpread());
        ecStaticDataDaily.setCcyPairAsk(ecStaticDataDaily.getCcyPairMid() + ecStaticDataDaily.getCcyPairSpread());
        ecStaticDataDaily.setAtmosStage("STG001");

//        to here

        Atmospherics baseAtmData = atmosphericsRepository.getByCcyAndStageAndCategory(base, "STG001", "Data");
        Atmospherics baseAtmCb = atmosphericsRepository.getByCcyAndStageAndCategory(base, "STG001", "Central Bank");
        Atmospherics baseAtmMp = atmosphericsRepository.getByCcyAndStageAndCategory(base, "STG001", "Macro Politics");
        
        ecStaticDataDaily.setBaseAtmosDa(baseAtmData.getItem());
        ecStaticDataDaily.setBaseAtmosCb(baseAtmCb.getItem());
        ecStaticDataDaily.setBaseAtmosMp(baseAtmMp.getItem());

        ecStaticDataDaily.setBaseAtmosDaTs(baseAtmData.getTrendScore());
        ecStaticDataDaily.setBaseAtmosCbTs(baseAtmCb.getTrendScore());
        ecStaticDataDaily.setBaseAtmosMpTs(baseAtmMp.getTrendScore());

        ecStaticDataDaily.setBaseAtmosDaCts(baseAtmData.getCTrendScore());
        ecStaticDataDaily.setBaseAtmosCbCts(baseAtmCb.getCTrendScore());
        ecStaticDataDaily.setBaseAtmosMpCts(baseAtmMp.getCTrendScore());

        Atmospherics termAtmData = atmosphericsRepository.getByCcyAndStageAndCategory(term, "STG001", "Data");
        Atmospherics termAtmCb = atmosphericsRepository.getByCcyAndStageAndCategory(term, "STG001", "Central Bank");
        Atmospherics termAtmMp = atmosphericsRepository.getByCcyAndStageAndCategory(term, "STG001", "Macro Politics");

        ecStaticDataDaily.setTermAtmosDa(termAtmData.getItem());
        ecStaticDataDaily.setTermAtmosCb(termAtmCb.getItem());
        ecStaticDataDaily.setTermAtmosMp(termAtmMp.getItem());

        ecStaticDataDaily.setTermAtmosDaTs(termAtmData.getTrendScore());
        ecStaticDataDaily.setTermAtmosCbTs(termAtmCb.getTrendScore());
        ecStaticDataDaily.setTermAtmosMpTs(termAtmMp.getTrendScore());

        ecStaticDataDaily.setTermAtmosDaCts(termAtmData.getCTrendScore());
        ecStaticDataDaily.setTermAtmosCbCts(termAtmCb.getCTrendScore());
        ecStaticDataDaily.setTermAtmosMpCts(termAtmMp.getCTrendScore());


        InflationRate inflationRateBase = inflationRateRepository.getAllByPosition(ecStaticDataDaily.getCcyPairBase() + "01")
                .stream().min(Comparator.comparing(InflationRate::getTime))
                .orElse(null);

        InflationRate inflationRateTerm = inflationRateRepository.getAllByPosition(ecStaticDataDaily.getCcyPairTerm() + "01")
                .stream().min(Comparator.comparing(InflationRate::getTime))
                .orElse(null);

        ecStaticDataDaily.setBaseAtmosInfRate(inflationRateBase.getInfRate());
        ecStaticDataDaily.setBaseAtmosInfTrend(inflationRateBase.getInfTrend());

        ecStaticDataDaily.setTermAtmosInfRate(inflationRateTerm.getInfRate());
        ecStaticDataDaily.setTermAtmosInflTrend(inflationRateTerm.getInfTrend());

        ecStaticDataDaily.setCcyPairTradedRateBullish(ecStaticDataDaily.getCcyPairAsk());
        ecStaticDataDaily.setCcyPairTradedRateBearish(ecStaticDataDaily.getCcyPairBid());
        ecStaticDataDaily.setCcyPairPartRate(subStringLeftDouble(ecStaticDataDaily.getCcyPairTradedRateBullish()));
        //pips
        ecStaticDataDaily.setCcyPairBidPips(subStringRightDouble(ecStaticDataDaily.getCcyPairTradedRateBearish()).intValue());
        ecStaticDataDaily.setCcyPairAskPips(subStringRightDouble(ecStaticDataDaily.getCcyPairTradedRateBullish()).intValue());

        EcStaticData ecStaticData = ecStaticDataRepository.getByClient("Default");

        ecStaticDataDaily.setUserMarginCcy(ecStaticData.getUserMarginCcy());
        ecStaticDataDaily.setUserMargin(ecStaticData.getUserMargin().doubleValue());
        ecStaticDataDaily.setBrokerLeverage(ecStaticData.getBrokerLeverage());

        ecStaticDataDaily.setMaxPosSizeBase(Double.valueOf(formatDecimal(ecStaticDataDaily.getCcyPairBase().equals("USD") ?
                ecStaticDataDaily.getUserMargin() * ecStaticDataDaily.getBrokerLeverage() :
                ecStaticDataDaily.getUserMargin() * ecStaticDataDaily.getBrokerLeverage() / ecStaticDataDaily.getCcyPairMid()
                , 2)));

        ecStaticDataDaily.setMaxPosSizeTerm(Double.valueOf(formatDecimal(ecStaticDataDaily.getCcyPairBase().equals("USD") ?
                ecStaticDataDaily.getUserMargin() * ecStaticDataDaily.getBrokerLeverage() / ecStaticDataDaily.getCcyPairMid() :
                ecStaticDataDaily.getUserMargin() * ecStaticDataDaily.getBrokerLeverage()
                , 2)));

        ecStaticDataDaily.setMrgSizeVariable01(ecStaticData.getMrgSizeVariable01());
        ecStaticDataDaily.setMrgSizeVariable02(ecStaticData.getMrgSizeVariable02());
        ecStaticDataDaily.setMrgSizeVariable03(ecStaticData.getMrgSizeVariable03());
        ecStaticDataDaily.setMrgSizeVariable04(ecStaticData.getMrgSizeVariable04());
        ecStaticDataDaily.setMrgSizeVariable05(ecStaticData.getMrgSizeVariable05());
        ecStaticDataDaily.setMrgSizeVariable06(ecStaticData.getMrgSizeVariable06());
        ecStaticDataDaily.setMrgSizeVariable07(ecStaticData.getMrgSizeVariable07());
        ecStaticDataDaily.setMrgSizeVariable08(ecStaticData.getMrgSizeVariable08());

        ecStaticDataDaily.setMrgSize01(ecStaticDataDaily.getUserMargin() * ecStaticDataDaily.getMrgSizeVariable01());
        ecStaticDataDaily.setMrgSize02(ecStaticDataDaily.getUserMargin() * ecStaticDataDaily.getMrgSizeVariable02());
        ecStaticDataDaily.setMrgSize03(ecStaticDataDaily.getUserMargin() * ecStaticDataDaily.getMrgSizeVariable03());
        ecStaticDataDaily.setMrgSize04(ecStaticDataDaily.getUserMargin() * ecStaticDataDaily.getMrgSizeVariable04());
        ecStaticDataDaily.setMrgSize05(ecStaticDataDaily.getUserMargin() * ecStaticDataDaily.getMrgSizeVariable05());
        ecStaticDataDaily.setMrgSize06(ecStaticDataDaily.getUserMargin() * ecStaticDataDaily.getMrgSizeVariable06());
        ecStaticDataDaily.setMrgSize07(ecStaticDataDaily.getUserMargin() * ecStaticDataDaily.getMrgSizeVariable07());
        ecStaticDataDaily.setMrgSize08(ecStaticDataDaily.getUserMargin() * ecStaticDataDaily.getMrgSizeVariable08());

        ecStaticDataDaily.setPosSize01(ecStaticDataDaily.getBrokerLeverage() * ecStaticDataDaily.getMrgSize01());
        ecStaticDataDaily.setPosSize02(ecStaticDataDaily.getBrokerLeverage() * ecStaticDataDaily.getMrgSize02());
        ecStaticDataDaily.setPosSize03(ecStaticDataDaily.getBrokerLeverage() * ecStaticDataDaily.getMrgSize03());
        ecStaticDataDaily.setPosSize04(ecStaticDataDaily.getBrokerLeverage() * ecStaticDataDaily.getMrgSize04());
        ecStaticDataDaily.setPosSize05(ecStaticDataDaily.getBrokerLeverage() * ecStaticDataDaily.getMrgSize05());
        ecStaticDataDaily.setPosSize06(ecStaticDataDaily.getBrokerLeverage() * ecStaticDataDaily.getMrgSize06());
        ecStaticDataDaily.setPosSize07(ecStaticDataDaily.getBrokerLeverage() * ecStaticDataDaily.getMrgSize07());
        ecStaticDataDaily.setPosSize08(ecStaticDataDaily.getBrokerLeverage() * ecStaticDataDaily.getMrgSize08());

        ecStaticDataDaily.setUserCapitalPosSize01(ecStaticDataDaily.getUserMargin() - ecStaticDataDaily.getMrgSize01());
        ecStaticDataDaily.setUserCapitalPosSize02(ecStaticDataDaily.getUserMargin() - ecStaticDataDaily.getMrgSize02());
        ecStaticDataDaily.setUserCapitalPosSize03(ecStaticDataDaily.getUserMargin() - ecStaticDataDaily.getMrgSize03());
        ecStaticDataDaily.setUserCapitalPosSize04(ecStaticDataDaily.getUserMargin() - ecStaticDataDaily.getMrgSize04());
        ecStaticDataDaily.setUserCapitalPosSize05(ecStaticDataDaily.getUserMargin() - ecStaticDataDaily.getMrgSize05());
        ecStaticDataDaily.setUserCapitalPosSize06(ecStaticDataDaily.getUserMargin() - ecStaticDataDaily.getMrgSize06());
        ecStaticDataDaily.setUserCapitalPosSize07(ecStaticDataDaily.getUserMargin() - ecStaticDataDaily.getMrgSize07());
        ecStaticDataDaily.setUserCapitalPosSize08(ecStaticDataDaily.getUserMargin() - ecStaticDataDaily.getMrgSize08());

        ecStaticDataDaily.setMidPosSize01(Double.valueOf(formatDecimal(ecStaticDataDaily.getCcyPairTerm().equals("USD") ?
                ecStaticDataDaily.getPosSize01() / ecStaticDataDaily.getCcyPairMid() :
                ecStaticDataDaily.getPosSize01() * ecStaticDataDaily.getCcyPairMid()
                , 2)));
        ecStaticDataDaily.setMidPosSize02(Double.valueOf(formatDecimal(ecStaticDataDaily.getCcyPairTerm().equals("USD") ?
                ecStaticDataDaily.getPosSize02() / ecStaticDataDaily.getCcyPairMid() :
                ecStaticDataDaily.getPosSize02() * ecStaticDataDaily.getCcyPairMid()
                , 2)));
        ecStaticDataDaily.setMidPosSize03(Double.valueOf(formatDecimal(ecStaticDataDaily.getCcyPairTerm().equals("USD") ?
                ecStaticDataDaily.getPosSize03() / ecStaticDataDaily.getCcyPairMid() :
                ecStaticDataDaily.getPosSize03() * ecStaticDataDaily.getCcyPairMid()
                , 2)));
        ecStaticDataDaily.setMidPosSize04(Double.valueOf(formatDecimal(ecStaticDataDaily.getCcyPairTerm().equals("USD") ?
                ecStaticDataDaily.getPosSize04() / ecStaticDataDaily.getCcyPairMid() :
                ecStaticDataDaily.getPosSize04() * ecStaticDataDaily.getCcyPairMid()
                , 2)));
        ecStaticDataDaily.setMidPosSize05(Double.valueOf(formatDecimal(ecStaticDataDaily.getCcyPairTerm().equals("USD") ?
                ecStaticDataDaily.getPosSize05() / ecStaticDataDaily.getCcyPairMid() :
                ecStaticDataDaily.getPosSize05() * ecStaticDataDaily.getCcyPairMid()
                , 2)));
        ecStaticDataDaily.setMidPosSize06(Double.valueOf(formatDecimal(ecStaticDataDaily.getCcyPairTerm().equals("USD") ?
                ecStaticDataDaily.getPosSize06() / ecStaticDataDaily.getCcyPairMid() :
                ecStaticDataDaily.getPosSize06() * ecStaticDataDaily.getCcyPairMid()
                , 2)));
        ecStaticDataDaily.setMidPosSize07(Double.valueOf(formatDecimal(ecStaticDataDaily.getCcyPairTerm().equals("USD") ?
                ecStaticDataDaily.getPosSize07() / ecStaticDataDaily.getCcyPairMid() :
                ecStaticDataDaily.getPosSize07() * ecStaticDataDaily.getCcyPairMid()
                , 2)));
        ecStaticDataDaily.setMidPosSize08(Double.valueOf(formatDecimal(ecStaticDataDaily.getCcyPairTerm().equals("USD") ?
                ecStaticDataDaily.getPosSize08() / ecStaticDataDaily.getCcyPairMid() :
                ecStaticDataDaily.getPosSize08() * ecStaticDataDaily.getCcyPairMid()
                , 2)));


        ecStaticDataDaily.setBidPosSize01(Double.valueOf(formatDecimal(ecStaticDataDaily.getCcyPairTerm().equals("USD") ?
                ecStaticDataDaily.getPosSize01() / ecStaticDataDaily.getCcyPairBid() :
                ecStaticDataDaily.getPosSize01() * ecStaticDataDaily.getCcyPairBid()
                , 2)));
        ecStaticDataDaily.setBidPosSize02(Double.valueOf(formatDecimal(ecStaticDataDaily.getCcyPairTerm().equals("USD") ?
                ecStaticDataDaily.getPosSize02() / ecStaticDataDaily.getCcyPairBid() :
                ecStaticDataDaily.getPosSize02() * ecStaticDataDaily.getCcyPairBid()
                , 2)));
        ecStaticDataDaily.setBidPosSize03(Double.valueOf(formatDecimal(ecStaticDataDaily.getCcyPairTerm().equals("USD") ?
                ecStaticDataDaily.getPosSize03() / ecStaticDataDaily.getCcyPairBid() :
                ecStaticDataDaily.getPosSize03() * ecStaticDataDaily.getCcyPairBid()
                , 2)));
        ecStaticDataDaily.setBidPosSize04(Double.valueOf(formatDecimal(ecStaticDataDaily.getCcyPairTerm().equals("USD") ?
                ecStaticDataDaily.getPosSize04() / ecStaticDataDaily.getCcyPairBid() :
                ecStaticDataDaily.getPosSize04() * ecStaticDataDaily.getCcyPairBid()
                , 2)));
        ecStaticDataDaily.setBidPosSize05(Double.valueOf(formatDecimal(ecStaticDataDaily.getCcyPairTerm().equals("USD") ?
                ecStaticDataDaily.getPosSize05() / ecStaticDataDaily.getCcyPairBid() :
                ecStaticDataDaily.getPosSize05() * ecStaticDataDaily.getCcyPairBid()
                , 2)));
        ecStaticDataDaily.setBidPosSize06(Double.valueOf(formatDecimal(ecStaticDataDaily.getCcyPairTerm().equals("USD") ?
                ecStaticDataDaily.getPosSize06() / ecStaticDataDaily.getCcyPairBid() :
                ecStaticDataDaily.getPosSize06() * ecStaticDataDaily.getCcyPairBid()
                , 2)));
        ecStaticDataDaily.setBidPosSize07(Double.valueOf(formatDecimal(ecStaticDataDaily.getCcyPairTerm().equals("USD") ?
                ecStaticDataDaily.getPosSize07() / ecStaticDataDaily.getCcyPairBid() :
                ecStaticDataDaily.getPosSize07() * ecStaticDataDaily.getCcyPairBid()
                , 2)));
        ecStaticDataDaily.setBidPosSize08(Double.valueOf(formatDecimal(ecStaticDataDaily.getCcyPairTerm().equals("USD") ?
                ecStaticDataDaily.getPosSize08() / ecStaticDataDaily.getCcyPairBid() :
                ecStaticDataDaily.getPosSize08() * ecStaticDataDaily.getCcyPairBid()
                , 2)));


        ecStaticDataDaily.setAskPosSize01(Double.valueOf(formatDecimal(ecStaticDataDaily.getCcyPairTerm().equals("USD") ?
                ecStaticDataDaily.getPosSize01() / ecStaticDataDaily.getCcyPairAsk() :
                ecStaticDataDaily.getPosSize01() * ecStaticDataDaily.getCcyPairAsk()
        , 2)));
        ecStaticDataDaily.setAskPosSize02(Double.valueOf(formatDecimal(ecStaticDataDaily.getCcyPairTerm().equals("USD") ?
                ecStaticDataDaily.getPosSize02() / ecStaticDataDaily.getCcyPairAsk() :
                ecStaticDataDaily.getPosSize02() * ecStaticDataDaily.getCcyPairAsk()
        ,2)));
        ecStaticDataDaily.setAskPosSize03(Double.valueOf(formatDecimal(ecStaticDataDaily.getCcyPairTerm().equals("USD") ?
                ecStaticDataDaily.getPosSize03() / ecStaticDataDaily.getCcyPairAsk() :
                ecStaticDataDaily.getPosSize03() * ecStaticDataDaily.getCcyPairAsk()
        ,2)));
        ecStaticDataDaily.setAskPosSize04(Double.valueOf(formatDecimal(ecStaticDataDaily.getCcyPairTerm().equals("USD") ?
                ecStaticDataDaily.getPosSize04() / ecStaticDataDaily.getCcyPairAsk() :
                ecStaticDataDaily.getPosSize04() * ecStaticDataDaily.getCcyPairAsk()
        ,2)));
        ecStaticDataDaily.setAskPosSize05(Double.valueOf(formatDecimal(ecStaticDataDaily.getCcyPairTerm().equals("USD") ?
                ecStaticDataDaily.getPosSize05() / ecStaticDataDaily.getCcyPairAsk() :
                ecStaticDataDaily.getPosSize05() * ecStaticDataDaily.getCcyPairAsk()
        ,2 )));
        ecStaticDataDaily.setAskPosSize06(Double.valueOf(formatDecimal(ecStaticDataDaily.getCcyPairTerm().equals("USD") ?
                ecStaticDataDaily.getPosSize06() / ecStaticDataDaily.getCcyPairAsk() :
                ecStaticDataDaily.getPosSize06() * ecStaticDataDaily.getCcyPairAsk()
        ,2)));
        ecStaticDataDaily.setAskPosSize07(Double.valueOf(formatDecimal(ecStaticDataDaily.getCcyPairTerm().equals("USD") ?
                ecStaticDataDaily.getPosSize07() / ecStaticDataDaily.getCcyPairAsk() :
                ecStaticDataDaily.getPosSize07() * ecStaticDataDaily.getCcyPairAsk()
        ,2)));
        ecStaticDataDaily.setAskPosSize08(Double.valueOf(formatDecimal(ecStaticDataDaily.getCcyPairTerm().equals("USD") ?
                ecStaticDataDaily.getPosSize08() / ecStaticDataDaily.getCcyPairAsk() :
                ecStaticDataDaily.getPosSize08() * ecStaticDataDaily.getCcyPairAsk()
        ,2)));


        ecStaticDataDaily.setPnlBase01Bullish(Double.valueOf(formatDecimal(ecStaticDataDaily.getCcyPairTerm().equals("USD") ?
                ecStaticDataDaily.getAskPosSize01() - ecStaticDataDaily.getMidPosSize01() :
                (ecStaticDataDaily.getAskPosSize01() - ecStaticDataDaily.getMidPosSize01()) / ecStaticDataDaily.getCcyPairMid()
                , 2)));

        ecStaticDataDaily.setPnlBase02Bullish(Double.valueOf(formatDecimal(ecStaticDataDaily.getCcyPairTerm().equals("USD") ?
                ecStaticDataDaily.getAskPosSize02() - ecStaticDataDaily.getMidPosSize02() :
                (ecStaticDataDaily.getAskPosSize02() - ecStaticDataDaily.getMidPosSize02()) / ecStaticDataDaily.getCcyPairMid()
                , 2)));

        ecStaticDataDaily.setPnlBase03Bullish(Double.valueOf(formatDecimal(ecStaticDataDaily.getCcyPairTerm().equals("USD") ?
                ecStaticDataDaily.getAskPosSize03() - ecStaticDataDaily.getMidPosSize03() :
                (ecStaticDataDaily.getAskPosSize03() - ecStaticDataDaily.getMidPosSize03()) / ecStaticDataDaily.getCcyPairMid()
                , 2)));

        ecStaticDataDaily.setPnlBase04Bullish(Double.valueOf(formatDecimal(ecStaticDataDaily.getCcyPairTerm().equals("USD") ?
                ecStaticDataDaily.getAskPosSize04() - ecStaticDataDaily.getMidPosSize04() :
                (ecStaticDataDaily.getAskPosSize04() - ecStaticDataDaily.getMidPosSize04()) / ecStaticDataDaily.getCcyPairMid()
                , 2)));

        ecStaticDataDaily.setPnlBase05Bullish(Double.valueOf(formatDecimal(ecStaticDataDaily.getCcyPairTerm().equals("USD") ?
                ecStaticDataDaily.getAskPosSize05() - ecStaticDataDaily.getMidPosSize05() :
                (ecStaticDataDaily.getAskPosSize05() - ecStaticDataDaily.getMidPosSize05()) / ecStaticDataDaily.getCcyPairMid()
                , 2)));

        ecStaticDataDaily.setPnlBase06Bullish(Double.valueOf(formatDecimal(ecStaticDataDaily.getCcyPairTerm().equals("USD") ?
                ecStaticDataDaily.getAskPosSize06() - ecStaticDataDaily.getMidPosSize06() :
                (ecStaticDataDaily.getAskPosSize06() - ecStaticDataDaily.getMidPosSize06()) / ecStaticDataDaily.getCcyPairMid()
                , 2)));

        ecStaticDataDaily.setPnlBase07Bullish(Double.valueOf(formatDecimal(ecStaticDataDaily.getCcyPairTerm().equals("USD") ?
                ecStaticDataDaily.getAskPosSize07() - ecStaticDataDaily.getMidPosSize07() :
                (ecStaticDataDaily.getAskPosSize07() - ecStaticDataDaily.getMidPosSize07()) / ecStaticDataDaily.getCcyPairMid()
                , 2)));

        ecStaticDataDaily.setPnlBase08Bullish(Double.valueOf(formatDecimal(ecStaticDataDaily.getCcyPairTerm().equals("USD") ?
                ecStaticDataDaily.getAskPosSize08() - ecStaticDataDaily.getMidPosSize08() :
                (ecStaticDataDaily.getAskPosSize08() - ecStaticDataDaily.getMidPosSize08()) / ecStaticDataDaily.getCcyPairMid()
                , 2)));

        ecStaticDataDaily.setPnlTerm01Bullish(Double.valueOf(formatDecimal(ecStaticDataDaily.getCcyPairTerm().equals("USD") ?
                ecStaticDataDaily.getPnlBase01Bullish() * ecStaticDataDaily.getCcyPairMid() :
                ecStaticDataDaily.getPnlBase01Bullish() / ecStaticDataDaily.getCcyPairMid()
                , 2)));

        ecStaticDataDaily.setPnlTerm02Bullish(Double.valueOf(formatDecimal(ecStaticDataDaily.getCcyPairTerm().equals("USD") ?
                ecStaticDataDaily.getPnlBase02Bullish() * ecStaticDataDaily.getCcyPairMid() :
                ecStaticDataDaily.getPnlBase02Bullish() / ecStaticDataDaily.getCcyPairMid()
                , 2 )));

        ecStaticDataDaily.setPnlTerm03Bullish(Double.valueOf(formatDecimal(ecStaticDataDaily.getCcyPairTerm().equals("USD") ?
                ecStaticDataDaily.getPnlBase03Bullish() * ecStaticDataDaily.getCcyPairMid() :
                ecStaticDataDaily.getPnlBase03Bullish() / ecStaticDataDaily.getCcyPairMid()
                , 2)));

        ecStaticDataDaily.setPnlTerm04Bullish(Double.valueOf(formatDecimal(ecStaticDataDaily.getCcyPairTerm().equals("USD") ?
                ecStaticDataDaily.getPnlBase04Bullish() * ecStaticDataDaily.getCcyPairMid() :
                ecStaticDataDaily.getPnlBase04Bullish() / ecStaticDataDaily.getCcyPairMid()
                , 2)));

        ecStaticDataDaily.setPnlTerm05Bullish(Double.valueOf(formatDecimal(ecStaticDataDaily.getCcyPairTerm().equals("USD") ?
                ecStaticDataDaily.getPnlBase05Bullish() * ecStaticDataDaily.getCcyPairMid() :
                ecStaticDataDaily.getPnlBase05Bullish() / ecStaticDataDaily.getCcyPairMid()
                , 2)));

        ecStaticDataDaily.setPnlTerm06Bullish(Double.valueOf(formatDecimal(ecStaticDataDaily.getCcyPairTerm().equals("USD") ?
                ecStaticDataDaily.getPnlBase06Bullish() * ecStaticDataDaily.getCcyPairMid() :
                ecStaticDataDaily.getPnlBase06Bullish() / ecStaticDataDaily.getCcyPairMid()
                , 2)));

        ecStaticDataDaily.setPnlTerm07Bullish(Double.valueOf(formatDecimal(ecStaticDataDaily.getCcyPairTerm().equals("USD") ?
                ecStaticDataDaily.getPnlBase07Bullish() * ecStaticDataDaily.getCcyPairMid() :
                ecStaticDataDaily.getPnlBase07Bullish() / ecStaticDataDaily.getCcyPairMid()
                , 2 )));

        ecStaticDataDaily.setPnlTerm08Bullish(Double.valueOf(formatDecimal(ecStaticDataDaily.getCcyPairTerm().equals("USD") ?
                ecStaticDataDaily.getPnlBase08Bullish() * ecStaticDataDaily.getCcyPairMid() :
                ecStaticDataDaily.getPnlBase08Bullish() / ecStaticDataDaily.getCcyPairMid()
                , 2)));


        ecStaticDataDaily.setPnlBase01Bearish(Double.valueOf(formatDecimal(ecStaticDataDaily.getCcyPairTerm().equals("USD") ?
                ecStaticDataDaily.getMidPosSize01() - ecStaticDataDaily.getAskPosSize01() :
                (ecStaticDataDaily.getMidPosSize01() - ecStaticDataDaily.getAskPosSize01()) / ecStaticDataDaily.getCcyPairMid()
                , 2)));

        ecStaticDataDaily.setPnlBase02Bearish(Double.valueOf(formatDecimal(ecStaticDataDaily.getCcyPairTerm().equals("USD") ?
                ecStaticDataDaily.getMidPosSize02() - ecStaticDataDaily.getAskPosSize02() :
                (ecStaticDataDaily.getMidPosSize02() - ecStaticDataDaily.getAskPosSize02()) / ecStaticDataDaily.getCcyPairMid()
                , 2)));

        ecStaticDataDaily.setPnlBase03Bearish(Double.valueOf(formatDecimal(ecStaticDataDaily.getCcyPairTerm().equals("USD") ?
                ecStaticDataDaily.getMidPosSize03() - ecStaticDataDaily.getAskPosSize03() :
                (ecStaticDataDaily.getMidPosSize03() - ecStaticDataDaily.getAskPosSize03()) / ecStaticDataDaily.getCcyPairMid()
                , 2)));

        ecStaticDataDaily.setPnlBase04Bearish(Double.valueOf(formatDecimal(ecStaticDataDaily.getCcyPairTerm().equals("USD") ?
                ecStaticDataDaily.getMidPosSize04() - ecStaticDataDaily.getAskPosSize04() :
                (ecStaticDataDaily.getMidPosSize04() - ecStaticDataDaily.getAskPosSize04()) / ecStaticDataDaily.getCcyPairMid()
                , 2)));

        ecStaticDataDaily.setPnlBase05Bearish(Double.valueOf(formatDecimal(ecStaticDataDaily.getCcyPairTerm().equals("USD") ?
                ecStaticDataDaily.getMidPosSize05() - ecStaticDataDaily.getAskPosSize05() :
                (ecStaticDataDaily.getMidPosSize05() - ecStaticDataDaily.getAskPosSize05()) / ecStaticDataDaily.getCcyPairMid()
                , 2)));

        ecStaticDataDaily.setPnlBase06Bearish(Double.valueOf(formatDecimal(ecStaticDataDaily.getCcyPairTerm().equals("USD") ?
                ecStaticDataDaily.getMidPosSize06() - ecStaticDataDaily.getAskPosSize06() :
                (ecStaticDataDaily.getMidPosSize06() - ecStaticDataDaily.getAskPosSize06()) / ecStaticDataDaily.getCcyPairMid()
                , 2)));

        ecStaticDataDaily.setPnlBase07Bearish(Double.valueOf(formatDecimal(ecStaticDataDaily.getCcyPairTerm().equals("USD") ?
                ecStaticDataDaily.getMidPosSize07() - ecStaticDataDaily.getAskPosSize07() :
                (ecStaticDataDaily.getMidPosSize07() - ecStaticDataDaily.getAskPosSize07()) / ecStaticDataDaily.getCcyPairMid()
                , 2)));

        ecStaticDataDaily.setPnlBase08Bearish(Double.valueOf(formatDecimal(ecStaticDataDaily.getCcyPairTerm().equals("USD") ?
                ecStaticDataDaily.getMidPosSize08() - ecStaticDataDaily.getAskPosSize08() :
                (ecStaticDataDaily.getMidPosSize08() - ecStaticDataDaily.getAskPosSize08()) / ecStaticDataDaily.getCcyPairMid()
                , 2)));

        ecStaticDataDaily.setPnlTerm01Bearish(Double.valueOf(formatDecimal(ecStaticDataDaily.getCcyPairTerm().equals("USD") ?
                ecStaticDataDaily.getPnlBase01Bearish() * ecStaticDataDaily.getCcyPairMid() :
                ecStaticDataDaily.getPnlBase01Bearish() / ecStaticDataDaily.getCcyPairMid()
                , 2)));

        ecStaticDataDaily.setPnlTerm02Bearish(Double.valueOf(formatDecimal(ecStaticDataDaily.getCcyPairTerm().equals("USD") ?
                ecStaticDataDaily.getPnlBase02Bearish() * ecStaticDataDaily.getCcyPairMid() :
                ecStaticDataDaily.getPnlBase02Bearish() / ecStaticDataDaily.getCcyPairMid()
                , 2)));

        ecStaticDataDaily.setPnlTerm03Bearish(Double.valueOf(formatDecimal(ecStaticDataDaily.getCcyPairTerm().equals("USD") ?
                ecStaticDataDaily.getPnlBase03Bearish() * ecStaticDataDaily.getCcyPairMid() :
                ecStaticDataDaily.getPnlBase03Bearish() / ecStaticDataDaily.getCcyPairMid()
                , 2)));

        ecStaticDataDaily.setPnlTerm04Bearish(Double.valueOf(formatDecimal(ecStaticDataDaily.getCcyPairTerm().equals("USD") ?
                ecStaticDataDaily.getPnlBase04Bearish() * ecStaticDataDaily.getCcyPairMid() :
                ecStaticDataDaily.getPnlBase04Bearish() / ecStaticDataDaily.getCcyPairMid()
                , 2)));

        ecStaticDataDaily.setPnlTerm05Bearish(Double.valueOf(formatDecimal(ecStaticDataDaily.getCcyPairTerm().equals("USD") ?
                ecStaticDataDaily.getPnlBase05Bearish() * ecStaticDataDaily.getCcyPairMid() :
                ecStaticDataDaily.getPnlBase05Bearish() / ecStaticDataDaily.getCcyPairMid()
                , 2)));

        ecStaticDataDaily.setPnlTerm06Bearish(Double.valueOf(formatDecimal(ecStaticDataDaily.getCcyPairTerm().equals("USD") ?
                ecStaticDataDaily.getPnlBase06Bearish() * ecStaticDataDaily.getCcyPairMid() :
                ecStaticDataDaily.getPnlBase06Bearish() / ecStaticDataDaily.getCcyPairMid()
                , 2 )));

        ecStaticDataDaily.setPnlTerm07Bearish(Double.valueOf(formatDecimal(ecStaticDataDaily.getCcyPairTerm().equals("USD") ?
                ecStaticDataDaily.getPnlBase07Bearish() * ecStaticDataDaily.getCcyPairMid() :
                ecStaticDataDaily.getPnlBase07Bearish() / ecStaticDataDaily.getCcyPairMid()
                , 2 )));

        ecStaticDataDaily.setPnlTerm08Bearish(Double.valueOf(formatDecimal(ecStaticDataDaily.getCcyPairTerm().equals("USD") ?
                ecStaticDataDaily.getPnlBase08Bearish() * ecStaticDataDaily.getCcyPairMid() :
                ecStaticDataDaily.getPnlBase08Bearish() / ecStaticDataDaily.getCcyPairMid()
                , 2 )));

        ecStaticDataDaily.setSlSizeVariable01(ecStaticData.getSlSizeVariable01());
        ecStaticDataDaily.setSlSizeVariable02(ecStaticData.getSlSizeVariable02());
        ecStaticDataDaily.setSlSizeVariable03(ecStaticData.getSlSizeVariable03());
        ecStaticDataDaily.setSlSizeVariable04(ecStaticData.getSlSizeVariable04());
        ecStaticDataDaily.setSlSizeVariable05(ecStaticData.getSlSizeVariable05());
        ecStaticDataDaily.setSlSizeVariable06(ecStaticData.getSlSizeVariable06());
        ecStaticDataDaily.setSlSizeVariable07(ecStaticData.getSlSizeVariable07());
        ecStaticDataDaily.setSlSizeVariable08(ecStaticData.getSlSizeVariable08());
        ecStaticDataDaily.setSlSizeVariable09(ecStaticData.getSlSizeVariable09());
        ecStaticDataDaily.setSlSizeVariable10(ecStaticData.getSlSizeVariable10());
        ecStaticDataDaily.setSlSizeVariable11(ecStaticData.getSlSizeVariable11());
        ecStaticDataDaily.setSlSizeVariable12(ecStaticData.getSlSizeVariable12());

        ecStaticDataDaily.setTpSizeVariable01(ecStaticData.getTpSizeVariable01());
        ecStaticDataDaily.setTpSizeVariable02(ecStaticData.getTpSizeVariable02());
        ecStaticDataDaily.setTpSizeVariable03(ecStaticData.getTpSizeVariable03());
        ecStaticDataDaily.setTpSizeVariable04(ecStaticData.getTpSizeVariable04());
        ecStaticDataDaily.setTpSizeVariable05(ecStaticData.getTpSizeVariable05());
        ecStaticDataDaily.setTpSizeVariable06(ecStaticData.getTpSizeVariable06());
        ecStaticDataDaily.setTpSizeVariable07(ecStaticData.getTpSizeVariable07());
        ecStaticDataDaily.setTpSizeVariable08(ecStaticData.getTpSizeVariable08());
        ecStaticDataDaily.setTpSizeVariable09(ecStaticData.getTpSizeVariable09());
        ecStaticDataDaily.setTpSizeVariable10(ecStaticData.getTpSizeVariable10());
        ecStaticDataDaily.setTpSizeVariable11(ecStaticData.getTpSizeVariable11());
        ecStaticDataDaily.setTpSizeVariable12(ecStaticData.getTpSizeVariable12());


        ecStaticDataDaily.setSlSizeVariable01Bullish(ecStaticDataDaily.getCcyPairTerm().equals("JPY") ?
                ecStaticDataDaily.getCcyPairMid() - (Double.valueOf(ecStaticDataDaily.getSlSizeVariable01()) / 100) :
                ecStaticDataDaily.getCcyPairMid() - (Double.valueOf(ecStaticDataDaily.getSlSizeVariable01()) / 10000)
        );
        ecStaticDataDaily.setSlSizeVariable02Bullish(ecStaticDataDaily.getCcyPairTerm().equals("JPY") ?
                ecStaticDataDaily.getCcyPairMid() - (Double.valueOf(ecStaticDataDaily.getSlSizeVariable02()) / 100) :
                ecStaticDataDaily.getCcyPairMid() - (Double.valueOf(ecStaticDataDaily.getSlSizeVariable02()) / 10000)
        );

//        here
        ecStaticDataDaily.setSlSizeVariable03Bullish(ecStaticDataDaily.getCcyPairTerm().equals("JPY") ?
                ecStaticDataDaily.getCcyPairMid() - (Double.valueOf(ecStaticDataDaily.getSlSizeVariable03()) / 100) :
                ecStaticDataDaily.getCcyPairMid() - (Double.valueOf(ecStaticDataDaily.getSlSizeVariable03()) / 10000)
        );
        ecStaticDataDaily.setSlSizeVariable04Bullish(ecStaticDataDaily.getCcyPairTerm().equals("JPY") ?
                ecStaticDataDaily.getCcyPairMid() - (Double.valueOf(ecStaticDataDaily.getSlSizeVariable04()) / 100) :
                ecStaticDataDaily.getCcyPairMid() - (Double.valueOf(ecStaticDataDaily.getSlSizeVariable04()) / 10000)
        );
        ecStaticDataDaily.setSlSizeVariable05Bullish(ecStaticDataDaily.getCcyPairTerm().equals("JPY") ?
                ecStaticDataDaily.getCcyPairMid() - (Double.valueOf(ecStaticDataDaily.getSlSizeVariable05()) / 100) :
                ecStaticDataDaily.getCcyPairMid() - (Double.valueOf(ecStaticDataDaily.getSlSizeVariable05()) / 10000)
        );
        ecStaticDataDaily.setSlSizeVariable06Bullish(ecStaticDataDaily.getCcyPairTerm().equals("JPY") ?
                ecStaticDataDaily.getCcyPairMid() - (Double.valueOf(ecStaticDataDaily.getSlSizeVariable06()) / 100) :
                ecStaticDataDaily.getCcyPairMid() - (Double.valueOf(ecStaticDataDaily.getSlSizeVariable06()) / 10000)
        );
        ecStaticDataDaily.setSlSizeVariable07Bullish(ecStaticDataDaily.getCcyPairTerm().equals("JPY") ?
                ecStaticDataDaily.getCcyPairMid() - (Double.valueOf(ecStaticDataDaily.getSlSizeVariable07()) / 100) :
                ecStaticDataDaily.getCcyPairMid() - (Double.valueOf(ecStaticDataDaily.getSlSizeVariable07()) / 10000)
        );
        ecStaticDataDaily.setSlSizeVariable08Bullish(ecStaticDataDaily.getCcyPairTerm().equals("JPY") ?
                ecStaticDataDaily.getCcyPairMid() - (Double.valueOf(ecStaticDataDaily.getSlSizeVariable08()) / 100) :
                ecStaticDataDaily.getCcyPairMid() - (Double.valueOf(ecStaticDataDaily.getSlSizeVariable08()) / 10000)
        );
        ecStaticDataDaily.setSlSizeVariable09Bullish(ecStaticDataDaily.getCcyPairTerm().equals("JPY") ?
                ecStaticDataDaily.getCcyPairMid() - (Double.valueOf(ecStaticDataDaily.getSlSizeVariable09()) / 100) :
                ecStaticDataDaily.getCcyPairMid() - (Double.valueOf(ecStaticDataDaily.getSlSizeVariable09()) / 10000)
        );
        ecStaticDataDaily.setSlSizeVariable10Bullish(ecStaticDataDaily.getCcyPairTerm().equals("JPY") ?
                ecStaticDataDaily.getCcyPairMid() - (Double.valueOf(ecStaticDataDaily.getSlSizeVariable10()) / 100) :
                ecStaticDataDaily.getCcyPairMid() - (Double.valueOf(ecStaticDataDaily.getSlSizeVariable10()) / 10000)
        );
        ecStaticDataDaily.setSlSizeVariable11Bullish(ecStaticDataDaily.getCcyPairTerm().equals("JPY") ?
                ecStaticDataDaily.getCcyPairMid() - (Double.valueOf(ecStaticDataDaily.getSlSizeVariable11()) / 100) :
                ecStaticDataDaily.getCcyPairMid() - (Double.valueOf(ecStaticDataDaily.getSlSizeVariable11()) / 10000)
        );
        ecStaticDataDaily.setSlSizeVariable12Bullish(ecStaticDataDaily.getCcyPairTerm().equals("JPY") ?
                ecStaticDataDaily.getCcyPairMid() - (Double.valueOf(ecStaticDataDaily.getSlSizeVariable12()) / 100) :
                ecStaticDataDaily.getCcyPairMid() - (Double.valueOf(ecStaticDataDaily.getSlSizeVariable12()) / 10000)
        );


        ecStaticDataDaily.setSlSizeVariable01Bearish(ecStaticDataDaily.getCcyPairTerm().equals("JPY") ?
                ecStaticDataDaily.getCcyPairMid() + (Double.valueOf(ecStaticDataDaily.getSlSizeVariable01()) / 100) :
                ecStaticDataDaily.getCcyPairMid() + (Double.valueOf(ecStaticDataDaily.getSlSizeVariable01()) / 10000)
        );
        ecStaticDataDaily.setSlSizeVariable02Bearish(ecStaticDataDaily.getCcyPairTerm().equals("JPY") ?
                ecStaticDataDaily.getCcyPairMid() + (Double.valueOf(ecStaticDataDaily.getSlSizeVariable02()) / 100) :
                ecStaticDataDaily.getCcyPairMid() + (Double.valueOf(ecStaticDataDaily.getSlSizeVariable02()) / 10000)
        );
        ecStaticDataDaily.setSlSizeVariable03Bearish(ecStaticDataDaily.getCcyPairTerm().equals("JPY") ?
                ecStaticDataDaily.getCcyPairMid() + (Double.valueOf(ecStaticDataDaily.getSlSizeVariable03()) / 100) :
                ecStaticDataDaily.getCcyPairMid() + (Double.valueOf(ecStaticDataDaily.getSlSizeVariable03()) / 10000)
        );
        ecStaticDataDaily.setSlSizeVariable04Bearish(ecStaticDataDaily.getCcyPairTerm().equals("JPY") ?
                ecStaticDataDaily.getCcyPairMid() + (Double.valueOf(ecStaticDataDaily.getSlSizeVariable04()) / 100) :
                ecStaticDataDaily.getCcyPairMid() + (Double.valueOf(ecStaticDataDaily.getSlSizeVariable04()) / 10000)
        );
        ecStaticDataDaily.setSlSizeVariable05Bearish(ecStaticDataDaily.getCcyPairTerm().equals("JPY") ?
                ecStaticDataDaily.getCcyPairMid() + (Double.valueOf(ecStaticDataDaily.getSlSizeVariable05()) / 100) :
                ecStaticDataDaily.getCcyPairMid() + (Double.valueOf(ecStaticDataDaily.getSlSizeVariable05()) / 10000)
        );
        ecStaticDataDaily.setSlSizeVariable06Bearish(ecStaticDataDaily.getCcyPairTerm().equals("JPY") ?
                ecStaticDataDaily.getCcyPairMid() + (Double.valueOf(ecStaticDataDaily.getSlSizeVariable06()) / 100) :
                ecStaticDataDaily.getCcyPairMid() + (Double.valueOf(ecStaticDataDaily.getSlSizeVariable06()) / 10000)
        );
        ecStaticDataDaily.setSlSizeVariable07Bearish(ecStaticDataDaily.getCcyPairTerm().equals("JPY") ?
                ecStaticDataDaily.getCcyPairMid() + (Double.valueOf(ecStaticDataDaily.getSlSizeVariable07()) / 100) :
                ecStaticDataDaily.getCcyPairMid() + (Double.valueOf(ecStaticDataDaily.getSlSizeVariable07()) / 10000)
        );
        ecStaticDataDaily.setSlSizeVariable08Bearish(ecStaticDataDaily.getCcyPairTerm().equals("JPY") ?
                ecStaticDataDaily.getCcyPairMid() + (Double.valueOf(ecStaticDataDaily.getSlSizeVariable08()) / 100) :
                ecStaticDataDaily.getCcyPairMid() + (Double.valueOf(ecStaticDataDaily.getSlSizeVariable08()) / 10000)
        );
        ecStaticDataDaily.setSlSizeVariable09Bearish(ecStaticDataDaily.getCcyPairTerm().equals("JPY") ?
                ecStaticDataDaily.getCcyPairMid() + (Double.valueOf(ecStaticDataDaily.getSlSizeVariable09()) / 100) :
                ecStaticDataDaily.getCcyPairMid() + (Double.valueOf(ecStaticDataDaily.getSlSizeVariable09()) / 10000)
        );
        ecStaticDataDaily.setSlSizeVariable10Bearish(ecStaticDataDaily.getCcyPairTerm().equals("JPY") ?
                ecStaticDataDaily.getCcyPairMid() + (Double.valueOf(ecStaticDataDaily.getSlSizeVariable10()) / 100) :
                ecStaticDataDaily.getCcyPairMid() + (Double.valueOf(ecStaticDataDaily.getSlSizeVariable10()) / 10000)
        );
        ecStaticDataDaily.setSlSizeVariable11Bearish(ecStaticDataDaily.getCcyPairTerm().equals("JPY") ?
                ecStaticDataDaily.getCcyPairMid() + (Double.valueOf(ecStaticDataDaily.getSlSizeVariable11()) / 100) :
                ecStaticDataDaily.getCcyPairMid() + (Double.valueOf(ecStaticDataDaily.getSlSizeVariable11()) / 10000)
        );
        ecStaticDataDaily.setSlSizeVariable12Bearish(ecStaticDataDaily.getCcyPairTerm().equals("JPY") ?
                ecStaticDataDaily.getCcyPairMid() + (Double.valueOf(ecStaticDataDaily.getSlSizeVariable12()) / 100) :
                ecStaticDataDaily.getCcyPairMid() + (Double.valueOf(ecStaticDataDaily.getSlSizeVariable12()) / 10000)
        );

        ecStaticDataDaily.setTpSizeVariable01Bullish(ecStaticDataDaily.getCcyPairTerm().equals("JPY") ?
                ecStaticDataDaily.getCcyPairMid() + (Double.valueOf(ecStaticDataDaily.getTpSizeVariable01()) / 100) :
                ecStaticDataDaily.getCcyPairMid() + (Double.valueOf(ecStaticDataDaily.getTpSizeVariable01()) / 10000)
        );
        ecStaticDataDaily.setTpSizeVariable02Bullish(ecStaticDataDaily.getCcyPairTerm().equals("JPY") ?
                ecStaticDataDaily.getCcyPairMid() + (Double.valueOf(ecStaticDataDaily.getTpSizeVariable02()) / 100) :
                ecStaticDataDaily.getCcyPairMid() + (Double.valueOf(ecStaticDataDaily.getTpSizeVariable02()) / 10000)
        );
        ecStaticDataDaily.setTpSizeVariable03Bullish(ecStaticDataDaily.getCcyPairTerm().equals("JPY") ?
                ecStaticDataDaily.getCcyPairMid() + (Double.valueOf(ecStaticDataDaily.getTpSizeVariable03()) / 100) :
                ecStaticDataDaily.getCcyPairMid() + (Double.valueOf(ecStaticDataDaily.getTpSizeVariable03()) / 10000)
        );
        ecStaticDataDaily.setTpSizeVariable04Bullish(ecStaticDataDaily.getCcyPairTerm().equals("JPY") ?
                ecStaticDataDaily.getCcyPairMid() + (Double.valueOf(ecStaticDataDaily.getTpSizeVariable04()) / 100) :
                ecStaticDataDaily.getCcyPairMid() + (Double.valueOf(ecStaticDataDaily.getTpSizeVariable04()) / 10000)
        );
        ecStaticDataDaily.setTpSizeVariable05Bullish(ecStaticDataDaily.getCcyPairTerm().equals("JPY") ?
                ecStaticDataDaily.getCcyPairMid() + (Double.valueOf(ecStaticDataDaily.getTpSizeVariable05()) / 100) :
                ecStaticDataDaily.getCcyPairMid() + (Double.valueOf(ecStaticDataDaily.getTpSizeVariable05()) / 10000)
        );
        ecStaticDataDaily.setTpSizeVariable06Bullish(ecStaticDataDaily.getCcyPairTerm().equals("JPY") ?
                ecStaticDataDaily.getCcyPairMid() + (Double.valueOf(ecStaticDataDaily.getTpSizeVariable06()) / 100) :
                ecStaticDataDaily.getCcyPairMid() + (Double.valueOf(ecStaticDataDaily.getTpSizeVariable06()) / 10000)
        );
        ecStaticDataDaily.setTpSizeVariable07Bullish(ecStaticDataDaily.getCcyPairTerm().equals("JPY") ?
                ecStaticDataDaily.getCcyPairMid() + (Double.valueOf(ecStaticDataDaily.getTpSizeVariable07()) / 100) :
                ecStaticDataDaily.getCcyPairMid() + (Double.valueOf(ecStaticDataDaily.getTpSizeVariable07()) / 10000)
        );
        ecStaticDataDaily.setTpSizeVariable08Bullish(ecStaticDataDaily.getCcyPairTerm().equals("JPY") ?
                ecStaticDataDaily.getCcyPairMid() + (Double.valueOf(ecStaticDataDaily.getTpSizeVariable08()) / 100) :
                ecStaticDataDaily.getCcyPairMid() + (Double.valueOf(ecStaticDataDaily.getTpSizeVariable08()) / 10000)
        );
        ecStaticDataDaily.setTpSizeVariable09Bullish(ecStaticDataDaily.getCcyPairTerm().equals("JPY") ?
                ecStaticDataDaily.getCcyPairMid() + (Double.valueOf(ecStaticDataDaily.getTpSizeVariable09()) / 100) :
                ecStaticDataDaily.getCcyPairMid() + (Double.valueOf(ecStaticDataDaily.getTpSizeVariable09()) / 10000)
        );
        ecStaticDataDaily.setTpSizeVariable10Bullish(ecStaticDataDaily.getCcyPairTerm().equals("JPY") ?
                ecStaticDataDaily.getCcyPairMid() + (Double.valueOf(ecStaticDataDaily.getTpSizeVariable10()) / 100) :
                ecStaticDataDaily.getCcyPairMid() + (Double.valueOf(ecStaticDataDaily.getTpSizeVariable10()) / 10000)
        );
        ecStaticDataDaily.setTpSizeVariable11Bullish(ecStaticDataDaily.getCcyPairTerm().equals("JPY") ?
                ecStaticDataDaily.getCcyPairMid() + (Double.valueOf(ecStaticDataDaily.getTpSizeVariable11()) / 100) :
                ecStaticDataDaily.getCcyPairMid() + (Double.valueOf(ecStaticDataDaily.getTpSizeVariable11()) / 10000)
        );
        ecStaticDataDaily.setTpSizeVariable12Bullish(ecStaticDataDaily.getCcyPairTerm().equals("JPY") ?
                ecStaticDataDaily.getCcyPairMid() + (Double.valueOf(ecStaticDataDaily.getTpSizeVariable12()) / 100) :
                ecStaticDataDaily.getCcyPairMid() + (Double.valueOf(ecStaticDataDaily.getTpSizeVariable12()) / 10000)
        );


        ecStaticDataDaily.setTpSizeVariable01Bearish(ecStaticDataDaily.getCcyPairTerm().equals("JPY") ?
                ecStaticDataDaily.getCcyPairMid() - (Double.valueOf(ecStaticDataDaily.getTpSizeVariable01()) / 100) :
                ecStaticDataDaily.getCcyPairMid() - (Double.valueOf(ecStaticDataDaily.getTpSizeVariable01()) / 10000)
        );
        ecStaticDataDaily.setTpSizeVariable02Bearish(ecStaticDataDaily.getCcyPairTerm().equals("JPY") ?
                ecStaticDataDaily.getCcyPairMid() - (Double.valueOf(ecStaticDataDaily.getTpSizeVariable02()) / 100) :
                ecStaticDataDaily.getCcyPairMid() - (Double.valueOf(ecStaticDataDaily.getTpSizeVariable02()) / 10000)
        );
        ecStaticDataDaily.setTpSizeVariable03Bearish(ecStaticDataDaily.getCcyPairTerm().equals("JPY") ?
                ecStaticDataDaily.getCcyPairMid() - (Double.valueOf(ecStaticDataDaily.getTpSizeVariable03()) / 100) :
                ecStaticDataDaily.getCcyPairMid() - (Double.valueOf(ecStaticDataDaily.getTpSizeVariable03()) / 10000)
        );
        ecStaticDataDaily.setTpSizeVariable04Bearish(ecStaticDataDaily.getCcyPairTerm().equals("JPY") ?
                ecStaticDataDaily.getCcyPairMid() - (Double.valueOf(ecStaticDataDaily.getTpSizeVariable04()) / 100) :
                ecStaticDataDaily.getCcyPairMid() - (Double.valueOf(ecStaticDataDaily.getTpSizeVariable04()) / 10000)
        );
        ecStaticDataDaily.setTpSizeVariable05Bearish(ecStaticDataDaily.getCcyPairTerm().equals("JPY") ?
                ecStaticDataDaily.getCcyPairMid() - (Double.valueOf(ecStaticDataDaily.getTpSizeVariable05()) / 100) :
                ecStaticDataDaily.getCcyPairMid() - (Double.valueOf(ecStaticDataDaily.getTpSizeVariable05()) / 10000)
        );
        ecStaticDataDaily.setTpSizeVariable06Bearish(ecStaticDataDaily.getCcyPairTerm().equals("JPY") ?
                ecStaticDataDaily.getCcyPairMid() - (Double.valueOf(ecStaticDataDaily.getTpSizeVariable06()) / 100) :
                ecStaticDataDaily.getCcyPairMid() - (Double.valueOf(ecStaticDataDaily.getTpSizeVariable06()) / 10000)
        );
        ecStaticDataDaily.setTpSizeVariable07Bearish(ecStaticDataDaily.getCcyPairTerm().equals("JPY") ?
                ecStaticDataDaily.getCcyPairMid() - (Double.valueOf(ecStaticDataDaily.getTpSizeVariable07()) / 100) :
                ecStaticDataDaily.getCcyPairMid() - (Double.valueOf(ecStaticDataDaily.getTpSizeVariable07()) / 10000)
        );
        ecStaticDataDaily.setTpSizeVariable08Bearish(ecStaticDataDaily.getCcyPairTerm().equals("JPY") ?
                ecStaticDataDaily.getCcyPairMid() - (Double.valueOf(ecStaticDataDaily.getTpSizeVariable08()) / 100) :
                ecStaticDataDaily.getCcyPairMid() - (Double.valueOf(ecStaticDataDaily.getTpSizeVariable08()) / 10000)
        );
        ecStaticDataDaily.setTpSizeVariable09Bearish(ecStaticDataDaily.getCcyPairTerm().equals("JPY") ?
                ecStaticDataDaily.getCcyPairMid() - (Double.valueOf(ecStaticDataDaily.getTpSizeVariable09()) / 100) :
                ecStaticDataDaily.getCcyPairMid() - (Double.valueOf(ecStaticDataDaily.getTpSizeVariable09()) / 10000)
        );
        ecStaticDataDaily.setTpSizeVariable10Bearish(ecStaticDataDaily.getCcyPairTerm().equals("JPY") ?
                ecStaticDataDaily.getCcyPairMid() - (Double.valueOf(ecStaticDataDaily.getTpSizeVariable10()) / 100) :
                ecStaticDataDaily.getCcyPairMid() - (Double.valueOf(ecStaticDataDaily.getTpSizeVariable10()) / 10000)
        );
        ecStaticDataDaily.setTpSizeVariable11Bearish(ecStaticDataDaily.getCcyPairTerm().equals("JPY") ?
                ecStaticDataDaily.getCcyPairMid() - (Double.valueOf(ecStaticDataDaily.getTpSizeVariable11()) / 100) :
                ecStaticDataDaily.getCcyPairMid() - (Double.valueOf(ecStaticDataDaily.getTpSizeVariable11()) / 10000)
        );
        ecStaticDataDaily.setTpSizeVariable12Bearish(ecStaticDataDaily.getCcyPairTerm().equals("JPY") ?
                ecStaticDataDaily.getCcyPairMid() - (Double.valueOf(ecStaticDataDaily.getTpSizeVariable12()) / 100) :
                ecStaticDataDaily.getCcyPairMid() - (Double.valueOf(ecStaticDataDaily.getTpSizeVariable12()) / 10000)
        );

        return ecStaticDataDaily;
    }

    private Double subStringLeftDouble(Double param) {
        return Double.valueOf(param.toString().substring(0, 4));
    }

    private Double subStringRightDouble(Double param) {
        String toString = formatDecimal(param, 10);
        //i need 5 and 6 as result
        return Double.valueOf(toString.substring(4, 6));
        //1.09205
    }

}
