package hyphin.model.currency;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "CURRENCYRATESGOLDENSOURCE", schema="PUBLIC", catalog = "HYFIN")
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyExchangeRate {

    @Id
    private Long id;

    @Column(name = "CCYPAIR")
    private String ccyPair;

    private String date;
    private Double open;
    private Double high;
    private Double low;
    private Double close;

    @Column(name = "sourceRef")
    private String sourceRef;
}

//Columns are CCYPAIR, DATE, OPEN, HIGH, LOW, CLOSE, SOURCEREF
