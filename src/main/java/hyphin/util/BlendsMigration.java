package hyphin.util;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

//@Service
@FieldDefaults(makeFinal = true)
@RequiredArgsConstructor
public class BlendsMigration {

//    BlendEurUsdRepository blendEurUsdRepository;
//    BlendGbpUsdRepository blendGbpUsdRepository;
//    BlendUsdJpyRepository blendUsdJpyRepository;
//
//    CurrencyRatesBlendRepository currencyRatesBlendRepository;
//
//    @PostConstruct
//    public void migrate(){
//        System.out.println("Migrstart................................");
//
//        List<CurrencyRatesBlend> eurUsdBlendNew = blendEurUsdRepository.findAll()
//                .stream()
//                .map(this::map)
//                .collect(Collectors.toList());
//
//        List<CurrencyRatesBlend> gbpUsdNew = blendGbpUsdRepository.findAll()
//                .stream()
//                .map(this::map)
//                .collect(Collectors.toList());
//
//        List<CurrencyRatesBlend> usdJpyNew = blendUsdJpyRepository.findAll()
//                .stream()
//                .map(this::map)
//                .collect(Collectors.toList());
//
//        List<CurrencyRatesBlend> all = new ArrayList<>();
//
//        all.addAll(eurUsdBlendNew);
//        all.addAll(gbpUsdNew);
//        all.addAll(usdJpyNew);
//
//        System.out.println("MigBeforeSave................................");
//
//
//        all.forEach(currencyRatesBlendRepository::save);
//
//        System.out.println("MigAfterSave................................");
//    }
//
//    private CurrencyRatesBlend map(Blend blend) {
//        CurrencyRatesBlend currencyRatesBlend = new CurrencyRatesBlend();
//
//        currencyRatesBlend.setCcyPair(blend.getCcyPair());
//        currencyRatesBlend.setPosition(blend.getPosition());
//        currencyRatesBlend.setDate(blend.getDate());
//        currencyRatesBlend.setBlendOpen(blend.getBlendOpen());
//        currencyRatesBlend.setBlendHigh(blend.getBlendHigh());
//        currencyRatesBlend.setBlendLow(blend.getBlendLow());
//        currencyRatesBlend.setBlendClose(blend.getBlendClose());
//        currencyRatesBlend.setDailyRange(blend.getDailyRange());
//        currencyRatesBlend.setLogChance(blend.getLogChance());
//        currencyRatesBlend.setSourceRef(blend.getSourceRef());
//
//        return currencyRatesBlend;
//    }
}
