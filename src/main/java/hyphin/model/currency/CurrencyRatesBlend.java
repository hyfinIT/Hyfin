package hyphin.model.currency;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "CURRENCYRATESBLEND", schema="PUBLIC", catalog = "HYFIN")
@NoArgsConstructor
public class CurrencyRatesBlend{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_cur_rates_blend")
    @SequenceGenerator(name = "id_cur_rates_blend", sequenceName = "CURRENCYRATESBLENDSEQUENCE")

    private Long id;

    @Column(name = "CCYPAIR")
    private String ccyPair;

    private String position;

    private String date;

    @Column(name = "BLENDOPEN")
    private Double blendOpen;

    @Column(name = "BLENDHIGH")
    private Double blendHigh;

    @Column(name = "BLENDLOW")
    private Double blendLow;

    @Column(name = "BLENDCLOSE")
    private Double blendClose;

    @Column(name = "DAILYRANGE")
    private Double dailyRange;

    @Column(name = "LOGCHANCE")
    private Double logChance;

    @Column(name = "SOURCEREF")
    private String sourceRef;

}
