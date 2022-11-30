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
    Double posSize01;
    Double posSize02;
    Double posSize03;
    Double posSize04;

    Double ccyPairPartRate;

    String userCapitalPosSize;

    Double userCapitalPosSize01;
    Double userCapitalPosSize02;
    Double userCapitalPosSize03;
    Double userCapitalPosSize04;
    Double userCapitalPosSize05;
    Double userCapitalPosSize06;
    Double userCapitalPosSize07;
    Double userCapitalPosSize08;

    Double ccyPairTradedRate;
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
}
