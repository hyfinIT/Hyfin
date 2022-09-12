package hyphin.service;

import hyphin.model.game.UserGame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class GameService {
    private Map<String, UserGame> userGames = new HashMap<>();

    public void increaseAnswersCounter(String sessionId) {
        if (userGames.containsKey(sessionId)) {
            UserGame userGame = userGames.get(sessionId);
            userGame.setAnswersCounter(userGame.getAnswersCounter() + 1);
            userGames.put(sessionId, userGame);
        } else {
            throw new RuntimeException("Increase answers counter error");
        }
    }

    public void increaseCorrectAnswersCounter(String sessionId) {
        if (userGames.containsKey(sessionId)) {
            UserGame userGame = userGames.get(sessionId);
            userGame.setCorrectAnswersCounter(userGame.getCorrectAnswersCounter() + 1);
            userGames.put(sessionId, userGame);
        } else {
            throw new RuntimeException("Increase correct answers counter error");
        }
    }

    public void increaseRound(String sessionId) {
        if (userGames.containsKey(sessionId)) {
            UserGame userGame = userGames.get(sessionId);
            userGame.setRound(userGame.getRound() + 1);
            userGame.setAnswersCounter(0);
            userGame.setCorrectAnswersCounter(0);
            userGames.put(sessionId, userGame);
        } else {
            log.error("Increase round error");
        }
    }

    public int getOrCreateAnswersCounter(String sessionId) {
        if (userGames.containsKey(sessionId)) {
            return userGames.get(sessionId).getAnswersCounter();
        } else {
            userGames.put(sessionId, new UserGame(sessionId));
            return 1;
        }
    }

    public int getRound(String sessionId) {
        if (userGames.containsKey(sessionId)) {
            return userGames.get(sessionId).getRound();
        } else {
            throw new RuntimeException("Get round error");
        }
    }

    public int getCorrectAnswersCounter(String sessionId) {
        if (userGames.containsKey(sessionId)) {
            return userGames.get(sessionId).getCorrectAnswersCounter();
        } else {
            throw new RuntimeException("Get correct answers count error");
        }
    }

    public void dropCounter(String sessionId) {
        userGames.remove(sessionId);
    }
}
