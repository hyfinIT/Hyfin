package hyphin.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "EC_STATIC_DATA_DAILY", schema = "PUBLIC", catalog = "HYFIN")
@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EcStaticDataDaily {

    @Id
    @Column(name = "ID")
    Long id;
    //    DATE
    @Column(name = "DATE")
    String date;
    //            REGION
    @Column(name = "REGION")
    String region;
    //    CCY_PAIR_GROUP
    @Column(name = "CCY_PAIR_GROUP")
    String ccyPairGroup;
    //            CCY_PAIR
    @Column(name = "CCY_PAIR")
    String ccyPair;
    //    CCY_PAIR_BASE
    @Column(name = "CCY_PAIR_BASE")
    String ccyPairBase;
    //    CCY_PAIR_TERM
    @Column(name = "CCY_PAIR_TERM")
    String ccyPairTerm;
    //    CCY_PAIR_SPREAD
    @Column(name = "CCY_PAIR_SPREAD")
    Double ccyPairSpread;
    //            CCY_PAIR_MID
    @Column(name = "CCY_PAIR_MID")
    Double ccyPairMid;
    //    CCY_PAIR_BID
    @Column(name = "CCY_PAIR_BID")
    Double ccyPairBid;
    //            CCY_PAIR_ASK
    @Column(name = "CCY_PAIR_ASK")
    Double ccyPairAsk;
    //    ATMOS_STAGE
    @Column(name = "ATMOS_STAGE")
    String atmosStage;
    //            BASE_ATMOS_DA
    @Column(name = "BASE_ATMOS_DA")
    String baseAtmosDa;
    //    BASE_ATMOS_CB
    @Column(name = "BASE_ATMOS_CB")
    String baseAtmosCb;
    //            BASE_ATMOS_MP
    @Column(name = "BASE_ATMOS_MP")
    String baseAtmosMp;
    //    BASE_ATMOS_DA_TS
    @Column(name = "BASE_ATMOS_DA_TS")
    Integer baseAtmosDaTs;
    //            BASE_ATMOS_CB_TS
    @Column(name = "BASE_ATMOS_CB_TS")
    Integer baseAtmosCbTs;
    //    BASE_ATMOS_MP_TS
    @Column(name = "BASE_ATMOS_MP_TS")
    Integer baseAtmosMpTs;
    //            BASE_ATMOS_DA_CTS
    @Column(name = "BASE_ATMOS_DA_CTS")
    Integer baseAtmosDaCts;
    //    BASE_ATMOS_CB_CTS
    @Column(name = "BASE_ATMOS_CB_CTS")
    Integer baseAtmosCts;
    //            BASE_ATMOS_MP_CTS
    @Column(name = "BASE_ATMOS_MP_CTS")
    Integer baseAtmosMpCts;
    //    BASE_ATMOS_INF_RATE
    @Column(name = "BASE_ATMOS_INF_RATE")
    Double baseAtmosInfRate;
    //            BASE_ATMOS_INF_TREND
    @Column(name = "BASE_ATMOS_INF_TREND")
    String baseAtmosInfTrend;
    //    TERM_ATMOS_DA
    @Column(name = "TERM_ATMOS_DA")
    String termAtmosDa;
    //            TERM_ATMOS_CB
    @Column(name = "TERM_ATMOS_CB")
    String termAtmosCb;
    //    TERM_ATMOS_MP
    @Column(name = "TERM_ATMOS_MP")
    String termAtmosMp;
    //            TERM_ATMOS_DA_TS
    @Column(name = "TERM_ATMOS_DA_TS")
    Integer termAtmosDaTs;
    //    TERM_ATMOS_CB_TS
    @Column(name = "TERM_ATMOS_CB_TS")
    Integer termAtmosCbTS;
    //            TERM_ATMOS_MP_TS
    @Column(name = "TERM_ATMOS_MP_TS")
    Integer termAtmosMpTs;
    //    TERM_ATMOS_DA_CTS
    @Column(name = "TERM_ATMOS_DA_CTS")
    Integer termAtmosDaCts;
    //            TERM_ATMOS_CB_CTS
    @Column(name = "TERM_ATMOS_CB_CTS")
    Integer termAtmosCbCts;
    //    TERM_ATMOS_MP_CTS
    @Column(name = "TERM_ATMOS_MP_CTS")
    Integer termAtmosMpCts;
    //            TERM_ATMOS_INFL_RATE
    @Column(name = "TERM_ATMOS_INFL_RATE")
    Double termAtmosInflationRate;
    //    TERM_ATMOS_INFL_TREND
    @Column(name = "TERM_ATMOS_INFL_TREND")
    String termAtmosInflTrend;
    //            CCY_PAIR_TRADED_RATE_BULLISH
    @Column(name = "CCY_PAIR_TRADED_RATE_BULLISH")
    Double ccyPairTradedRateBullish;
    //    CCY_PAIR_TRADED_RATE_BEARISH
    @Column(name = "CCY_PAIR_TRADED_RATE_BEARISH")
    Double ccyPairTradedRateBearish;
    //            CCY_PAIR_PART_RATE
    @Column(name = "CCY_PAIR_PART_RATE")
    Double ccyPairPartRate;
    //    CCY_PAIR_BID_PIPS
    @Column(name = "CCY_PAIR_BID_PIPS")
    Integer ccyPairBidPips;
    //            CCY_PAIR_ASK_PIPS
    @Column(name = "CCY_PAIR_ASK_PIPS")
    Integer ccyPairAskPips;
    //    USER_MARGIN_CCY
    @Column(name = "USER_MARGIN_CCY")
    String userMarginCcy;
    //            USER_MARGIN
    @Column(name = "USER_MARGIN")
    Double userMargin;
    //    BROKER_LEVERAGE
    @Column(name = "BROKER_LEVERAGE")
    Integer brokerLeverage;
    //            MAX_POS_SIZE_BASE
    @Column(name = "MAX_POS_SIZE_BASE")
    Double maxPosSizeBase;
    //    MAX_POS_SIZE_TERM
    @Column(name = "MAX_POS_SIZE_TERM")
    Double maxPosSizeTerm;
    //            MRG_SIZE_VARIABLE_01
    @Column(name = "MRG_SIZE_VARIABLE_01")
    Double mrgSizeVariable01;
    //    MRG_SIZE_VARIABLE_02
    @Column(name = "MRG_SIZE_VARIABLE_02")
    Double mrgSizeVariable02;
    //            MRG_SIZE_VARIABLE_03
    @Column(name = "MRG_SIZE_VARIABLE_03")
    Double mrgSizeVariable03;
    //    MRG_SIZE_VARIABLE_04
    @Column(name = "MRG_SIZE_VARIABLE_04")
    Double mrgSizeVariable04;
    //            MRG_SIZE_VARIABLE_05
    @Column(name = "MRG_SIZE_VARIABLE_05")
    Double mrgSizeVariable05;
    //    MRG_SIZE_VARIABLE_06
    @Column(name = "MRG_SIZE_VARIABLE_06")
    Double mrgSizeVariable06;
    //            MRG_SIZE_VARIABLE_07
    @Column(name = "MRG_SIZE_VARIABLE_07")
    Double mrgSizeVariable07;
    //    MRG_SIZE_VARIABLE_08
    @Column(name = "MRG_SIZE_VARIABLE_08")
    Double mrgSizeVariable08;
    //            MRG_SIZE_01
    @Column(name = "MRG_SIZE_01")
    Double mrgSize01;
    //    POS_SIZE_01
    @Column(name = "POS_SIZE_01")
    Double posSize01;
    //            USER_CAPITAL_POS_SIZE_01
    @Column(name = "USER_CAPITAL_POS_SIZE_01")
    Double userCapitalPosSize01;
    //    MID_POS_SIZE_01
    @Column(name = "MID_POS_SIZE_01")
    Double midPosSize01;
    //            BID_POS_SIZE_01
    @Column(name = "BID_POS_SIZE_01")
    Double bidPosSize01;
    //    ASK_POS_SIZE_01
    @Column(name = "ASK_POS_SIZE_01")
    Double askPosSize01;
    //            PNL_BASE_01_BULLISH
    @Column(name = "PNL_BASE_01_BULLISH")
    Double pnlBase01Bullish;
    //    PNL_TERM_01_BULLISH
    @Column(name = "PNL_TERM_01_BULLISH")
    Double pnlTerm01Bullish;
    //            PNL_BASE_01_BEARISH
    @Column(name = "PNL_BASE_01_BEARISH")
    Double pnlBase01Bearish;
    //    PNL_TERM_01_BEARISH
    @Column(name = "PNL_TERM_01_BEARISH")
    Double pnlTerm01Bearish;
    //            MRG_SIZE_02
    @Column(name = "MRG_SIZE_02")
    Double mrgSize02;
    //    POS_SIZE_02
    @Column(name = "POS_SIZE_02")
    Double posSize02;
    //            USER_CAPITAL_POS_SIZE_02
    @Column(name = "USER_CAPITAL_POS_SIZE_02")
    Double userCapitalPosSize02;
    //    MID_POS_SIZE_02
    @Column(name = "MID_POS_SIZE_02")
    Double midPosSize02;
    //            BID_POS_SIZE_02
    @Column(name = "BID_POS_SIZE_02")
    Double bidPosSize02;
    //    ASK_POS_SIZE_02
    @Column(name = "ASK_POS_SIZE_02")
    Double askPosSize02;
    //            PNL_BASE_02_BULLISH
    @Column(name = "PNL_BASE_02_BULLISH")
    Double pnlBase02Bullish;
    //    PNL_TERM_02_BULLISH
    @Column(name = "PNL_TERM_02_BULLISH")
    Double pnlTerm02Bullish;
    //            PNL_BASE_02_BEARISH
    @Column(name = "PNL_BASE_02_BEARISH")
    Double pnlBase02Bearish;
    //    PNL_TERM_02_BEARISH
    @Column(name = "PNL_TERM_02_BEARISH")
    Double pnlTerm02Bearish;
    //            MRG_SIZE_03
    @Column(name = "MRG_SIZE_03")
    Double mrgSize03;
    //    POS_SIZE_03
    @Column(name = "POS_SIZE_03")
    Double posSize03;
    //            USER_CAPITAL_POS_SIZE_03
    @Column(name = "USER_CAPITAL_POS_SIZE_03")
    Double userCapitalPosSize03;
    //    MID_POS_SIZE_03
    @Column(name = "MID_POS_SIZE_03")
    Double midPosSize03;
    //            BID_POS_SIZE_03
    @Column(name = "BID_POS_SIZE_03")
    Double bidPosSize03;
    //    ASK_POS_SIZE_03
    @Column(name = "ASK_POS_SIZE_03")
    Double askPosSize03;
    //            PNL_BASE_03_BULLISH
    @Column(name = "PNL_BASE_03_BULLISH")
    Double pnlBase03Bullish;
    //    PNL_TERM_03_BULLISH
    @Column(name = "PNL_TERM_03_BULLISH")
    Double pnlTerm03Bullish;
    //            PNL_BASE_03_BEARISH
    @Column(name = "PNL_BASE_03_BEARISH")
    Double pnlBase03Bearish;
    //    PNL_TERM_03_BEARISH
    @Column(name = "PNL_TERM_03_BEARISH")
    Double pnlTerm03Bearish;
    //            MRG_SIZE_04
    @Column(name = "MRG_SIZE_04")
    Double mrgSize04;
    //    POS_SIZE_04
    @Column(name = "POS_SIZE_04")
    Double posSize04;
    //            USER_CAPITAL_POS_SIZE_04
    @Column(name = "USER_CAPITAL_POS_SIZE_04")
    Double userCapitalPosSize04;
    //    MID_POS_SIZE_04
    @Column(name = "MID_POS_SIZE_04")
    Double midPosSize04;
    //            BID_POS_SIZE_04
    @Column(name = "BID_POS_SIZE_04")
    Double bidPosSize04;
    //    ASK_POS_SIZE_04
    @Column(name = "ASK_POS_SIZE_04")
    Double askPosSize04;
    //            PNL_BASE_04_BULLISH
    @Column(name = "PNL_BASE_04_BULLISH")
    Double pnlBase04Bullish;
    //    PNL_TERM_04_BULLISH
    @Column(name = "PNL_TERM_04_BULLISH")
    Double pnlTerm04Bullish;
    //            PNL_BASE_04_BEARISH
    @Column(name = "PNL_BASE_04_BEARISH")
    Double pnlBase04Bearish;
    //    PNL_TERM_04_BEARISH
    @Column(name = "PNL_TERM_04_BEARISH")
    Double pnlTerm04Bearish;


    @Column(name = "MRG_SIZE_05")
    Double mrgSize05;
    @Column(name = "POS_SIZE_05")
    Double posSize05;
    @Column(name = "USER_CAPITAL_POS_SIZE_05")
    Double userCapitalPosSize05;
    @Column(name = "MID_POS_SIZE_05")
    Double midPosSize05;
    @Column(name = "BID_POS_SIZE_05")
    Double bidPosSize05;
    @Column(name = "ASK_POS_SIZE_05")
    Double askPosSize05;
    @Column(name = "PNL_BASE_05_BULLISH")
    Double pnlBase05Bullish;
    @Column(name = "PNL_TERM_05_BULLISH")
    Double pnlTerm05Bullish;
    @Column(name = "PNL_BASE_05_BEARISH")
    Double pnlBase05Bearish;
    @Column(name = "PNL_TERM_05_BEARISH")
    Double pnlTerm05Bearish;
    @Column(name = "MRG_SIZE_06")
    Double mrgSize06;
    @Column(name = "POS_SIZE_06")
    Double posSize06;
    @Column(name = "USER_CAPITAL_POS_SIZE_06")
    Double userCapitalPosSize06;
    @Column(name = "MID_POS_SIZE_06")
    Double midPosSize06;
    @Column(name = "BID_POS_SIZE_06")
    Double bidPosSize06;
    @Column(name = "ASK_POS_SIZE_06")
    Double askPosSize06;
    @Column(name = "PNL_BASE_06_BULLISH")
    Double pnlBase06Bullish;
    @Column(name = "PNL_TERM_06_BULLISH")
    Double pnlTerm06Bullish;
    @Column(name = "PNL_BASE_06_BEARISH")
    Double pnlBase06Bearish;
    @Column(name = "PNL_TERM_06_BEARISH")
    Double pnlTerm06Bearish;
    @Column(name = "MRG_SIZE_07")
    Double mrgSize07;
    @Column(name = "POS_SIZE_07")
    Double posSize07;
    @Column(name = "USER_CAPITAL_POS_SIZE_07")
    Double userCapitalPosSize07;
    @Column(name = "MID_POS_SIZE_07")
    Double midPosSize07;
    @Column(name = "BID_POS_SIZE_07")
    Double bidPosSize07;
    @Column(name = "ASK_POS_SIZE_07")
    Double askPosSize07;
    @Column(name = "PNL_BASE_07_BULLISH")
    Double pnlBase07Bullish;
    @Column(name = "PNL_TERM_07_BULLISH")
    Double pnlTerm07Bullish;
    @Column(name = "PNL_BASE_07_BEARISH")
    Double pnlBase07Bearish;
    @Column(name = "PNL_TERM_07_BEARISH")
    Double pnlTerm07Bearish;
    @Column(name = "MRG_SIZE_08")
    Double mrgSize08;
    @Column(name = "POS_SIZE_08")
    Double posSize08;
    @Column(name = "USER_CAPITAL_POS_SIZE_08")
    Double userCapitalPosSize08;
    @Column(name = "MID_POS_SIZE_08")
    Double midPosSize08;
    @Column(name = "BID_POS_SIZE_08")
    Double bidPosSize08;
    @Column(name = "ASK_POS_SIZE_08")
    Double askPosSize08;
    @Column(name = "PNL_BASE_08_BULLISH")
    Double pnlBase08Bullish;
    @Column(name = "PNL_TERM_08_BULLISH")
    Double pnlTerm08Bullish;
    @Column(name = "PNL_BASE_08_BEARISH")
    Double pnlBase08Bearish;
    @Column(name = "PNL_TERM_08_BEARISH")
    Double pnlTerm08Bearish;
}
