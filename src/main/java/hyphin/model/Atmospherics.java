package hyphin.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ATMOSPHERICS", schema = "PUBLIC", catalog = "HYFIN")
@Getter
@Setter
public class Atmospherics {

    @Id
    private Long id;
    private String stage;
    private String ccy;
    private String category;
    private String catTag;
    private String item;
    private String priortrend;
    private Integer trendScore;
    private Integer cTrendScore;
    private Integer year;
    private Integer month;
    private String version;
    private String reference;
}
