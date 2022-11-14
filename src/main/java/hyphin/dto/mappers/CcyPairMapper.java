package hyphin.dto.mappers;

import hyphin.dto.CcyPairDto;
import hyphin.model.CcyPair;
import hyphin.model.currency.CurrencyRatesBlend;
import hyphin.repository.FlagIconRepository;
import hyphin.repository.currency.CurrencyRatesBlendRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class CcyPairMapper {

    @Autowired
    protected FlagIconRepository flagIconRepository;

    @Autowired
    protected CurrencyRatesBlendRepository currencyRatesBlendRepository;

    @Mapping(source = "currencyPair", target = "flagUrl1", qualifiedByName = "flag1")
    @Mapping(source = "currencyPair", target = "flagUrl2", qualifiedByName = "flag2")
    @Mapping(source = "currencyPair", target = "currencyPairFormatted", qualifiedByName = "currencyPairFormatted")
    @Mapping(source = "currencyPair", target = "spotFxMidRate", qualifiedByName = "spotFxMidRate")
    public abstract CcyPairDto mapToDto(CcyPair ccyPair);

    public abstract CcyPair mapToEntity(CcyPairDto ccyPairDto);

    @Named("flag1")
    String flag1(String currencyPair) {
        return flagIconRepository.getByCcyMnemonic(currencyPair.substring(0, 3)).getFlagFile();
    }

    @Named("flag2")
    String flag2(String currencyPair) {
        return flagIconRepository.getByCcyMnemonic(currencyPair.substring(3, 6)).getFlagFile();
    }

    @Named("currencyPairFormatted")
    String currencyPairFormatted(String currencyPair) {
        return currencyPair.substring(0, 3) + "/" + currencyPair.substring(3, 6);
    }

    @Named("spotFxMidRate")
    String spotFxMidRate(String currencyPair) {
        CurrencyRatesBlend blend = null;
        switch (currencyPair) {
            case "EURUSD": {
                blend = currencyRatesBlendRepository.findByPositionAndCcyPair("001", "EUR/USD");
                break;
            }
            case "GBPUSD": {
                blend = currencyRatesBlendRepository.findByPositionAndCcyPair("001", "GBP/USD");
                break;
            }
            case "USDJPY": {
                blend = currencyRatesBlendRepository.findByPositionAndCcyPair("001", "USD/JPY");
                break;
            }
        }
        return Objects.nonNull(blend) ? Double.toString((blend.getBlendLow() + blend.getBlendHigh()) / 2).substring(0, 6) : "error";
    }
}
