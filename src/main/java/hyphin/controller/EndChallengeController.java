package hyphin.controller;

import hyphin.dto.CcyPairDto;
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
import java.util.Objects;

@RestController
@RequestMapping("end-challenge")
@RequiredArgsConstructor
public class EndChallengeController {

    private final CurrencyRatesBlendRepository currencyRatesBlendRepository;

    private final EndChallengeService endChallengeService;

    @GetMapping("/ready")
    public ModelAndView ready(HttpSession session){
        endChallengeService.start(session);
        return redirectTo("end_challenge/ready");
    }

    @GetMapping("/start")
    public ModelAndView start() {
        return redirectTo("end_challenge/start");
    }

    @GetMapping("/ec-cfd-1")
    public ModelAndView viewCFD1(HttpSession session) {
        ModelAndView mav = HyfinUtils.modelAndView("ec-cfd-1");
        mav.getModel().put("pairs", endChallengeService.getAllPairs(session));
        return mav;
    }

    @PostMapping("/event")
    public void event(HttpSession session, @RequestParam String stage) {
        System.out.println("event");
        System.out.println(stage);
        endChallengeService.addAudit(session, stage);
    }

    @GetMapping("/chosen-pair")
    public ModelAndView chosenPair(HttpSession session, String chosenPair) {

        List<CcyPairDto> allPairs = endChallengeService.getAllPairs(session);

        int pairNumber = 0;

        for (int i = 0; i < allPairs.size(); i++) {
            if (allPairs.get(i).getCurrencyPairFormatted().equalsIgnoreCase(chosenPair)) {
                pairNumber = i + 1;
            }
        }

        switch (pairNumber) {
            case 1:
                endChallengeService.addAudit(session, "D01");
                break;
            case 2:
                endChallengeService.addAudit(session, "D02");
                break;
            case 3:
                endChallengeService.addAudit(session, "D03");
                break;
        }

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

        if (endChallengeTradeDto.getSentiment().equals(Sentiment.BULLISH) && endChallengeTradeDto.getPrice().equalsIgnoreCase("BID")) {
            endChallengeService.addAudit(session, "Z02");
            ModelAndView modelAndView = HyfinUtils.modelAndView("ec-cfd-5c");
            modelAndView.getModel().put("var1", "bullish");
            modelAndView.getModel().put("var2", "sold");
            return modelAndView;
        }

        if (endChallengeTradeDto.getSentiment().equals(Sentiment.BEARISH) && endChallengeTradeDto.getPrice().equalsIgnoreCase("ASK")) {
            endChallengeService.addAudit(session, "Z02");
            ModelAndView modelAndView = HyfinUtils.modelAndView("ec-cfd-5c");
            modelAndView.getModel().put("var1", "bearish");
            modelAndView.getModel().put("var2", "bought");
            return modelAndView;
        }

        endChallengeService.chooseCapitalPercent(session, endChallengeTradeDto);

        ModelAndView modelAndView = HyfinUtils.modelAndView("ec-cfd-4a");
        modelAndView.getModel().put("chosenPair", endChallengeService.getChosenPair(session));
        modelAndView.getModel().put("ecStaticDataDaily", endChallengeService.getEcStaticDataDaily(session));
        modelAndView.getModel().put("trade", endChallengeService.getTrade(session));
        modelAndView.getModel().put("amounts", endChallengeService.getEndChallengeSession(session).getAmounts());
        return modelAndView;
    }

    @GetMapping("/ec-cfd-4a")
    public ModelAndView viewCFD4(HttpSession session) {
        ModelAndView modelAndView = HyfinUtils.modelAndView("ec-cfd-4a");
        modelAndView.getModel().put("chosenPair", endChallengeService.getChosenPair(session));
        modelAndView.getModel().put("ecStaticDataDaily", endChallengeService.getEcStaticDataDaily(session));
        modelAndView.getModel().put("trade", endChallengeService.getTrade(session));
        modelAndView.getModel().put("amounts", endChallengeService.getEndChallengeSession(session).getAmounts());
        return modelAndView;
    }

    @GetMapping("/ec-cfd-6_01")
    public ModelAndView stopLoss(HttpSession session) {
        ModelAndView modelAndView = HyfinUtils.modelAndView("ec-cfd-6_01");



        modelAndView.getModel().put("chosenPair", endChallengeService.getChosenPair(session));
        modelAndView.getModel().put("ecStaticDataDaily", endChallengeService.getEcStaticDataDaily(session));
        modelAndView.getModel().put("trade", endChallengeService.getTrade(session));
        modelAndView.getModel().put("amounts", endChallengeService.getEndChallengeSession(session).getAmounts());

        return modelAndView;
    }

    @PostMapping("/ec-cfd-6a_01")
    public ModelAndView takeProfit(Integer slOptionNumber, HttpSession session) {
        endChallengeService.setSlOptionNumber(slOptionNumber, session);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.getModel().put("chosenPair", endChallengeService.getChosenPair(session));
        modelAndView.getModel().put("ecStaticDataDaily", endChallengeService.getEcStaticDataDaily(session));
        modelAndView.getModel().put("trade", endChallengeService.getTrade(session));
        modelAndView.getModel().put("amounts", endChallengeService.getEndChallengeSession(session).getAmounts());

        modelAndView.setViewName("ec-cfd-6a_01");

        return modelAndView;
    }

    @GetMapping("/ec-cfd-6a_01")
    public ModelAndView getTakeProfit(HttpSession session) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.getModel().put("chosenPair", endChallengeService.getChosenPair(session));
        modelAndView.getModel().put("ecStaticDataDaily", endChallengeService.getEcStaticDataDaily(session));
        modelAndView.getModel().put("trade", endChallengeService.getTrade(session));
        modelAndView.getModel().put("amounts", endChallengeService.getEndChallengeSession(session).getAmounts());
        modelAndView.setViewName("ec-cfd-6a_01");
        return modelAndView;
    }

    @PostMapping("/ec-cfd-4c_01")
    public ModelAndView viewCFD4c01(Integer tpOptionNumber, HttpSession session) {
        endChallengeService.setTpOptionNumber(tpOptionNumber, session);
        endChallengeService.calculateOcoTermAmounts(session);
        ModelAndView modelAndView = HyfinUtils.modelAndView("ec-cfd-4c_01");
        modelAndView.getModel().put("chosenPair", endChallengeService.getChosenPair(session));
        modelAndView.getModel().put("ecStaticDataDaily", endChallengeService.getEcStaticDataDaily(session));
        modelAndView.getModel().put("trade", endChallengeService.getTrade(session));
        modelAndView.getModel().put("amounts", endChallengeService.getEndChallengeSession(session).getAmounts());
        modelAndView.getModel().put("sl", endChallengeService.getEndChallengeSession(session).getSlOptionNumber());
        modelAndView.getModel().put("tp", endChallengeService.getEndChallengeSession(session).getTpOptionNumber());

        String orderType = "buy";

        if (endChallengeService.getEndChallengeSession(session).getSentiment().equals(Sentiment.BEARISH)) {
            orderType = "sell";
        }

        modelAndView.getModel().put("orderType", orderType);
        return modelAndView;
    }

    @GetMapping("/ec-cfd-7a_01")
    public ModelAndView ECCFD7a01(HttpSession session) {
        endChallengeService.calculateTradeResult(session);
        ModelAndView mav = HyfinUtils.modelAndView("ec-cfd-7a_01");
        mav.getModel().put("tradeResult", endChallengeService.getEndChallengeSession(session).getTradeResult());

        return mav;
    }

    @GetMapping("/ec-cfd-7b_01")
    public ModelAndView viewECCFD7b01(HttpSession session) {
        endChallengeService.calculateTradeResult(session);
        ModelAndView mav = HyfinUtils.modelAndView("ec-cfd-7b_01");
        mav.getModel().put("tradeResult", endChallengeService.getEndChallengeSession(session).getTradeResult());

        return mav;
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
