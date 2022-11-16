package hyphin.dto.mappers;

import hyphin.dto.InflationRateDto;
import hyphin.model.InflationRate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface InflationRateMapper {

    @Mapping(source = "infRate", target = "infRate", qualifiedByName = "infRate")
    InflationRateDto mapToDto(InflationRate entity);
    InflationRate mapToEntity(InflationRate dto);

    @Named("infRate")
    static String infRate(Double param) {
        return param + "%";
    }
}
