package hyphin.service;

import hyphin.model.currency.Blend;
import hyphin.repository.currency.BlendEurUsdRepository;
import hyphin.repository.currency.BlendGbpUsdRepository;
import hyphin.repository.currency.BlendUsdJpyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EndChallengeService {

    private final BlendEurUsdRepository blendEurUsdRepository;
    private final BlendGbpUsdRepository blendGbpUsdRepository;
    private final BlendUsdJpyRepository blendUsdJpyRepository;

    NumberFormat formatter = new DecimalFormat("#0.00");

    public List<? extends Blend> getChartData() {
        return blendEurUsdRepository.findAll().stream().map(blend -> {
            blend.setBlendOpen(trimDigits(blend.getBlendOpen()));
            blend.setBlendHigh(trimDigits(blend.getBlendHigh()));
            blend.setBlendLow(trimDigits(blend.getBlendLow()));
            blend.setBlendClose(trimDigits(blend.getBlendClose()));

            return blend;
        }).collect(Collectors.toList());
    }

    private Double trimDigits(Double arg) {
        if (Objects.nonNull(arg)) {
            return Math.floor(arg * 10000) / 10000;
        }

        return arg;
    }

}
