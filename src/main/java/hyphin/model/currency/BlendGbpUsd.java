package hyphin.model.currency;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "CURRENCYRATESBLEND_GBPUSD", schema="PUBLIC", catalog = "HYFIN")
@NoArgsConstructor
public class BlendGbpUsd extends Blend {

}
