package hyphin.dto.mappers;


import hyphin.dto.AtmosphericsDto;
import hyphin.model.Atmospherics;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AtmosphericsMapper {

    AtmosphericsDto mapToDto(Atmospherics entity);
    Atmospherics mapToEntity(AtmosphericsDto dto);

}
