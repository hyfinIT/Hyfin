package hyphin.service;

import hyphin.dto.*;
import hyphin.dto.mappers.CcyPairMapper;
import hyphin.dto.mappers.EcStaticDataDailyMapper;
import hyphin.enums.Sentiment;
import hyphin.model.CcyPair;
import hyphin.model.User;
import hyphin.model.currency.CurrencyRatesBlend;
import hyphin.model.endchallenge.EndChallengeSession;
import hyphin.repository.CcyPairRepository;
import hyphin.repository.EcStaticDataDailyRepository;
import hyphin.repository.UserAuditRepository;
import hyphin.repository.currency.CurrencyRatesBlendRepository;
import hyphin.util.HyfinUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EndChallengeService {

    private final CurrencyRatesBlendRepository currencyRatesBlendRepository;

    public List<CurrencyRatesBlend> getChartData(String pair) {
        return currencyRatesBlendRepository.findAll().stream()
                .filter(currencyRatesBlend -> currencyRatesBlend.getCcyPair().equals(pair))
                .map(blend -> {
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

    private final UserAuditRepository userAuditRepository;
    private final CcyPairRepository ccyPairRepository;
    private final EcStaticDataDailyRepository ecStaticDataDailyRepository;


    private final CcyPairMapper ccyPairMapper;
    private final EcStaticDataDailyMapper ecStaticDataDailyMapper;

    private static final SimpleDateFormat jdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    private static final String END_CHALLENGE_ELEMENT_ID = "?";
    private static final long SESSION_LIVE_TIME = 1000 * 60 * 60;
    private static final long GAME_TIMEOUT_MILLIS = 1000 * 45;
    private ConcurrentHashMap<String, EndChallengeSession> sessions = new ConcurrentHashMap<>();
    List<CcyPairDto> allPairs = new ArrayList<>();

//    @PostConstruct
//    private void init(){
//        System.out.println("initPairs.....");
//        allPairs = StreamSupport.stream(ccyPairRepository.findAll().spliterator(), false)
//                .map(ccyPairMapper::mapToDto).collect(Collectors.toList());
//        System.out.println("initPairs..... finished");
//    }

    public void start(HttpSession session) {
        User user = (User) session.getAttribute("User-entity");
        List<CcyPair> ccyPairs = ccyPairRepository.getCcyPairsByUserParams(user.getRegion(), user.getPreferenceType(), user.getDisplayPriority());

        List<CcyPairDto> collect = ccyPairs
                .stream()
                .map(ccyPairMapper::mapToDto)
                .collect(Collectors.toList());

        String endChallengeSessionId = session.getId() + System.currentTimeMillis();

        session.setAttribute("end-challenge-session-id", endChallengeSessionId);
        EndChallengeSession endChallengeSession = new EndChallengeSession(endChallengeSessionId, user);
        endChallengeSession.setPairs(collect);
        sessions.put(endChallengeSession.getEndChallengeSessionId(), endChallengeSession);
    }

    public void chosePair(HttpSession session, String pair) {
        EndChallengeSession endChallengeSession = sessions.get(getEndChallengeSessionId(session));
        endChallengeSession.setChosenPair(endChallengeSession.getPairs().stream().filter(ccyPairDto -> ccyPairDto.getCurrencyPairFormatted().equals(pair)).findAny().orElseThrow(RuntimeException::new));
        EcStaticDataDailyDto ecStaticDataDailyDto = ecStaticDataDailyMapper.mapToDto(ecStaticDataDailyRepository.getByCcyPair(endChallengeSession.getChosenPair().getCurrencyPair()));
        endChallengeSession.setEcStaticDataDailyDto(ecStaticDataDailyDto);
    }

    public void choseSentiment(HttpSession session, Sentiment sentiment) {
        EndChallengeSession endChallengeSession = sessions.get(getEndChallengeSessionId(session));
        endChallengeSession.setSentiment(sentiment);
    }

    private void setAmounts(EndChallengeSession endChallengeSession) {
        Amounts amounts = new Amounts();

        if (endChallengeSession.getSentiment().equals(Sentiment.BULLISH)) {

            switch (endChallengeSession.getChosenPair().getCurrency1().getName()) {
                case "EUR":
                    amounts.setEur("+" + endChallengeSession.getEcStaticDataDailyDto().getAskPosSize());
                    break;
                case "GBP":
                    amounts.setGbp("+" + endChallengeSession.getEcStaticDataDailyDto().getAskPosSize());
                    break;
                case "USD":
                    amounts.setUsd("+" + endChallengeSession.getEcStaticDataDailyDto().getAskPosSize());
                    break;
                case "JPY":
                    amounts.setJpy("+" + endChallengeSession.getEcStaticDataDailyDto().getAskPosSize());
                    break;
            }

            switch (endChallengeSession.getChosenPair().getCurrency2().getName()) {
                case "EUR":
                    amounts.setEur("-" + endChallengeSession.getEcStaticDataDailyDto().getPosSize());
                    break;
                case "GBP":
                    amounts.setGbp("-" + endChallengeSession.getEcStaticDataDailyDto().getPosSize());
                    break;
                case "USD":
                    amounts.setUsd("-" + endChallengeSession.getEcStaticDataDailyDto().getPosSize());
                    break;
                case "JPY":
                    amounts.setJpy("-" + endChallengeSession.getEcStaticDataDailyDto().getPosSize());
                    break;
            }
        }

        if (endChallengeSession.getSentiment().equals(Sentiment.BEARISH)) {
            switch (endChallengeSession.getChosenPair().getCurrency1().getName()) {
                case "EUR":
                    amounts.setEur("-" + endChallengeSession.getEcStaticDataDailyDto().getBidPosSize());
                    break;
                case "GBP":
                    amounts.setGbp("-" + endChallengeSession.getEcStaticDataDailyDto().getBidPosSize());
                    break;
                case "USD":
                    amounts.setUsd("-" + endChallengeSession.getEcStaticDataDailyDto().getBidPosSize());
                    break;
                case "JPY":
                    amounts.setJpy("-" + endChallengeSession.getEcStaticDataDailyDto().getAskPosSize());
                    break;
            }

            switch (endChallengeSession.getChosenPair().getCurrency2().getName()) {
                case "EUR":
                    amounts.setEur("+" + endChallengeSession.getEcStaticDataDailyDto().getPosSize());
                    break;
                case "GBP":
                    amounts.setGbp("+" + endChallengeSession.getEcStaticDataDailyDto().getPosSize());
                    break;
                case "USD":
                    amounts.setUsd("+" + endChallengeSession.getEcStaticDataDailyDto().getPosSize());
                    break;
                case "JPY":
                    amounts.setJpy("+" + endChallengeSession.getEcStaticDataDailyDto().getPosSize());
                    break;
            }
        }


        endChallengeSession.setAmounts(amounts);
    }

    public void chooseCapitalPercent(HttpSession session, EndChallengeTradeDto endChallengeTradeDto) {
        EndChallengeSession endChallengeSession = sessions.get(getEndChallengeSessionId(session));
        endChallengeSession.setCapitalPercent(endChallengeTradeDto.getCapitalPercent());

        EcStaticDataDailyDto ecStaticDataDailyDto = endChallengeSession.getEcStaticDataDailyDto();

        switch (endChallengeSession.getCapitalPercent()) {
            case 1:
                ecStaticDataDailyDto.setUserCapitalPosSize(HyfinUtils.formatDecimalToMoney(ecStaticDataDailyDto.getUserCapitalPosSize01()));
                break;
            case 2:
                ecStaticDataDailyDto.setUserCapitalPosSize(HyfinUtils.formatDecimalToMoney(ecStaticDataDailyDto.getUserCapitalPosSize02()));
                break;
            case 3:
                ecStaticDataDailyDto.setUserCapitalPosSize(HyfinUtils.formatDecimalToMoney(ecStaticDataDailyDto.getUserCapitalPosSize03()));
                break;
            case 4:
                ecStaticDataDailyDto.setUserCapitalPosSize(HyfinUtils.formatDecimalToMoney(ecStaticDataDailyDto.getUserCapitalPosSize04()));
                break;
            case 5:
                ecStaticDataDailyDto.setUserCapitalPosSize(HyfinUtils.formatDecimalToMoney(ecStaticDataDailyDto.getUserCapitalPosSize05()));
                break;
            case 6:
                ecStaticDataDailyDto.setUserCapitalPosSize(HyfinUtils.formatDecimalToMoney(ecStaticDataDailyDto.getUserCapitalPosSize06()));
                break;
            case 7:
                ecStaticDataDailyDto.setUserCapitalPosSize(HyfinUtils.formatDecimalToMoney(ecStaticDataDailyDto.getUserCapitalPosSize07()));
                break;
            case 8:
                ecStaticDataDailyDto.setUserCapitalPosSize(HyfinUtils.formatDecimalToMoney(ecStaticDataDailyDto.getUserCapitalPosSize08()));
                break;
        }

        Trade trade = new Trade();

        String timestamp = String.valueOf(System.currentTimeMillis());
        trade.setId(timestamp.substring(timestamp.length() - 9, timestamp.length() - 1));

        setCcyPairTradedRate(endChallengeSession, ecStaticDataDailyDto);
        setPnl(endChallengeSession, ecStaticDataDailyDto);
        setMrgSize(endChallengeSession, ecStaticDataDailyDto);
        setAskPosSize(endChallengeSession, ecStaticDataDailyDto);
        setBidPosSize(endChallengeSession, ecStaticDataDailyDto);
        setPosSize(endChallengeSession, ecStaticDataDailyDto);

        endChallengeSession.setTrade(trade);

        setAmounts(endChallengeSession);
    }

    public EndChallengeSession getEndChallengeSession(HttpSession session){
        return sessions.get(getEndChallengeSessionId(session));
    }

    private void setPosSize(EndChallengeSession endChallengeSession, EcStaticDataDailyDto ecStaticDataDailyDto) {
        if (endChallengeSession.getCapitalPercent().equals(1)) {
            ecStaticDataDailyDto.setPosSize(HyfinUtils.formatDecimalToMoney(ecStaticDataDailyDto.getPosSize01()));
        }
        if (endChallengeSession.getCapitalPercent().equals(2)) {
            ecStaticDataDailyDto.setPosSize(HyfinUtils.formatDecimalToMoney(ecStaticDataDailyDto.getPosSize02()));
        }
        if (endChallengeSession.getCapitalPercent().equals(3)) {
            ecStaticDataDailyDto.setPosSize(HyfinUtils.formatDecimalToMoney(ecStaticDataDailyDto.getPosSize03()));
        }
        if (endChallengeSession.getCapitalPercent().equals(4)) {
            ecStaticDataDailyDto.setPosSize(HyfinUtils.formatDecimalToMoney(ecStaticDataDailyDto.getPosSize04()));
        }
        if (endChallengeSession.getCapitalPercent().equals(5)) {
            ecStaticDataDailyDto.setPosSize(HyfinUtils.formatDecimalToMoney(ecStaticDataDailyDto.getPosSize05()));
        }
        if (endChallengeSession.getCapitalPercent().equals(6)) {
            ecStaticDataDailyDto.setPosSize(HyfinUtils.formatDecimalToMoney(ecStaticDataDailyDto.getPosSize06()));
        }
        if (endChallengeSession.getCapitalPercent().equals(7)) {
            ecStaticDataDailyDto.setPosSize(HyfinUtils.formatDecimalToMoney(ecStaticDataDailyDto.getPosSize07()));
        }
        if (endChallengeSession.getCapitalPercent().equals(8)) {
            ecStaticDataDailyDto.setPosSize(HyfinUtils.formatDecimalToMoney(ecStaticDataDailyDto.getPosSize08()));
        }
    }

    private void setAskPosSize(EndChallengeSession endChallengeSession, EcStaticDataDailyDto ecStaticDataDailyDto) {
        if (endChallengeSession.getCapitalPercent().equals(1)) {
            ecStaticDataDailyDto.setAskPosSize(HyfinUtils.formatDecimalToMoney(ecStaticDataDailyDto.getAskPosSize01()));
        }
        if (endChallengeSession.getCapitalPercent().equals(2)) {
            ecStaticDataDailyDto.setAskPosSize(HyfinUtils.formatDecimalToMoney(ecStaticDataDailyDto.getAskPosSize02()));
        }
        if (endChallengeSession.getCapitalPercent().equals(3)) {
            ecStaticDataDailyDto.setAskPosSize(HyfinUtils.formatDecimalToMoney(ecStaticDataDailyDto.getAskPosSize03()));
        }
        if (endChallengeSession.getCapitalPercent().equals(4)) {
            ecStaticDataDailyDto.setAskPosSize(HyfinUtils.formatDecimalToMoney(ecStaticDataDailyDto.getAskPosSize04()));
        }
        if (endChallengeSession.getCapitalPercent().equals(5)) {
            ecStaticDataDailyDto.setAskPosSize(HyfinUtils.formatDecimalToMoney(ecStaticDataDailyDto.getAskPosSize05()));
        }
        if (endChallengeSession.getCapitalPercent().equals(6)) {
            ecStaticDataDailyDto.setAskPosSize(HyfinUtils.formatDecimalToMoney(ecStaticDataDailyDto.getAskPosSize06()));
        }
        if (endChallengeSession.getCapitalPercent().equals(7)) {
            ecStaticDataDailyDto.setAskPosSize(HyfinUtils.formatDecimalToMoney(ecStaticDataDailyDto.getAskPosSize07()));
        }
        if (endChallengeSession.getCapitalPercent().equals(8)) {
            ecStaticDataDailyDto.setAskPosSize(HyfinUtils.formatDecimalToMoney(ecStaticDataDailyDto.getAskPosSize08()));
        }
    }

    private void setBidPosSize(EndChallengeSession endChallengeSession, EcStaticDataDailyDto ecStaticDataDailyDto) {
        if (endChallengeSession.getCapitalPercent().equals(1)) {
            ecStaticDataDailyDto.setBidPosSize(HyfinUtils.formatDecimalToMoney(ecStaticDataDailyDto.getBidPosSize01()));
        }
        if (endChallengeSession.getCapitalPercent().equals(2)) {
            ecStaticDataDailyDto.setBidPosSize(HyfinUtils.formatDecimalToMoney(ecStaticDataDailyDto.getBidPosSize02()));
        }
        if (endChallengeSession.getCapitalPercent().equals(3)) {
            ecStaticDataDailyDto.setBidPosSize(HyfinUtils.formatDecimalToMoney(ecStaticDataDailyDto.getBidPosSize03()));
        }
        if (endChallengeSession.getCapitalPercent().equals(4)) {
            ecStaticDataDailyDto.setBidPosSize(HyfinUtils.formatDecimalToMoney(ecStaticDataDailyDto.getBidPosSize04()));
        }
        if (endChallengeSession.getCapitalPercent().equals(5)) {
            ecStaticDataDailyDto.setBidPosSize(HyfinUtils.formatDecimalToMoney(ecStaticDataDailyDto.getBidPosSize05()));
        }
        if (endChallengeSession.getCapitalPercent().equals(6)) {
            ecStaticDataDailyDto.setBidPosSize(HyfinUtils.formatDecimalToMoney(ecStaticDataDailyDto.getBidPosSize06()));
        }
        if (endChallengeSession.getCapitalPercent().equals(7)) {
            ecStaticDataDailyDto.setBidPosSize(HyfinUtils.formatDecimalToMoney(ecStaticDataDailyDto.getBidPosSize07()));
        }
        if (endChallengeSession.getCapitalPercent().equals(8)) {
            ecStaticDataDailyDto.setBidPosSize(HyfinUtils.formatDecimalToMoney(ecStaticDataDailyDto.getBidPosSize08()));
        }
    }


    private void setMrgSize(EndChallengeSession endChallengeSession, EcStaticDataDailyDto ecStaticDataDailyDto) {
        if (endChallengeSession.getCapitalPercent().equals(1)) {
            ecStaticDataDailyDto.setMrgSize(HyfinUtils.formatDecimalToMoney(ecStaticDataDailyDto.getMrgSize01()));
        }
        if (endChallengeSession.getCapitalPercent().equals(2)) {
            ecStaticDataDailyDto.setMrgSize(HyfinUtils.formatDecimalToMoney(ecStaticDataDailyDto.getMrgSize02()));
        }
        if (endChallengeSession.getCapitalPercent().equals(3)) {
            ecStaticDataDailyDto.setMrgSize(HyfinUtils.formatDecimalToMoney(ecStaticDataDailyDto.getMrgSize03()));
        }
        if (endChallengeSession.getCapitalPercent().equals(4)) {
            ecStaticDataDailyDto.setMrgSize(HyfinUtils.formatDecimalToMoney(ecStaticDataDailyDto.getMrgSize04()));
        }
        if (endChallengeSession.getCapitalPercent().equals(5)) {
            ecStaticDataDailyDto.setMrgSize(HyfinUtils.formatDecimalToMoney(ecStaticDataDailyDto.getMrgSize05()));
        }
        if (endChallengeSession.getCapitalPercent().equals(6)) {
            ecStaticDataDailyDto.setMrgSize(HyfinUtils.formatDecimalToMoney(ecStaticDataDailyDto.getMrgSize05()));
        }
        if (endChallengeSession.getCapitalPercent().equals(7)) {
            ecStaticDataDailyDto.setMrgSize(HyfinUtils.formatDecimalToMoney(ecStaticDataDailyDto.getMrgSize07()));
        }
        if (endChallengeSession.getCapitalPercent().equals(8)) {
            ecStaticDataDailyDto.setMrgSize(HyfinUtils.formatDecimalToMoney(ecStaticDataDailyDto.getMrgSize08()));
        }
    }

    private void setPnl(EndChallengeSession endChallengeSession, EcStaticDataDailyDto ecStaticDataDailyDto) {
        if (endChallengeSession.getSentiment().equals(Sentiment.BULLISH)) {
            if (endChallengeSession.getCapitalPercent().equals(1)) {
                ecStaticDataDailyDto.setPnl(ecStaticDataDailyDto.getPnlTerm01Bullish());
            }
            if (endChallengeSession.getCapitalPercent().equals(2)) {
                ecStaticDataDailyDto.setPnl(ecStaticDataDailyDto.getPnlTerm02Bullish());
            }
            if (endChallengeSession.getCapitalPercent().equals(3)) {
                ecStaticDataDailyDto.setPnl(ecStaticDataDailyDto.getPnlTerm03Bullish());
            }
            if (endChallengeSession.getCapitalPercent().equals(4)) {
                ecStaticDataDailyDto.setPnl(ecStaticDataDailyDto.getPnlTerm04Bullish());
            }
            if (endChallengeSession.getCapitalPercent().equals(5)) {
                ecStaticDataDailyDto.setPnl(ecStaticDataDailyDto.getPnlTerm05Bullish());
            }
            if (endChallengeSession.getCapitalPercent().equals(6)) {
                ecStaticDataDailyDto.setPnl(ecStaticDataDailyDto.getPnlTerm06Bullish());
            }
            if (endChallengeSession.getCapitalPercent().equals(7)) {
                ecStaticDataDailyDto.setPnl(ecStaticDataDailyDto.getPnlTerm07Bullish());
            }
            if (endChallengeSession.getCapitalPercent().equals(8)) {
                ecStaticDataDailyDto.setPnl(ecStaticDataDailyDto.getPnlTerm08Bullish());
            }
        }

        if (endChallengeSession.getSentiment().equals(Sentiment.BEARISH)) {
            if (endChallengeSession.getCapitalPercent().equals(1)) {
                ecStaticDataDailyDto.setPnl(ecStaticDataDailyDto.getPnlTerm01Bearish());
            }
            if (endChallengeSession.getCapitalPercent().equals(2)) {
                ecStaticDataDailyDto.setPnl(ecStaticDataDailyDto.getPnlTerm02Bearish());
            }
            if (endChallengeSession.getCapitalPercent().equals(3)) {
                ecStaticDataDailyDto.setPnl(ecStaticDataDailyDto.getPnlTerm03Bearish());
            }
            if (endChallengeSession.getCapitalPercent().equals(4)) {
                ecStaticDataDailyDto.setPnl(ecStaticDataDailyDto.getPnlTerm04Bearish());
            }
            if (endChallengeSession.getCapitalPercent().equals(5)) {
                ecStaticDataDailyDto.setPnl(ecStaticDataDailyDto.getPnlTerm05Bearish());
            }
            if (endChallengeSession.getCapitalPercent().equals(6)) {
                ecStaticDataDailyDto.setPnl(ecStaticDataDailyDto.getPnlTerm06Bearish());
            }
            if (endChallengeSession.getCapitalPercent().equals(7)) {
                ecStaticDataDailyDto.setPnl(ecStaticDataDailyDto.getPnlTerm07Bearish());
            }
            if (endChallengeSession.getCapitalPercent().equals(8)) {
                ecStaticDataDailyDto.setPnl(ecStaticDataDailyDto.getPnlTerm08Bearish());
            }
        }
    }

    private void setCcyPairTradedRate(EndChallengeSession endChallengeSession, EcStaticDataDailyDto ecStaticDataDailyDto) {
        if (endChallengeSession.getSentiment().equals(Sentiment.BULLISH)) {
            ecStaticDataDailyDto.setCcyPairTradedRate(ecStaticDataDailyDto.getCcyPairTradedRateBullish());
        }
        if (endChallengeSession.getSentiment().equals(Sentiment.BEARISH)) {
            ecStaticDataDailyDto.setCcyPairTradedRate(ecStaticDataDailyDto.getCcyPairTradedRateBearish());
        }
    }

    public CcyPairDto getChosenPair(HttpSession session) {
        EndChallengeSession endChallengeSession = sessions.get(getEndChallengeSessionId(session));
        return endChallengeSession.getChosenPair();
    }

    public EcStaticDataDailyDto getEcStaticDataDaily(HttpSession session) {
        EndChallengeSession endChallengeSession = sessions.get(getEndChallengeSessionId(session));
        return endChallengeSession.getEcStaticDataDailyDto();
    }

    public Trade getTrade(HttpSession session) {
        EndChallengeSession endChallengeSession = sessions.get(getEndChallengeSessionId(session));
        return endChallengeSession.getTrade();
    }

    public List<CcyPairDto> getAllPairs(HttpSession session) {
        return sessions.get(getEndChallengeSessionId(session)).getPairs();
    }

    private String getEndChallengeSessionId(HttpSession session) {
        if (Objects.isNull(session.getAttribute("end-challenge-session-id"))) {
            return "none";
        }

        return (String) session.getAttribute("end-challenge-session-id");
    }
}
