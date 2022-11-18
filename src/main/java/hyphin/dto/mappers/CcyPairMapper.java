package hyphin.dto.mappers;

import hyphin.dto.AtmosphericsDto;
import hyphin.dto.CcyPairDto;
import hyphin.dto.InflationRateDto;
import hyphin.model.Atmospherics;
import hyphin.model.CcyPair;
import hyphin.model.InflationRate;
import hyphin.model.currency.CurrencyRatesBlend;
import hyphin.repository.AtmosphericsRepository;
import hyphin.repository.FlagIconRepository;
import hyphin.repository.InflationRateRepository;
import hyphin.repository.currency.CurrencyRatesBlendRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class CcyPairMapper {

    @Autowired
    protected FlagIconRepository flagIconRepository;
    @Autowired
    protected AtmosphericsRepository atmosphericsRepository;
    @Autowired
    protected InflationRateRepository inflationRateRepository;

    @Autowired
    InflationRateMapper inflationRateMapper;

    @Autowired
    protected CurrencyRatesBlendRepository currencyRatesBlendRepository;

    @Mapping(source = "currencyPair", target = "currency1.flagUrl", qualifiedByName = "flag1")
    @Mapping(source = "currencyPair", target = "currency2.flagUrl", qualifiedByName = "flag2")
    @Mapping(source = "currencyPair", target = "currency1.country", qualifiedByName = "country1")
    @Mapping(source = "currencyPair", target = "currency2.country", qualifiedByName = "country2")
    @Mapping(source = "currencyPair", target = "currency1.inflationRate", qualifiedByName = "inflationRate1")
    @Mapping(source = "currencyPair", target = "currency2.inflationRate", qualifiedByName = "inflationRate2")
    @Mapping(source = "currencyPair", target = "currency1.atmospherics", qualifiedByName = "atmospherics1")
    @Mapping(source = "currencyPair", target = "currency2.atmospherics", qualifiedByName = "atmospherics2")
    @Mapping(source = "currencyPair", target = "currency1.name", qualifiedByName = "currency1Name")
    @Mapping(source = "currencyPair", target = "currency2.name", qualifiedByName = "currency2Name")
    @Mapping(source = "currencyPair", target = "currencyPairFormatted", qualifiedByName = "currencyPairFormatted")
    @Mapping(source = "currencyPair", target = "spotFxMidRate", qualifiedByName = "spotFxMidRate")
    public abstract CcyPairDto mapToDto(CcyPair entity);

    public abstract CcyPair mapToEntity(CcyPairDto dto);

    @Named("flag1")
    String flag1(String currencyPair) {
        return flagIconRepository.getByCcyMnemonic(currencyPair.substring(0, 3)).getFlagFile();
    }

    @Named("flag2")
    String flag2(String currencyPair) {
        return flagIconRepository.getByCcyMnemonic(currencyPair.substring(3, 6)).getFlagFile();
    }

    @Named("country1")
    String country1(String currencyPair) {
        return flagIconRepository.getByCcyMnemonic(currencyPair.substring(0, 3)).getCountry();
    }

    @Named("country2")
    String country2(String currencyPair) {
        return flagIconRepository.getByCcyMnemonic(currencyPair.substring(3, 6)).getCountry();
    }

    @Named("currency1Name")
    String currency1Name(String currencyPair) {
        return currencyPair.substring(0, 3);
    }

    @Named("currency2Name")
    String currency2Name(String currencyPair) {
        return currencyPair.substring(3, 6);
    }

    @Named("atmospherics1")
    AtmosphericsDto atmospherics1(String currencyPair) {
        List<Atmospherics> atmosphericsList = atmosphericsRepository.getAllByCcyAndStage(currencyPair.substring(0, 3), "STG001");

        AtmosphericsDto atmosphericsDto = new AtmosphericsDto();
        atmosphericsDto.setDataRelease(atmosphericsList.stream().filter(atmospherics -> atmospherics.getCategory().equals("Data")).findAny().get().getItem());
        atmosphericsDto.setCentralBank(atmosphericsList.stream().filter(atmospherics -> atmospherics.getCategory().equals("Central Bank")).findAny().get().getItem());
        atmosphericsDto.setMacroPolitics(atmosphericsList.stream().filter(atmospherics -> atmospherics.getCategory().equals("Macro Politics")).findAny().get().getItem());

        return atmosphericsDto;
    }

    @Named("atmospherics2")
    AtmosphericsDto atmospherics2(String currencyPair) {
        List<Atmospherics> atmosphericsList = atmosphericsRepository.getAllByCcyAndStage(currencyPair.substring(3, 6), "STG001");

        AtmosphericsDto atmosphericsDto = new AtmosphericsDto();
        atmosphericsDto.setDataRelease(atmosphericsList.stream().filter(atmospherics -> atmospherics.getCategory().equals("Data")).findAny().get().getItem());
        atmosphericsDto.setCentralBank(atmosphericsList.stream().filter(atmospherics -> atmospherics.getCategory().equals("Central Bank")).findAny().get().getItem());
        atmosphericsDto.setMacroPolitics(atmosphericsList.stream().filter(atmospherics -> atmospherics.getCategory().equals("Macro Politics")).findAny().get().getItem());

        return atmosphericsDto;
    }

    @Named("inflationRate1")
    InflationRateDto inflationRate1(String currencyPair) {
        InflationRate inflationRate1 = inflationRateRepository.getAllByPosition(currencyPair.substring(0, 3) + "01")
                .stream().min(Comparator.comparing(InflationRate::getTime))
                .orElse(null);

        return inflationRateMapper.mapToDto(inflationRate1);
    }

    @Named("inflationRate2")
    InflationRateDto inflationRate2(String currencyPair) {
        InflationRate inflationRate1 = inflationRateRepository.getAllByPosition(currencyPair.substring(3, 6) + "01")
                .stream().min(Comparator.comparing(InflationRate::getTime))
                .orElse(null);

        return inflationRateMapper.mapToDto(inflationRate1);
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
