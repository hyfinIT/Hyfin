package hyphin.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Trade {

    String id;

    String eur;
    String usd;
    String gbp;
    String jpy;

    Double tradedRate;
    Double currentMidRate;
    Double pl;

    String allocatedMargin;

}
