package hyphin.model.currency;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "BLEND_STDDEV", schema="PUBLIC", catalog = "HYFIN")
@NoArgsConstructor
@AllArgsConstructor
public class StandardDeviation {

    @Id
    private Long id;

    @Column(name = "CCYPAIR")
    private String ccyPair;

    private String date;

    @Column(name = "RATETYPE")
    private String rateType;

    @Column(name = "POS001")
    private Double pos001;

    @Column(name = "POS002")
    private Double pos002;

    @Column(name = "POS003")
    private Double pos003;

    @Column(name = "POS004")
    private Double pos004;

    @Column(name = "POS005")
    private Double pos005;

    @Column(name = "POS006")
    private Double pos006;

    @Column(name = "POS007")
    private Double pos007;

    @Column(name = "POS008")
    private Double pos008;

    @Column(name = "POS009")
    private Double pos009;

    @Column(name = "POS010")
    private Double pos010;

    @Column(name = "POS011")
    private Double pos011;

    @Column(name = "POS012")
    private Double pos012;

    @Column(name = "POS013")
    private Double pos013;

    @Column(name = "POS014")
    private Double pos014;

    @Column(name = "POS015")
    private Double pos015;

    @Column(name = "POS016")
    private Double pos016;

    @Column(name = "POS017")
    private Double pos017;

    @Column(name = "POS018")
    private Double pos018;

    @Column(name = "POS019")
    private Double pos019;

    @Column(name = "POS020")
    private Double pos020;

    @Column(name = "POS021")
    private Double pos021;

    @Column(name = "POS022")
    private Double pos022;

    @Column(name = "POS023")
    private Double pos023;

    @Column(name = "ONE_MTH_STDDEV")
    private Double oneMthStdDev;

    @Column(name = "SPOTRATE")
    private Double spotRate;

    @Column(name = "FIVE_DAY_HI")
    private Double fiveDayHi;

    @Column(name = "FIVE_DAY_LO")
    private Double fiveDayLo;

    @Column(name = "ONE_MTH_HI")
    private Double oneMthHi;

    @Column(name = "ONE_MTH_LO")
    private Double oneMthLo;

    @Column(name = "ONE_MTH_RVOL")
    private Double oneMthRvol;
}
