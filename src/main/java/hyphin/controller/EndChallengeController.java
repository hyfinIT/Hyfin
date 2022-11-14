package hyphin.controller;

import hyphin.model.currency.CurrencyRatesBlend;
import hyphin.model.endchallenge.EndChallengeTrade;
import hyphin.repository.currency.CurrencyRatesBlendRepository;
import hyphin.service.EndChallengeService;
import hyphin.util.HyfinUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
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

    @GetMapping("/ec-cfd-1")
    public ModelAndView viewCFD1(HttpSession session) {
        endChallengeService.start(session);
        ModelAndView mav = HyfinUtils.modelAndView("ec-cfd-1");
        mav.getModel().put("pairs", endChallengeService.getPairs(session));
        return mav;
    }

    @GetMapping("/chosen-pair")
    public ModelAndView chosenPair(HttpSession session, String pair){
        endChallengeService.chosePair(session, pair);
        ModelAndView modelAndView = HyfinUtils.modelAndView("ec-cfd-2");

        return modelAndView;
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
    public List<CurrencyRatesBlend> getChartData(
            @RequestParam String pair
    ){
        return endChallengeService.getChartData(pair);
    }

    public ModelAndView redirectTo(String pageTo) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName(pageTo);
        return mav;
    }
}
