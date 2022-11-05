package hyphin.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "EC_STATICDATA", schema = "PUBLIC", catalog = "HYFIN")
@Getter
@Setter
public class EcStaticData {

    @Id
    private Long id;
    private String client;
    @Column(name = "USER_MARGIN_CCY")
    private String userMarginCcy;
    @Column(name = "USER_MARGIN")
    private Integer userMargin;
    @Column(name = "BROKER_LEVERAGE")
    private Integer brokerLeverage;
    @Column(name = "MRG_SIZE_VARIABLE_01")
    private Double mrgSizeVariable01;
    @Column(name = "MRG_SIZE_VARIABLE_02")
    private Double mrgSizeVariable02;
    @Column(name = "MRG_SIZE_VARIABLE_03")
    private Double mrgSizeVariable03;
    @Column(name = "MRG_SIZE_VARIABLE_04")
    private Double mrgSizeVariable04;
    @Column(name = "MRG_SIZE_VARIABLE_05")
    private Double mrgSizeVariable05;
    @Column(name = "MRG_SIZE_VARIABLE_06")
    private Double mrgSizeVariable06;
    @Column(name = "MRG_SIZE_VARIABLE_07")
    private Double mrgSizeVariable07;
    @Column(name = "MRG_SIZE_VARIABLE_08")
    private Double mrgSizeVariable08;


//
//    ID int,
//    CLIENT text,
//  "User_Margin_CCY" text,
//            "User_Margin" int,
//            "Broker_Leverage" int,
//            "MrgSize_variable_01" text,
//            "MrgSize_variable_02" text,
//            "MrgSize_variable_03" text,
//            "MrgSize_variable_04" text,
//            "MrgSize_variable_05" text,
//            "MrgSize_variable_06" text,
//            "MrgSize_variable_07" text,
//            "MrgSize_variable_08" text
}
