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
    Double bidPosSize01;
    Double bidPosSize02;
    Double bidPosSize03;
    Double bidPosSize04;
    Double bidPosSize05;
    Double bidPosSize06;
    Double bidPosSize07;
    Double bidPosSize08;
    Double askPosSize01;
    Double askPosSize02;
    Double askPosSize03;
    Double askPosSize04;
    Double askPosSize05;
    Double askPosSize06;
    Double askPosSize07;
    Double askPosSize08;

    Double posSize01;
    Double posSize02;
    Double posSize03;
    Double posSize04;

    Double ccyPairPartRate;

}
