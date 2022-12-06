package hyphin.dto.mappers;

import hyphin.dto.EcStaticDataDailyDto;
import hyphin.model.EcStaticDataDaily;
import hyphin.util.HyfinUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

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
    @Mapping(source = "maxPosSizeBase", target = "maxPosSizeBase", qualifiedByName = "toMoneyWithZeros")
    @Mapping(source = "maxPosSizeTerm", target = "maxPosSizeTerm", qualifiedByName = "toMoneyWithZeros")
    @Mapping(source = "userMargin", target = "userMargin", qualifiedByName = "toMoneyWithZeros")

    @Mapping(source = "posSize01", target = "posSize01Formatted", qualifiedByName = "toMoney")
    @Mapping(source = "posSize02", target = "posSize02Formatted", qualifiedByName = "toMoney")
    @Mapping(source = "posSize03", target = "posSize03Formatted", qualifiedByName = "toMoney")
    @Mapping(source = "posSize04", target = "posSize04Formatted", qualifiedByName = "toMoney")
    @Mapping(source = "posSize05", target = "posSize05Formatted", qualifiedByName = "toMoney")
    @Mapping(source = "posSize06", target = "posSize06Formatted", qualifiedByName = "toMoney")
    @Mapping(source = "posSize07", target = "posSize07Formatted", qualifiedByName = "toMoney")
    @Mapping(source = "posSize08", target = "posSize08Formatted", qualifiedByName = "toMoney")


    @Mapping(source = "bidPosSize01", target = "bidPosSize01Formatted", qualifiedByName = "toMoneyDropCents")
    @Mapping(source = "bidPosSize02", target = "bidPosSize02Formatted", qualifiedByName = "toMoneyDropCents")
    @Mapping(source = "bidPosSize03", target = "bidPosSize03Formatted", qualifiedByName = "toMoneyDropCents")
    @Mapping(source = "bidPosSize04", target = "bidPosSize04Formatted", qualifiedByName = "toMoneyDropCents")
    @Mapping(source = "bidPosSize05", target = "bidPosSize05Formatted", qualifiedByName = "toMoneyDropCents")
    @Mapping(source = "bidPosSize06", target = "bidPosSize06Formatted", qualifiedByName = "toMoneyDropCents")
    @Mapping(source = "bidPosSize07", target = "bidPosSize07Formatted", qualifiedByName = "toMoneyDropCents")
    @Mapping(source = "bidPosSize08", target = "bidPosSize08Formatted", qualifiedByName = "toMoneyDropCents")

    @Mapping(source = "askPosSize01", target = "askPosSize01Formatted", qualifiedByName = "toMoneyDropCents")
    @Mapping(source = "askPosSize02", target = "askPosSize02Formatted", qualifiedByName = "toMoneyDropCents")
    @Mapping(source = "askPosSize03", target = "askPosSize03Formatted", qualifiedByName = "toMoneyDropCents")
    @Mapping(source = "askPosSize04", target = "askPosSize04Formatted", qualifiedByName = "toMoneyDropCents")
    @Mapping(source = "askPosSize05", target = "askPosSize05Formatted", qualifiedByName = "toMoneyDropCents")
    @Mapping(source = "askPosSize06", target = "askPosSize06Formatted", qualifiedByName = "toMoneyDropCents")
    @Mapping(source = "askPosSize07", target = "askPosSize07Formatted", qualifiedByName = "toMoneyDropCents")
    @Mapping(source = "askPosSize08", target = "askPosSize08Formatted", qualifiedByName = "toMoneyDropCents")
    @Mapping(source = "ccyPairMid", target = "ccyPairMid", qualifiedByName = "ccyPairMid")
    @Mapping(source = "ccyPairPartRate", target = "ccyPairPartRateFormatted", qualifiedByName = "ccyPairPartRateFormatted")


    EcStaticDataDailyDto mapToDto(EcStaticDataDaily entity);

    @Named("toPercentString")
    static String toPercentString(Double d) {
        return HyfinUtils.toPercentage(d);
    }

    @Named("toMoney")
    static String toMoney(Double d) {
        return HyfinUtils.formatDecimalToMoneyWithoutZeros(d);
    }
    @Named("toMoneyWithZeros")
    static String toMoneyWithZeros(Double d) {
        return HyfinUtils.formatDecimalToMoney(d);
    }
    @Named("toMoneyDropCents")
    static String toMoneyDropCents(Double d) {
        return HyfinUtils.dropCents(HyfinUtils.formatDecimalToMoney(d));
    }
    @Named("ccyPairMid")
    static String ccyPairMid(Double d) {
        return HyfinUtils.formatDecimal(d);
    }
    @Named("ccyPairPartRateFormatted")
    static String ccyPairPartRateFormatted(Double d) {
        return HyfinUtils.formatDecimal(d, 2);
    }
}
