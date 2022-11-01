package hyphin.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "INFLATION_RATE", schema = "PUBLIC", catalog = "HYFIN")
@Getter
@Setter
public class InflationRate {

    @Id
    private Long id;
    private String position;
    private String location;
    private String indicator;
    private String subject;
    private String measure;
    private String frequency;
    private String time;
    @Column(name = "INF_RATE")
    private Double infRate;
    @Column(name = "INF_TREND")
    private String infTrend;
}
