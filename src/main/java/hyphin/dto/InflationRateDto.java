package hyphin.dto;

import lombok.Data;

import javax.persistence.Column;

@Data
public class InflationRateDto {

    private String infRate;
    private String infTrend;

}
