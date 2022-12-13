package hyphin.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.persistence.Column;


@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EcStaticDataDailyDto {

    String mrgSizeVariable01;
    String mrgSizeVariable02;
    String mrgSizeVariable03;
    String mrgSizeVariable04;
    String mrgSizeVariable05;
    String mrgSizeVariable06;
    String mrgSizeVariable07;
    String mrgSizeVariable08;
    String userMargin;
    Integer brokerLeverage;
    String maxPosSizeBase;
    String maxPosSizeTerm;

    String ccyPairMid;
    Integer ccyPairBidPips;
    Integer ccyPairAskPips;

    String bidPosSize;
    Double bidPosSize01;           // 3??????????
    Double bidPosSize02;
    Double bidPosSize03;
    Double bidPosSize04;
    Double bidPosSize05;
    Double bidPosSize06;
    Double bidPosSize07;
    Double bidPosSize08;

    String askPosSize; //2
    Double askPosSize01;
    Double askPosSize02;
    Double askPosSize03;
    Double askPosSize04;
    Double askPosSize05;
    Double askPosSize06;
    Double askPosSize07;
    Double askPosSize08;

    String posSize; //3
    Double posSizeDouble; //3
    Double posSize01;
    Double posSize02;
    Double posSize03;
    Double posSize04;

    Double ccyPairPartRate;
    String ccyPairPartRateFormatted;

    String userCapitalPosSize;

    Double userCapitalPosSize01;
    Double userCapitalPosSize02;
    Double userCapitalPosSize03;
    Double userCapitalPosSize04;
    Double userCapitalPosSize05;
    Double userCapitalPosSize06;
    Double userCapitalPosSize07;
    Double userCapitalPosSize08;

    String ccyPairTradedRate;
    Double ccyPairTradedRateBullish;
    Double ccyPairTradedRateBearish;


    Double pnl;
    Double pnlBase01Bullish;
    Double pnlTerm01Bullish;
    Double pnlBase01Bearish;
    Double pnlTerm01Bearish;
    Double pnlBase02Bullish;
    Double pnlTerm02Bullish;
    Double pnlBase02Bearish;
    Double pnlTerm02Bearish;
    Double pnlBase03Bullish;
    Double pnlTerm03Bullish;
    Double pnlBase03Bearish;
    Double pnlTerm03Bearish;
    Double pnlBase04Bullish;
    Double pnlTerm04Bullish;
    Double pnlBase04Bearish;
    Double pnlTerm04Bearish;

    String mrgSize;
    Double mrgSize01;
    Double mrgSize02;
    Double mrgSize03;
    Double mrgSize04;

    Double mrgSize05;
    Double posSize05;
    Double pnlBase05Bullish;
    Double pnlTerm05Bullish;
    Double pnlBase05Bearish;
    Double pnlTerm05Bearish;
    Double mrgSize06;
    Double posSize06;
    Double pnlBase06Bullish;
    Double pnlTerm06Bullish;
    Double pnlBase06Bearish;
    Double pnlTerm06Bearish;
    Double mrgSize07;
    Double posSize07;
    Double pnlBase07Bullish;
    Double pnlTerm07Bullish;
    Double pnlBase07Bearish;
    Double pnlTerm07Bearish;
    Double mrgSize08;
    Double posSize08;
    Double pnlBase08Bullish;
    Double pnlTerm08Bullish;
    Double pnlBase08Bearish;
    Double pnlTerm08Bearish;

    String posSize01Formatted;
    String posSize02Formatted;
    String posSize03Formatted;
    String posSize04Formatted;
    String posSize05Formatted;
    String posSize06Formatted;
    String posSize07Formatted;
    String posSize08Formatted;

    String bidPosSize01Formatted;
    String bidPosSize02Formatted;
    String bidPosSize03Formatted;
    String bidPosSize04Formatted;
    String bidPosSize05Formatted;
    String bidPosSize06Formatted;
    String bidPosSize07Formatted;
    String bidPosSize08Formatted;

    String askPosSize01Formatted;
    String askPosSize02Formatted;
    String askPosSize03Formatted;
    String askPosSize04Formatted;
    String askPosSize05Formatted;
    String askPosSize06Formatted;
    String askPosSize07Formatted;
    String askPosSize08Formatted;

    Integer slSizeVariable01;
    Integer slSizeVariable02;
    Integer slSizeVariable03;
    Integer slSizeVariable04;
    Integer slSizeVariable05;
    Integer slSizeVariable06;
    Integer slSizeVariable07;
    Integer slSizeVariable08;
    Integer slSizeVariable09;
    Integer slSizeVariable10;
    Integer slSizeVariable11;
    Integer slSizeVariable12;

    Double slSizeVariable01Bullish;
    Double slSizeVariable02Bullish;
    Double slSizeVariable03Bullish;
    Double slSizeVariable04Bullish;
    Double slSizeVariable05Bullish;
    Double slSizeVariable06Bullish;
    Double slSizeVariable07Bullish;
    Double slSizeVariable08Bullish;
    Double slSizeVariable09Bullish;
    Double slSizeVariable10Bullish;
    Double slSizeVariable11Bullish;
    Double slSizeVariable12Bullish;
    Double slSizeVariable01Bearish;
    Double slSizeVariable02Bearish;
    Double slSizeVariable03Bearish;
    Double slSizeVariable04Bearish;
    Double slSizeVariable05Bearish;
    Double slSizeVariable06Bearish;
    Double slSizeVariable07Bearish;
    Double slSizeVariable08Bearish;
    Double slSizeVariable09Bearish;
    Double slSizeVariable10Bearish;
    Double slSizeVariable11Bearish;
    Double slSizeVariable12Bearish;

    String slSizeVariable01Rate;
    String slSizeVariable02Rate;
    String slSizeVariable03Rate;
    String slSizeVariable04Rate;
    String slSizeVariable05Rate;
    String slSizeVariable06Rate;
    String slSizeVariable07Rate;
    String slSizeVariable08Rate;
    String slSizeVariable09Rate;
    String slSizeVariable10Rate;
    String slSizeVariable11Rate;
    String slSizeVariable12Rate;




    Integer tpSizeVariable01;
    Integer tpSizeVariable02;
    Integer tpSizeVariable03;
    Integer tpSizeVariable04;
    Integer tpSizeVariable05;
    Integer tpSizeVariable06;
    Integer tpSizeVariable07;
    Integer tpSizeVariable08;
    Integer tpSizeVariable09;
    Integer tpSizeVariable10;
    Integer tpSizeVariable11;
    Integer tpSizeVariable12;

    Double tpSizeVariable01Bullish;
    Double tpSizeVariable02Bullish;
    Double tpSizeVariable03Bullish;
    Double tpSizeVariable04Bullish;
    Double tpSizeVariable05Bullish;
    Double tpSizeVariable06Bullish;
    Double tpSizeVariable07Bullish;
    Double tpSizeVariable08Bullish;
    Double tpSizeVariable09Bullish;
    Double tpSizeVariable10Bullish;
    Double tpSizeVariable11Bullish;
    Double tpSizeVariable12Bullish;
    Double tpSizeVariable01Bearish;
    Double tpSizeVariable02Bearish;
    Double tpSizeVariable03Bearish;
    Double tpSizeVariable04Bearish;
    Double tpSizeVariable05Bearish;
    Double tpSizeVariable06Bearish;
    Double tpSizeVariable07Bearish;
    Double tpSizeVariable08Bearish;
    Double tpSizeVariable09Bearish;
    Double tpSizeVariable10Bearish;
    Double tpSizeVariable11Bearish;
    Double tpSizeVariable12Bearish;

    String tpSizeVariable01Rate;
    String tpSizeVariable02Rate;
    String tpSizeVariable03Rate;
    String tpSizeVariable04Rate;
    String tpSizeVariable05Rate;
    String tpSizeVariable06Rate;
    String tpSizeVariable07Rate;
    String tpSizeVariable08Rate;
    String tpSizeVariable09Rate;
    String tpSizeVariable10Rate;
    String tpSizeVariable11Rate;
    String tpSizeVariable12Rate;
}
