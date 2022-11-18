package hyphin.dto;

import lombok.Data;

@Data
public class CurrencyDto {

        private String flagUrl;
        private String country;
        private String name;
        private AtmosphericsDto atmospherics;
        private InflationRateDto inflationRate;

}
