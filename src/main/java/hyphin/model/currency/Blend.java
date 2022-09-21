package hyphin.model.currency;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Data
@Entity
@Inheritance( strategy = InheritanceType.TABLE_PER_CLASS )
@NoArgsConstructor
@AllArgsConstructor
public class Blend {
    @Id
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
