package hyphin.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;


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

}
