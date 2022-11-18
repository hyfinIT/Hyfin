package hyphin.dto;

import hyphin.model.Atmospherics;
import lombok.Data;

@Data
public class CcyPairDto {

    private Long id;
    private String currencyPair;
    private String currencyPairFormatted;
    private String spotFxMidRate;
    private CurrencyDto currency1;
    private CurrencyDto currency2;

}
