package hyphin.model.currency;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "CURRENCYRATESBLEND_USDJPY", schema="PUBLIC", catalog = "HYFIN")
@DiscriminatorValue("3")
@NoArgsConstructor
public class BlendUsdJpy extends Blend{



}
