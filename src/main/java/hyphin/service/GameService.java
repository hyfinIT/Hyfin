package hyphin.service;

import hyphin.enums.AuditEventType;
import hyphin.model.User;
import hyphin.model.UserAudit;
import hyphin.model.game.UserGame;
import hyphin.model.video.UserVideoSession;
import hyphin.repository.UserAuditRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;

@Service
@Slf4j
@RequiredArgsConstructor
public class GameService {
    private final UserAuditRepository userAuditRepository;

    private static final SimpleDateFormat jdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    private static final String GAMES_ELEMENT_ID = "3";
    private static final long SESSION_LIVE_TIME = 1000 * 60 * 60;
    private static final long GAME_TIMEOUT_MILLIS = 1000 * 45;
    private ConcurrentHashMap<String, UserGame> userGames = new ConcurrentHashMap<>();

    Timer timer = new Timer();

    {
        checkExpired();
    }

    public void checkExpired() {
        timer.schedule(new TimerTask() {
            public void run() {
                for (UserGame userGame : userGames.values()) {
                    if (isTimeOut(userGame) && !userGame.isExpired()) {
                        userGame.setExpired(true);
                        flushToDb(userGame);
                    }
                }
            }
        }, 0, 3 * 1000);

        timer.schedule(new TimerTask() {
            public void run() {
                log.info("session count before cleaning: {}", userGames.size());
                for (UserGame userGame : userGames.values()) {
                    if (userGame.isExpired() && isSessionLiveTimeOut(userGame)) {
                        userGames.remove(userGame.getGameSessionId());
                    }
                }
                log.info("session count after cleaning: {}", userGames.size());
            }
        }, 0, SESSION_LIVE_TIME);
    }

    public void increaseAnswersCounter(HttpSession session) {
        if (userGames.containsKey(getGameSessionId(session))) {
            UserGame userGame = userGames.get(getGameSessionId(session));
            userGame.setAnswersCounter(userGame.getAnswersCounter() + 1);
            userGames.put(getGameSessionId(session), userGame);
        } else {
            throw new RuntimeException("Increase answers counter error");
        }
    }

    public void increaseCorrectAnswersCounter(HttpSession session) {
        if (userGames.containsKey(getGameSessionId(session))) {
            UserGame userGame = userGames.get(getGameSessionId(session));
            userGame.setCorrectAnswersCounter(userGame.getCorrectAnswersCounter() + 1);
            userGames.put(getGameSessionId(session), userGame);
        } else {
            throw new RuntimeException("Increase correct answers counter error");
        }
    }

    public void increaseRound(HttpSession session) {
        if (userGames.containsKey(getGameSessionId(session))) {
            UserGame userGame = userGames.get(getGameSessionId(session));
            userGame.setRound(userGame.getRound() + 1);
            userGame.setAnswersCounter(0);
            userGame.setCorrectAnswersCounter(0);
            userGames.put(getGameSessionId(session), userGame);
        } else {
            log.error("Increase round error");
        }
    }

    public int getOrCreateAnswersCounter(HttpSession session, User user) {
        if (userGames.containsKey(getGameSessionId(session)) && !userGames.get(getGameSessionId(session)).isExpired()) {
            return userGames.get(getGameSessionId(session)).getAnswersCounter();
        } else {
            String s = session.getId() + System.currentTimeMillis();
            userGames.put(s, new UserGame(s, user));
            session.setAttribute("game-session-id", s);
            return 1;
        }
    }

    public int getRound(HttpSession session) {
        if (userGames.containsKey(getGameSessionId(session))) {
            return userGames.get(getGameSessionId(session)).getRound();
        } else {
            throw new RuntimeException("Get round error");
        }
    }

    public int getCorrectAnswersCounter(HttpSession session) {
        if (userGames.containsKey(getGameSessionId(session))) {
            return userGames.get(getGameSessionId(session)).getCorrectAnswersCounter();
        } else {
            throw new RuntimeException("Get correct answers count error");
        }
    }

    public void dropCounter(HttpSession session) {
        if (Objects.isNull(userGames.get(getGameSessionId(session)))) {
            return;
        }
        userGames.get(getGameSessionId(session)).   setExpired(true);
    }

    public int getQuestionNumber(HttpSession session) {
        if (userGames.containsKey(getGameSessionId(session))) {
            UserGame userGame = userGames.get(getGameSessionId(session));
            return userGame.getRound() * 10 - 10 + (userGame.getAnswersCounter() + 1);
        } else {
            throw new RuntimeException("Get question number error");
        }
    }

    public void touch(HttpSession session){
        if (Objects.isNull(userGames.get(getGameSessionId(session)))) {
            return;
        }
        userGames.get(getGameSessionId(session)).setLastActivityTime(System.currentTimeMillis());
    }

    public void addAuditToList(HttpSession session, UserAudit userAudit) {
        if (Objects.isNull(userGames.get(getGameSessionId(session)))) {
            return;
        }

        userGames.get(getGameSessionId(session)).getUserAudits().add(userAudit);
    }

    private String getGameSessionId(HttpSession session) {
        if (Objects.isNull(session.getAttribute("game-session-id"))) {
            return "none";
        }

        return (String) session.getAttribute("game-session-id");
    }

    private boolean isTimeOut(UserGame userGame) {
        return System.currentTimeMillis() - userGame.getLastActivityTime() > GAME_TIMEOUT_MILLIS;
    }

    private boolean isSessionLiveTimeOut(UserGame userGame) {
        return System.currentTimeMillis() - userGame.getLastActivityTime() > SESSION_LIVE_TIME;
    }

    private void flushToDb(UserGame userGame) {
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                Thread.sleep(10000l);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            userGame.getUserAudits().stream().sorted(Comparator.comparing(UserAudit::getDateTime)).forEach(userAuditRepository::saveAndFlush);
        });
    }

    public void handleEvent(AuditEventType eventType, HttpSession session) {
        if (!userGames.containsKey(getGameSessionId(session))) {
                return;
        }

        User user = (User) session.getAttribute("User-entity");

        UserAudit userAudit = new UserAudit();
        userAudit.setActivityType("IN MODULE GAME");
        userAudit.setElementId(GAMES_ELEMENT_ID);
        if (user != null)
            userAudit.setUid(user.getUid());
        userAudit.setLearningJourney(userAuditRepository.findLearningJourneyName());
        userAudit.setLearningJourneyId(userAuditRepository.findLearningJourneyId());
        userAudit.setModuleId(userAuditRepository.findModuleID());
        userAudit.setModule(userAuditRepository.findModuleName(userAudit.getModuleId(), GAMES_ELEMENT_ID));
        userAudit.setElementPosition(userAudit.getElementId());
        userAudit.setGlossaryTerm(userAuditRepository.findGlossaryTerm(userAudit.getModuleId(), userAudit.getLearningJourney()));
        userAudit.setMediaType(userAuditRepository.findElementType(userAudit.getElementId()));
        userAudit.setActivityType(userAuditRepository.findElementType(userAudit.getElementId()));
        userAudit.setDateTime(jdf.format(new Date()));
        userAudit.setQuidNumber(null);
        userAudit.setQuidNumberOutcome(null);
        userAudit.setDifficulty(null);
        userAudit.setCompletionTime(null);
        userAudit.setQuidAnswer(null);

        if (eventType.equals(AuditEventType.LEAVE)) {
            userAudit.setModuleProgressPosition("INCOMPLETE, ROUND: " + getRound(session) +  ", GAME QUESTION NO " + getQuestionNumber(session) + "" + " ASKED");
            userAudit.setElementStatus(eventType.name());
            addAuditToList(session, userAudit);
            userGames.get(getGameSessionId(session)).setExpired(true);
            flushToDb(userGames.get(getGameSessionId(session)));
        }

        if (eventType.equals(AuditEventType.COMPLETE)) {
            userAudit.setModuleProgressPosition("Complete");
            userAudit.setElementStatus(eventType.name());
            addAuditToList(session, userAudit);
            userGames.get(getGameSessionId(session)).setExpired(true);
            flushToDb(userGames.get(getGameSessionId(session)));
        }

        if (eventType.equals(AuditEventType.START_SESSION)) {
            userAudit.setModuleProgressPosition("INCOMPLETE, Game session started");
            userAudit.setElementStatus(eventType.name());
            addAuditToList(session, userAudit);
        }
    }

    public Boolean isSessionExpired(HttpSession session) {
        UserGame userGame = userGames.get(getGameSessionId(session));
        if (Objects.isNull(userGame)) {
            return true;
        }
        return userGames.get(getGameSessionId(session)).isExpired();
    }
}
