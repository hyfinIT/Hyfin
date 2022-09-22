package hyphin.service;

import hyphin.model.currency.Blend;
import hyphin.model.currency.BlendEurUsd;
import hyphin.model.currency.BlendGbpUsd;
import hyphin.model.currency.BlendUsdJpy;
import hyphin.model.currency.CurrencyExchangeRate;
import hyphin.model.currency.OperationAudit;
import hyphin.repository.currency.BlendEurUsdRepository;
import hyphin.repository.currency.BlendGbpUsdRepository;
import hyphin.repository.currency.BlendUsdJpyRepository;
import hyphin.repository.currency.CurrencyExchangeRateRepository;
import hyphin.repository.currency.OperationAuditRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CurrencyBlendService {

    private final CurrencyExchangeRateRepository currencyExchangeRateRepository;
    private final OperationAuditRepository operationAuditRepository;
    private final BlendEurUsdRepository blendEurUsdRepository;
    private final BlendGbpUsdRepository blendGbpUsdRepository;
    private final BlendUsdJpyRepository blendUsdJpyRepository;

    @Scheduled(cron = "0 5 1 * * *")
    @Transactional
    public void produceBlends(){
        log.info("Blending..................");
        OperationAudit operationAudit = new OperationAudit();
        operationAudit.setId(operationAuditRepository.maxId().orElse(0L) + 1L);
        operationAudit.setName("Currency blends creation");
        operationAudit.setDateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        List<CurrencyExchangeRate> yesterdayEntries = currencyExchangeRateRepository.getEntriesByDate(LocalDate.now().minusDays(1L).toString());

        if (yesterdayEntries.size() < 6) {
            operationAudit.setStatus("FAIL. (NO DATA FOR BLENDING)");
            operationAuditRepository.save(operationAudit);
        }

        try {
            if (!blendEurUsdRepository.findByDate(LocalDate.now().toString()).isPresent()) {
                eurUsdProcess(yesterdayEntries);
            }
            if (!blendGbpUsdRepository.findByDate(LocalDate.now().toString()).isPresent()) {
                gbpUsdProcess(yesterdayEntries);
            }
            if (!blendUsdJpyRepository.findByDate(LocalDate.now().toString()).isPresent()) {
                usdJpyProcess(yesterdayEntries);
            }
            operationAudit.setStatus("SUCCESS");
            operationAuditRepository.save(operationAudit);
        } catch (Exception e) {
            e.printStackTrace();
            operationAudit.setStatus("FAIL");
            operationAuditRepository.save(operationAudit);
        }
    }

    private void eurUsdProcess(List<CurrencyExchangeRate> yesterdayEntries) {
        List<CurrencyExchangeRate> todayEurUsdList = yesterdayEntries
                .stream()
                .filter(currencyExchangeRate -> currencyExchangeRate.getCcyPair().equals("EUR/USD"))
                .collect(Collectors.toList());

        List<? extends Blend> eurUsdBlends = blendEurUsdRepository.findAll();

        Blend yesterdayBlend = null;

        for (Blend eurUsdBlend : eurUsdBlends) {
            if (eurUsdBlend.getPosition().equals("030")) {
                blendEurUsdRepository.delete(eurUsdBlend.getId());
            }
            if (eurUsdBlend.getPosition().equals("001")) {
                yesterdayBlend = eurUsdBlend;
            }
        }

        List<BlendEurUsd> shiftedBlends = (List<BlendEurUsd>)shiftPositions(eurUsdBlends);

        Blend blendEurUsd = new BlendEurUsd();
        blendEurUsd.setCcyPair("EUR/USD");
        blendEurUsd.setId(blendEurUsdRepository.maxId().orElse(0L) + 1);
        blendEurUsd.setDate(LocalDate.now().toString());
        blendEurUsd.setPosition("001");
        blendEurUsd.setBlendOpen((todayEurUsdList.get(0).getOpen() + todayEurUsdList.get(1).getOpen()) / 2);
        blendEurUsd.setBlendHigh((todayEurUsdList.get(0).getHigh() + todayEurUsdList.get(1).getHigh()) / 2);
        blendEurUsd.setBlendLow((todayEurUsdList.get(0).getLow() + todayEurUsdList.get(1).getLow()) / 2);
        blendEurUsd.setBlendClose((todayEurUsdList.get(0).getClose() + todayEurUsdList.get(1).getClose()) / 2);
        blendEurUsd.setDailyRange(blendEurUsd.getBlendHigh() - blendEurUsd.getBlendLow());
        blendEurUsd.setSourceRef("blend");

        if (Objects.nonNull(yesterdayBlend)) {
            blendEurUsd.setLogChance(Math.log(blendEurUsd.getBlendClose() / yesterdayBlend.getBlendClose()));
        }

        shiftedBlends.add((BlendEurUsd) blendEurUsd);

        blendEurUsdRepository.save(shiftedBlends);
    }

    private void gbpUsdProcess(List<CurrencyExchangeRate> yesterdayEntries) {
        List<CurrencyExchangeRate> todayList = yesterdayEntries
                .stream()
                .filter(currencyExchangeRate -> currencyExchangeRate.getCcyPair().equals("GBP/USD"))
                .collect(Collectors.toList());

        List<? extends Blend> gbpUsdBlends = blendGbpUsdRepository.findAll();

        Blend yesterdayBlend = null;

        for (Blend gbpUsdBlend : gbpUsdBlends) {
            if (gbpUsdBlend.getPosition().equals("030")) {
                blendGbpUsdRepository.delete(gbpUsdBlend.getId());
            }
            if (gbpUsdBlend.getPosition().equals("001")) {
                yesterdayBlend = gbpUsdBlend;
            }
        }

        List<BlendGbpUsd> shiftedBlends = (List<BlendGbpUsd>)shiftPositions(gbpUsdBlends);

        Blend blend = new BlendGbpUsd();
        blend.setCcyPair("GBP/USD");
        blend.setId(blendGbpUsdRepository.maxId().orElse(0L) + 1);
        blend.setDate(LocalDate.now().toString());
        blend.setPosition("001");
        blend.setBlendOpen((todayList.get(0).getOpen() + todayList.get(1).getOpen()) / 2);
        blend.setBlendHigh((todayList.get(0).getHigh() + todayList.get(1).getHigh()) / 2);
        blend.setBlendLow((todayList.get(0).getLow() + todayList.get(1).getLow()) / 2);
        blend.setBlendClose((todayList.get(0).getClose() + todayList.get(1).getClose()) / 2);
        blend.setDailyRange(blend.getBlendHigh() - blend.getBlendLow());
        blend.setSourceRef("blend");

        if (Objects.nonNull(yesterdayBlend)) {
            blend.setLogChance(Math.log(blend.getBlendClose() / yesterdayBlend.getBlendClose()));
        }

        shiftedBlends.add((BlendGbpUsd) blend);

        blendGbpUsdRepository.save(shiftedBlends);
    }

    private void usdJpyProcess(List<CurrencyExchangeRate> yesterdayEntries) {
        List<CurrencyExchangeRate> todayList = yesterdayEntries
                .stream()
                .filter(currencyExchangeRate -> currencyExchangeRate.getCcyPair().equals("USD/JPY"))
                .collect(Collectors.toList());

        List<? extends Blend> usdJpyBlends = blendUsdJpyRepository.findAll();

        Blend yesterdayBlend = null;

        for (Blend usdJpyBlend : usdJpyBlends) {
            if (usdJpyBlend.getPosition().equals("030")) {
                blendUsdJpyRepository.delete(usdJpyBlend.getId());
            }
            if (usdJpyBlend.getPosition().equals("001")) {
                yesterdayBlend = usdJpyBlend;
            }
        }

        List<BlendUsdJpy> shiftedBlends = (List<BlendUsdJpy>)shiftPositions(usdJpyBlends);

        Blend blendUsdJpy = new BlendUsdJpy();
        blendUsdJpy.setCcyPair("USD/JPY");
        blendUsdJpy.setId(blendUsdJpyRepository.maxId().orElse(0L) + 1);
        blendUsdJpy.setDate(LocalDate.now().toString());
        blendUsdJpy.setPosition("001");
        blendUsdJpy.setBlendOpen((todayList.get(0).getOpen() + todayList.get(1).getOpen()) / 2);
        blendUsdJpy.setBlendHigh((todayList.get(0).getHigh() + todayList.get(1).getHigh()) / 2);
        blendUsdJpy.setBlendLow((todayList.get(0).getLow() + todayList.get(1).getLow()) / 2);
        blendUsdJpy.setBlendClose((todayList.get(0).getClose() + todayList.get(1).getClose()) / 2);
        blendUsdJpy.setDailyRange(blendUsdJpy.getBlendHigh() - blendUsdJpy.getBlendLow());
        blendUsdJpy.setSourceRef("blend");

        if (Objects.nonNull(yesterdayBlend)) {
            blendUsdJpy.setLogChance(Math.log(blendUsdJpy.getBlendClose() / yesterdayBlend.getBlendClose()));
        }

        shiftedBlends.add((BlendUsdJpy) blendUsdJpy);

        blendUsdJpyRepository.save(shiftedBlends);
    }

    private List<? extends Blend> shiftPositions(List<? extends Blend> blendsList){
        blendsList.sort(Comparator.comparing(Blend::getPosition));

        for (int i = 0; i < blendsList.size(); i++) {
            String newPosition = String.valueOf(Integer.parseInt(blendsList.get(i).getPosition()) + 1);

            if (newPosition.length() == 1) {
                newPosition = "00" + newPosition;
            } else if (newPosition.length() == 2) {
                newPosition = "0" + newPosition;
            }

            blendsList.get(i).setPosition(newPosition);
        }

        blendsList.removeIf(element -> element.getPosition().equals("031"));

        return blendsList;
    }
}
