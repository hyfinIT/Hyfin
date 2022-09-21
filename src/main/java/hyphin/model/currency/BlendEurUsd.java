package hyphin.model.currency;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

@Entity
@Table(name = "CURRENCYRATESBLEND_EURUSD", schema="PUBLIC", catalog = "HYFIN")
@NoArgsConstructor
public class BlendEurUsd extends Blend{

}
