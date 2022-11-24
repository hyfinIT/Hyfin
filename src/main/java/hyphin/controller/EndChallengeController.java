package hyphin.controller;

import hyphin.enums.Sentiment;
import hyphin.model.currency.CurrencyRatesBlend;
import hyphin.dto.EndChallengeTradeDto;
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
        mav.getModel().put("pairs", endChallengeService.getAllPairs(session));
        return mav;
    }

    @GetMapping("/chosen-pair")
    public ModelAndView chosenPair(HttpSession session, String chosenPair){
        endChallengeService.chosePair(session, chosenPair);
        ModelAndView modelAndView = HyfinUtils.modelAndView("ec-cfd-2");
        modelAndView.getModel().put("chosenPair", endChallengeService.getChosenPair(session));
        return modelAndView;
    }

    @GetMapping("/ec-cfd-3")
    public ModelAndView sentiment(HttpSession session){
        ModelAndView modelAndView = HyfinUtils.modelAndView("ec-cfd-3");

        modelAndView.getModel().put("chosenPair", endChallengeService.getChosenPair(session));
        modelAndView.getModel().put("ecStaticDataDaily", endChallengeService.getEcStaticDataDaily(session));
        return modelAndView;
    }

    @PostMapping("/send-trade")
    public ModelAndView sendTrade(HttpSession session, EndChallengeTradeDto endChallengeTradeDto){

        endChallengeService.choseSentiment(session, endChallengeTradeDto.getSentiment());

        if (endChallengeTradeDto.getCapitalPercent() == 8) {
            return redirectTo("ec-cfd-5b");
        }

        if (endChallengeTradeDto.getCapitalPercent() > 4) {
            return redirectTo("ec-cfd-5a");
        }

        if (endChallengeTradeDto.getSentiment().equals(Sentiment.BULLISH) && endChallengeTradeDto.getPrice() == 59) {
            return redirectTo("ec-cfd-5c");
        }

        endChallengeService.chooseCapitalPercent(session, endChallengeTradeDto);

        ModelAndView modelAndView = HyfinUtils.modelAndView("ec-cfd-4a");
        modelAndView.getModel().put("chosenPair", endChallengeService.getChosenPair(session));
        modelAndView.getModel().put("ecStaticDataDaily", endChallengeService.getEcStaticDataDaily(session));
        modelAndView.getModel().put("trade", endChallengeService.getTrade(session));
        modelAndView.getModel().put("amounts", endChallengeService.getEndChallengeSession(session).getAmounts());
        return modelAndView;

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
