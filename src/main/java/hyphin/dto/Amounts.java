package hyphin.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Amounts {
    String eur;
    String usd;
    String gbp;
    String jpy;
    String baseAmount;
    String termAmountSl;
    String termAmountTp;
}
