package hyphin.model.endchallenge;

import hyphin.dto.Amounts;
import hyphin.dto.CcyPairDto;
import hyphin.dto.EcStaticDataDailyDto;
import hyphin.dto.Trade;
import hyphin.enums.Sentiment;
import hyphin.model.User;
import hyphin.model.UserAudit;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

@Data
public class EndChallengeSession {

    private String endChallengeSessionId;
    private int round;
    private int answersCounter;
    private int correctAnswersCounter;

    private ConcurrentLinkedQueue<UserAudit> userAudits = new ConcurrentLinkedQueue<>();
    private List<CcyPairDto> pairs = new ArrayList<>();
    private CcyPairDto chosenPair;
    private EcStaticDataDailyDto ecStaticDataDailyDto;
    private Sentiment sentiment;
    private Integer slOptionNumber;
    private Integer tpOptionNumber;
    private Double slTermAmount;
    private Double tpTermAmount;
    private Integer capitalPercent;
    private Trade trade;
    private Amounts amounts;

    private User user;

    private long lastActivityTime = System.currentTimeMillis();
    private boolean expired = false;

    public EndChallengeSession(String endChallengeSessionId, User user) {
        this.endChallengeSessionId = endChallengeSessionId;
        this.round = 1;
        this.answersCounter = 0;
        this.correctAnswersCounter = 0;
        this.user = user;
    }
}

