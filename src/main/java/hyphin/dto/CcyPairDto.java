package hyphin.dto;

import lombok.Data;

@Data
public class CcyPairDto {

    private Long id;
    private String currencyPairFormatted;
    private String flagUrl1;
    private String flagUrl2;
    private String spotFxMidRate;

}
