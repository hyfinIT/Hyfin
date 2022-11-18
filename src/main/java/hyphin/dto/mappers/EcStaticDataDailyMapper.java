package hyphin.dto.mappers;

import hyphin.dto.EcStaticDataDailyDto;
import hyphin.model.EcStaticDataDaily;
import hyphin.model.InflationRate;
import hyphin.util.HyfinUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.text.DecimalFormat;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EcStaticDataDailyMapper {

    @Mapping(source = "mrgSizeVariable01", target = "mrgSizeVariable01", qualifiedByName = "toPercentString")
    @Mapping(source = "mrgSizeVariable02", target = "mrgSizeVariable02", qualifiedByName = "toPercentString")
    @Mapping(source = "mrgSizeVariable03", target = "mrgSizeVariable03", qualifiedByName = "toPercentString")
    @Mapping(source = "mrgSizeVariable04", target = "mrgSizeVariable04", qualifiedByName = "toPercentString")
    @Mapping(source = "mrgSizeVariable05", target = "mrgSizeVariable05", qualifiedByName = "toPercentString")
    @Mapping(source = "mrgSizeVariable06", target = "mrgSizeVariable06", qualifiedByName = "toPercentString")
    @Mapping(source = "mrgSizeVariable07", target = "mrgSizeVariable07", qualifiedByName = "toPercentString")
    @Mapping(source = "mrgSizeVariable08", target = "mrgSizeVariable08", qualifiedByName = "toPercentString")
    @Mapping(source = "maxPosSizeBase", target = "maxPosSizeBase", qualifiedByName = "trimDigits")
    @Mapping(source = "maxPosSizeTerm", target = "maxPosSizeTerm", qualifiedByName = "trimDigits")
    @Mapping(source = "userMargin", target = "userMargin", qualifiedByName = "trimDigits")
    EcStaticDataDailyDto mapToDto(EcStaticDataDaily entity);

    @Named("toPercentString")
    static String toPercentString(Double d) {
        return HyfinUtils.toPercentage(d);
    }
    @Named("trimDigits")
    static String trimDigits(Double d) {
        return HyfinUtils.formatDecimal(d);
    }
}
