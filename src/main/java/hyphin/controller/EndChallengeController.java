package hyphin.controller;

import hyphin.model.currency.Blend;
import hyphin.model.currency.CurrencyRatesBlend;
import hyphin.model.endchallenge.EndChallengeTrade;
import hyphin.repository.currency.CurrencyRatesBlendRepository;
import hyphin.repository.currency.CurrencyRatesBlendRepository;
import hyphin.repository.currency.CurrencyRatesBlendRepository;
import hyphin.service.EndChallengeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
@RequestMapping("end-challenge")
@RequiredArgsConstructor
public class EndChallengeController {

    private final CurrencyRatesBlendRepository currencyRatesBlendRepository;

    private final EndChallengeService endChallengeService;

    @GetMapping("/ready")
    public ModelAndView ready(){
        return redirectTo("end_challenge/ready");
    }

    @GetMapping("/start")
    public ModelAndView start() {
        return redirectTo("end_challenge/start");
    }

    @PostMapping("/send-trade")
    public ModelAndView sendTrade(EndChallengeTrade endChallengeTrade){

        System.out.println("Hey");
        System.out.println(endChallengeTrade);

        if (endChallengeTrade.getCapitalPercent() == 100) {
            return redirectTo("ec-cfd-5b");
        }

        if (endChallengeTrade.getCapitalPercent() > 20) {
            return redirectTo("ec-cfd-5a");
        }

        if (endChallengeTrade.getDirection().equalsIgnoreCase("Bullish") && endChallengeTrade.getPrice() == 59) {
            return redirectTo("ec-cfd-5c");
        }

        return redirectTo("ec-cfd-4a");
    }

    @GetMapping("/get-chart-data")
    public List<CurrencyRatesBlend> getChartData(){
        return endChallengeService.getChartData("EUR/USD");
    }

    public ModelAndView redirectTo(String pageTo) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName(pageTo);
        return mav;
    }
}
