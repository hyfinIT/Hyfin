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

    @Column(name = "SL_SIZE_VARIABLE_01")
    private Integer slSizeVariable01;
    @Column(name = "SL_SIZE_VARIABLE_02")
    private Integer slSizeVariable02;
    @Column(name = "SL_SIZE_VARIABLE_03")
    private Integer slSizeVariable03;
    @Column(name = "SL_SIZE_VARIABLE_04")
    private Integer slSizeVariable04;
    @Column(name = "SL_SIZE_VARIABLE_05")
    private Integer slSizeVariable05;
    @Column(name = "SL_SIZE_VARIABLE_06")
    private Integer slSizeVariable06;
    @Column(name = "SL_SIZE_VARIABLE_07")
    private Integer slSizeVariable07;
    @Column(name = "SL_SIZE_VARIABLE_08")
    private Integer slSizeVariable08;
    @Column(name = "SL_SIZE_VARIABLE_09")
    private Integer slSizeVariable09;
    @Column(name = "SL_SIZE_VARIABLE_10")
    private Integer slSizeVariable10;
    @Column(name = "SL_SIZE_VARIABLE_11")
    private Integer slSizeVariable11;
    @Column(name = "SL_SIZE_VARIABLE_12")
    private Integer slSizeVariable12;
    @Column(name = "TP_SIZE_VARIABLE_01")
    private Integer tpSizeVariable01;
    @Column(name = "TP_SIZE_VARIABLE_02")
    private Integer tpSizeVariable02;
    @Column(name = "TP_SIZE_VARIABLE_03")
    private Integer tpSizeVariable03;
    @Column(name = "TP_SIZE_VARIABLE_04")
    private Integer tpSizeVariable04;
    @Column(name = "TP_SIZE_VARIABLE_05")
    private Integer tpSizeVariable05;
    @Column(name = "TP_SIZE_VARIABLE_06")
    private Integer tpSizeVariable06;
    @Column(name = "TP_SIZE_VARIABLE_07")
    private Integer tpSizeVariable07;
    @Column(name = "TP_SIZE_VARIABLE_08")
    private Integer tpSizeVariable08;
    @Column(name = "TP_SIZE_VARIABLE_09")
    private Integer tpSizeVariable09;
    @Column(name = "TP_SIZE_VARIABLE_10")
    private Integer tpSizeVariable10;
    @Column(name = "TP_SIZE_VARIABLE_11")
    private Integer tpSizeVariable11;
    @Column(name = "TP_SIZE_VARIABLE_12")
    private Integer tpSizeVariable12;
}
