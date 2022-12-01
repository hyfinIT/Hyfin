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
    @Column(name = "DATE")
    String date;
    @Column(name = "REGION")
    String region;
    @Column(name = "CCY_PAIR_GROUP")
    String ccyPairGroup;
    @Column(name = "CCY_PAIR")
    String ccyPair;
    @Column(name = "CCY_PAIR_BASE")
    String ccyPairBase;
    @Column(name = "CCY_PAIR_TERM")
    String ccyPairTerm;
    @Column(name = "CCY_PAIR_SPREAD")
    Double ccyPairSpread;
    @Column(name = "CCY_PAIR_MID")
    Double ccyPairMid;
    @Column(name = "CCY_PAIR_BID")
    Double ccyPairBid;
    @Column(name = "CCY_PAIR_ASK")
    Double ccyPairAsk;
    @Column(name = "ATMOS_STAGE")
    String atmosStage;
    @Column(name = "BASE_ATMOS_DA")
    String baseAtmosDa;
    @Column(name = "BASE_ATMOS_CB")
    String baseAtmosCb;
    @Column(name = "BASE_ATMOS_MP")
    String baseAtmosMp;
    @Column(name = "BASE_ATMOS_DA_TS")
    Integer baseAtmosDaTs;
    @Column(name = "BASE_ATMOS_CB_TS")
    Integer baseAtmosCbTs;
    @Column(name = "BASE_ATMOS_MP_TS")
    Integer baseAtmosMpTs;
    @Column(name = "BASE_ATMOS_DA_CTS")
    Integer baseAtmosDaCts;
    @Column(name = "BASE_ATMOS_CB_CTS")
    Integer baseAtmosCts;
    @Column(name = "BASE_ATMOS_MP_CTS")
    Integer baseAtmosMpCts;
    @Column(name = "BASE_ATMOS_INF_RATE")
    Double baseAtmosInfRate;
    @Column(name = "BASE_ATMOS_INF_TREND")
    String baseAtmosInfTrend;
    @Column(name = "TERM_ATMOS_DA")
    String termAtmosDa;
    @Column(name = "TERM_ATMOS_CB")
    String termAtmosCb;
    @Column(name = "TERM_ATMOS_MP")
    String termAtmosMp;
    @Column(name = "TERM_ATMOS_DA_TS")
    Integer termAtmosDaTs;
    @Column(name = "TERM_ATMOS_CB_TS")
    Integer termAtmosCbTS;
    @Column(name = "TERM_ATMOS_MP_TS")
    Integer termAtmosMpTs;
    @Column(name = "TERM_ATMOS_DA_CTS")
    Integer termAtmosDaCts;
    @Column(name = "TERM_ATMOS_CB_CTS")
    Integer termAtmosCbCts;
    @Column(name = "TERM_ATMOS_MP_CTS")
    Integer termAtmosMpCts;
    @Column(name = "TERM_ATMOS_INFL_RATE")
    Double termAtmosInflationRate;
    @Column(name = "TERM_ATMOS_INFL_TREND")
    String termAtmosInflTrend;
    @Column(name = "CCY_PAIR_TRADED_RATE_BULLISH")
    Double ccyPairTradedRateBullish;
    @Column(name = "CCY_PAIR_TRADED_RATE_BEARISH")
    Double ccyPairTradedRateBearish;
    @Column(name = "CCY_PAIR_PART_RATE")
    Double ccyPairPartRate;
    @Column(name = "CCY_PAIR_BID_PIPS")
    Integer ccyPairBidPips;
    @Column(name = "CCY_PAIR_ASK_PIPS")
    Integer ccyPairAskPips;
    @Column(name = "USER_MARGIN_CCY")
    String userMarginCcy;
    @Column(name = "USER_MARGIN")
    Double userMargin;
    @Column(name = "BROKER_LEVERAGE")
    Integer brokerLeverage;
    @Column(name = "MAX_POS_SIZE_BASE")
    Double maxPosSizeBase;
    @Column(name = "MAX_POS_SIZE_TERM")
    Double maxPosSizeTerm;
    @Column(name = "MRG_SIZE_VARIABLE_01")
    Double mrgSizeVariable01;
    @Column(name = "MRG_SIZE_VARIABLE_02")
    Double mrgSizeVariable02;
    @Column(name = "MRG_SIZE_VARIABLE_03")
    Double mrgSizeVariable03;
    @Column(name = "MRG_SIZE_VARIABLE_04")
    Double mrgSizeVariable04;
    @Column(name = "MRG_SIZE_VARIABLE_05")
    Double mrgSizeVariable05;
    @Column(name = "MRG_SIZE_VARIABLE_06")
    Double mrgSizeVariable06;
    @Column(name = "MRG_SIZE_VARIABLE_07")
    Double mrgSizeVariable07;
    @Column(name = "MRG_SIZE_VARIABLE_08")
    Double mrgSizeVariable08;
    @Column(name = "MRG_SIZE_01")
    Double mrgSize01;
    @Column(name = "POS_SIZE_01")
    Double posSize01;
    @Column(name = "USER_CAPITAL_POS_SIZE_01")
    Double userCapitalPosSize01;
    @Column(name = "MID_POS_SIZE_01")
    Double midPosSize01;
    @Column(name = "BID_POS_SIZE_01")
    Double bidPosSize01;
    @Column(name = "ASK_POS_SIZE_01")
    Double askPosSize01;
    @Column(name = "PNL_BASE_01_BULLISH")
    Double pnlBase01Bullish;
    @Column(name = "PNL_TERM_01_BULLISH")
    Double pnlTerm01Bullish;
    @Column(name = "PNL_BASE_01_BEARISH")
    Double pnlBase01Bearish;
    @Column(name = "PNL_TERM_01_BEARISH")
    Double pnlTerm01Bearish;
    @Column(name = "MRG_SIZE_02")
    Double mrgSize02;
    @Column(name = "POS_SIZE_02")
    Double posSize02;
    @Column(name = "USER_CAPITAL_POS_SIZE_02")
    Double userCapitalPosSize02;
    @Column(name = "MID_POS_SIZE_02")
    Double midPosSize02;
    @Column(name = "BID_POS_SIZE_02")
    Double bidPosSize02;
    @Column(name = "ASK_POS_SIZE_02")
    Double askPosSize02;
    @Column(name = "PNL_BASE_02_BULLISH")
    Double pnlBase02Bullish;
    @Column(name = "PNL_TERM_02_BULLISH")
    Double pnlTerm02Bullish;
    @Column(name = "PNL_BASE_02_BEARISH")
    Double pnlBase02Bearish;
    @Column(name = "PNL_TERM_02_BEARISH")
    Double pnlTerm02Bearish;
    @Column(name = "MRG_SIZE_03")
    Double mrgSize03;
    @Column(name = "POS_SIZE_03")
    Double posSize03;
    @Column(name = "USER_CAPITAL_POS_SIZE_03")
    Double userCapitalPosSize03;
    @Column(name = "MID_POS_SIZE_03")
    Double midPosSize03;
    @Column(name = "BID_POS_SIZE_03")
    Double bidPosSize03;
    @Column(name = "ASK_POS_SIZE_03")
    Double askPosSize03;
    @Column(name = "PNL_BASE_03_BULLISH")
    Double pnlBase03Bullish;
    @Column(name = "PNL_TERM_03_BULLISH")
    Double pnlTerm03Bullish;
    @Column(name = "PNL_BASE_03_BEARISH")
    Double pnlBase03Bearish;
    @Column(name = "PNL_TERM_03_BEARISH")
    Double pnlTerm03Bearish;
    @Column(name = "MRG_SIZE_04")
    Double mrgSize04;
    @Column(name = "POS_SIZE_04")
    Double posSize04;
    @Column(name = "USER_CAPITAL_POS_SIZE_04")
    Double userCapitalPosSize04;
    @Column(name = "MID_POS_SIZE_04")
    Double midPosSize04;
    @Column(name = "BID_POS_SIZE_04")
    Double bidPosSize04;
    @Column(name = "ASK_POS_SIZE_04")
    Double askPosSize04;
    @Column(name = "PNL_BASE_04_BULLISH")
    Double pnlBase04Bullish;
    @Column(name = "PNL_TERM_04_BULLISH")
    Double pnlTerm04Bullish;
    @Column(name = "PNL_BASE_04_BEARISH")
    Double pnlBase04Bearish;
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

    @Column(name = "SL_SIZE_VARIABLE_01")
    Integer slSizeVariable01;
    @Column(name = "SL_SIZE_VARIABLE_02")
    Integer slSizeVariable02;
    @Column(name = "SL_SIZE_VARIABLE_03")
    Integer slSizeVariable03;
    @Column(name = "SL_SIZE_VARIABLE_04")
    Integer slSizeVariable04;
    @Column(name = "SL_SIZE_VARIABLE_05")
    Integer slSizeVariable05;
    @Column(name = "SL_SIZE_VARIABLE_06")
    Integer slSizeVariable06;
    @Column(name = "SL_SIZE_VARIABLE_07")
    Integer slSizeVariable07;
    @Column(name = "SL_SIZE_VARIABLE_08")
    Integer slSizeVariable08;
    @Column(name = "SL_SIZE_VARIABLE_09")
    Integer slSizeVariable09;
    @Column(name = "SL_SIZE_VARIABLE_10")
    Integer slSizeVariable10;
    @Column(name = "SL_SIZE_VARIABLE_11")
    Integer slSizeVariable11;
    @Column(name = "SL_SIZE_VARIABLE_12")
    Integer slSizeVariable12;
    @Column(name = "TP_SIZE_VARIABLE_01")
    Integer tpSizeVariable01;
    @Column(name = "TP_SIZE_VARIABLE_02")
    Integer tpSizeVariable02;
    @Column(name = "TP_SIZE_VARIABLE_03")
    Integer tpSizeVariable03;
    @Column(name = "TP_SIZE_VARIABLE_04")
    Integer tpSizeVariable04;
    @Column(name = "TP_SIZE_VARIABLE_05")
    Integer tpSizeVariable05;
    @Column(name = "TP_SIZE_VARIABLE_06")
    Integer tpSizeVariable06;
    @Column(name = "TP_SIZE_VARIABLE_07")
    Integer tpSizeVariable07;
    @Column(name = "TP_SIZE_VARIABLE_08")
    Integer tpSizeVariable08;
    @Column(name = "TP_SIZE_VARIABLE_09")
    Integer tpSizeVariable09;
    @Column(name = "TP_SIZE_VARIABLE_10")
    Integer tpSizeVariable10;
    @Column(name = "TP_SIZE_VARIABLE_11")
    Integer tpSizeVariable11;
    @Column(name = "TP_SIZE_VARIABLE_12")
    Integer tpSizeVariable12;
    @Column(name = "SL_SIZE_VARIABLE_01_BULLISH")
    Double slSizeVariable01Bullish;
    @Column(name = "SL_SIZE_VARIABLE_02_BULLISH")
    Double slSizeVariable02Bullish;
    @Column(name = "SL_SIZE_VARIABLE_03_BULLISH")
    Double slSizeVariable03Bullish;
    @Column(name = "SL_SIZE_VARIABLE_04_BULLISH")
    Double slSizeVariable04Bullish;
    @Column(name = "SL_SIZE_VARIABLE_05_BULLISH")
    Double slSizeVariable05Bullish;
    @Column(name = "SL_SIZE_VARIABLE_06_BULLISH")
    Double slSizeVariable06Bullish;
    @Column(name = "SL_SIZE_VARIABLE_07_BULLISH")
    Double slSizeVariable07Bullish;
    @Column(name = "SL_SIZE_VARIABLE_08_BULLISH")
    Double slSizeVariable08Bullish;
    @Column(name = "SL_SIZE_VARIABLE_09_BULLISH")
    Double slSizeVariable09Bullish;
    @Column(name = "SL_SIZE_VARIABLE_10_BULLISH")
    Double slSizeVariable10Bullish;
    @Column(name = "SL_SIZE_VARIABLE_11_BULLISH")
    Double slSizeVariable11Bullish;
    @Column(name = "SL_SIZE_VARIABLE_12_BULLISH")
    Double slSizeVariable12Bullish;
    @Column(name = "SL_SIZE_VARIABLE_01_BEARISH")
    Double slSizeVariable01Bearish;
    @Column(name = "SL_SIZE_VARIABLE_02_BEARISH")
    Double slSizeVariable02Bearish;
    @Column(name = "SL_SIZE_VARIABLE_03_BEARISH")
    Double slSizeVariable03Bearish;
    @Column(name = "SL_SIZE_VARIABLE_04_BEARISH")
    Double slSizeVariable04Bearish;
    @Column(name = "SL_SIZE_VARIABLE_05_BEARISH")
    Double slSizeVariable05Bearish;
    @Column(name = "SL_SIZE_VARIABLE_06_BEARISH")
    Double slSizeVariable06Bearish;
    @Column(name = "SL_SIZE_VARIABLE_07_BEARISH")
    Double slSizeVariable07Bearish;
    @Column(name = "SL_SIZE_VARIABLE_08_BEARISH")
    Double slSizeVariable08Bearish;
    @Column(name = "SL_SIZE_VARIABLE_09_BEARISH")
    Double slSizeVariable09Bearish;
    @Column(name = "SL_SIZE_VARIABLE_10_BEARISH")
    Double slSizeVariable10Bearish;
    @Column(name = "SL_SIZE_VARIABLE_11_BEARISH")
    Double slSizeVariable11Bearish;
    @Column(name = "SL_SIZE_VARIABLE_12_BEARISH")
    Double slSizeVariable12Bearish;
    @Column(name = "TP_SIZE_VARIABLE_01_BULLISH")
    Double tpSizeVariable01Bullish;
    @Column(name = "TP_SIZE_VARIABLE_02_BULLISH")
    Double tpSizeVariable02Bullish;
    @Column(name = "TP_SIZE_VARIABLE_03_BULLISH")
    Double tpSizeVariable03Bullish;
    @Column(name = "TP_SIZE_VARIABLE_04_BULLISH")
    Double tpSizeVariable04Bullish;
    @Column(name = "TP_SIZE_VARIABLE_05_BULLISH")
    Double tpSizeVariable05Bullish;
    @Column(name = "TP_SIZE_VARIABLE_06_BULLISH")
    Double tpSizeVariable06Bullish;
    @Column(name = "TP_SIZE_VARIABLE_07_BULLISH")
    Double tpSizeVariable07Bullish;
    @Column(name = "TP_SIZE_VARIABLE_08_BULLISH")
    Double tpSizeVariable08Bullish;
    @Column(name = "TP_SIZE_VARIABLE_09_BULLISH")
    Double tpSizeVariable09Bullish;
    @Column(name = "TP_SIZE_VARIABLE_10_BULLISH")
    Double tpSizeVariable10Bullish;
    @Column(name = "TP_SIZE_VARIABLE_11_BULLISH")
    Double tpSizeVariable11Bullish;
    @Column(name = "TP_SIZE_VARIABLE_12_BULLISH")
    Double tpSizeVariable12Bullish;
    @Column(name = "TP_SIZE_VARIABLE_01_BEARISH")
    Double tpSizeVariable01Bearish;
    @Column(name = "TP_SIZE_VARIABLE_02_BEARISH")
    Double tpSizeVariable02Bearish;
    @Column(name = "TP_SIZE_VARIABLE_03_BEARISH")
    Double tpSizeVariable03Bearish;
    @Column(name = "TP_SIZE_VARIABLE_04_BEARISH")
    Double tpSizeVariable04Bearish;
    @Column(name = "TP_SIZE_VARIABLE_05_BEARISH")
    Double tpSizeVariable05Bearish;
    @Column(name = "TP_SIZE_VARIABLE_06_BEARISH")
    Double tpSizeVariable06Bearish;
    @Column(name = "TP_SIZE_VARIABLE_07_BEARISH")
    Double tpSizeVariable07Bearish;
    @Column(name = "TP_SIZE_VARIABLE_08_BEARISH")
    Double tpSizeVariable08Bearish;
    @Column(name = "TP_SIZE_VARIABLE_09_BEARISH")
    Double tpSizeVariable09Bearish;
    @Column(name = "TP_SIZE_VARIABLE_10_BEARISH")
    Double tpSizeVariable10Bearish;
    @Column(name = "TP_SIZE_VARIABLE_11_BEARISH")
    Double tpSizeVariable11Bearish;
    @Column(name = "TP_SIZE_VARIABLE_12_BEARISH")
    Double tpSizeVariable12Bearish;
}
