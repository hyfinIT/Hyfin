package hyphin.model.game;

import hyphin.model.User;
import hyphin.model.UserAudit;
import lombok.Data;

import java.util.concurrent.ConcurrentLinkedQueue;

@Data
public class UserGame {

    private String gameSessionId;
    private int round;
    private int answersCounter;
    private int correctAnswersCounter;

    private ConcurrentLinkedQueue<UserAudit> userAudits = new ConcurrentLinkedQueue<>();

    private User user;

    private long lastActivityTime = System.currentTimeMillis();
    private boolean expired = false;

    public UserGame(String gameSessionId, User user) {
        this.gameSessionId = gameSessionId;
        this.round = 1;
        this.answersCounter = 0;
        this.correctAnswersCounter = 0;
        this.user = user;
    }
}
