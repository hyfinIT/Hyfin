package hyphin.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "CCY_PAIR", schema = "PUBLIC", catalog = "HYFIN")
@Getter
@Setter
public class CcyPair {

    @Id
    private Long id;
    private String region;
    private String currencyPair;
    private String preferenceType;
    private Long frequencyTier;
    private String displayPriority;
    private String ccyPairNumber;
    private Double spread;
}
