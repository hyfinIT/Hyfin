package hyphin.model.game;

import lombok.Data;

@Data
public class UserGame {

    private String sessionId;
    private int round;
    private int answersCounter;
    private int correctAnswersCounter;

    public UserGame(String sessionId) {
        this.sessionId = sessionId;
        this.round = 1;
        this.answersCounter = 0;
        this.correctAnswersCounter = 0;
    }
}
