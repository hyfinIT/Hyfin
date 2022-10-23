package hyphin.service;

import hyphin.enums.AuditEventType;
import hyphin.model.User;
import hyphin.model.UserAudit;
import hyphin.model.video.UserVideoSession;
import hyphin.repository.UserAuditRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor
@Slf4j
public class VideoService {
    private final UserAuditRepository userAuditRepository;

    private static final SimpleDateFormat jdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    private static final long SESSION_LIVE_TIME = 1000 * 60 * 60;
    private static final long VIDEO_TIMEOUT_MILLIS = 1000 * 60 * 4;
    private static final String MODULE_ID = "1";
    private static final String ELEMENT_ID = "1";
    private ConcurrentHashMap<String, UserVideoSession> userVideoSessions = new ConcurrentHashMap<>();

    Timer timer = new Timer();

    {
        checkExpired();
    }

    public void checkExpired() {
        timer.schedule(new TimerTask() {
            public void run() {
                for (UserVideoSession userVideoSession : userVideoSessions.values()) {
                    if (userVideoSession.isPaused() && isTimeOut(userVideoSession) && !userVideoSession.isExpired()) {
                        userVideoSession.setExpired(true);
                        flushToDb(userVideoSession);
                    }
                }
            }
        }, 0, 3 * 1000);

        timer.schedule(new TimerTask() {
            public void run() {
                log.info("session count before cleaning: {}", userVideoSessions.size());
                for (UserVideoSession userVideoSession : userVideoSessions.values()) {
                    if (userVideoSession.isExpired() && isSessionLiveTimeOut(userVideoSession)) {
                        userVideoSessions.remove(userVideoSession.getVideoSessionId());
                    }
                }
                log.info("session count after cleaning: {}", userVideoSessions.size());
            }
        }, 0, SESSION_LIVE_TIME);
    }

    public void startVideoSession(HttpSession session) {
        String videoSessionId = session.getId() + System.currentTimeMillis();

        userVideoSessions.put(videoSessionId,
                UserVideoSession
                        .builder()
                        .videoSessionId(videoSessionId)
                        .paused(true)
                        .pauseEventTime(System.currentTimeMillis())
                        .userAudits(new ConcurrentLinkedQueue<>())
                        .user((User) session.getAttribute("User-entity"))
                        .build());

        session.setAttribute("video-session-id", videoSessionId);
    }

    public void pause(HttpSession session) {
        userVideoSessions.get(getVideoSessionId(session)).pause();
    }

    public void play(HttpSession session) {
        userVideoSessions.get(getVideoSessionId(session)).play();
    }

    public void finish(HttpSession session) {
        flushToDb(userVideoSessions.get(getVideoSessionId(session)));
    }

    private boolean isTimeOut(UserVideoSession userVideoSession) {
        return System.currentTimeMillis() - userVideoSession.getPauseEventTime() > VIDEO_TIMEOUT_MILLIS;
    }

    private boolean isSessionLiveTimeOut(UserVideoSession userVideoSession) {
        return System.currentTimeMillis() - userVideoSession.getPauseEventTime() > SESSION_LIVE_TIME;
    }

    public void handleEvent(AuditEventType eventType, HttpSession session, String additionalInfo) {
        if (eventType.equals(AuditEventType.START_SESSION)) {
            addAuditToList(eventType, session, additionalInfo);
        }

        if (!userVideoSessions.containsKey(getVideoSessionId(session))) {
            return;
        }

        if (eventType.equals(AuditEventType.PLAY)) {
            addAuditToList(eventType, session, additionalInfo);
            play(session);
        }
        if (eventType.equals(AuditEventType.PAUSE)) {
            addAuditToList(eventType, session, additionalInfo);
            pause(session);
        }
        if (eventType.equals(AuditEventType.LEAVE)) {
            addAuditToList(eventType, session, additionalInfo);
            finish(session);
        }

        if (eventType.equals(AuditEventType.COMPLETE)) {
            addAuditToList(eventType, session, additionalInfo);
        }

        if (eventType.equals(AuditEventType.REWIND)) {
            addAuditToList(eventType, session, additionalInfo);
        }
    }

    public Boolean isSessionExpired(HttpSession session) {
        UserVideoSession userVideoSession = userVideoSessions.get(getVideoSessionId(session));
        if (Objects.isNull(userVideoSession)) {
            return true;
        }
        return userVideoSessions.get(getVideoSessionId(session)).isExpired();
    }

    private void addAuditToList(AuditEventType eventType, HttpSession session, String additionalInfo) {
        if (!userVideoSessions.containsKey(getVideoSessionId(session))) {
            return;
        }

        UserAudit userAudit = new UserAudit();
        userAudit.setModuleId(MODULE_ID);
        userAudit.setElementId(ELEMENT_ID);
        userAudit.setElementStatus(eventType.name());

        User user = userVideoSessions.get(getVideoSessionId(session)).getUser();

        if (user != null)
            userAudit.setUid(user.getUid());
        userAudit.setLearningJourney(userAuditRepository.findLearningJourneyName());
        userAudit.setLearningJourneyId(userAuditRepository.findLearningJourneyId());
        userAudit.setModule(userAuditRepository.findModuleName(userAudit.getModuleId()));
        userAudit.setElementPosition(userAudit.getElementId());
        userAudit.setGlossaryTerm(userAuditRepository.findGlossaryTerm(userAudit.getModuleId(), userAudit.getLearningJourney()));
        userAudit.setMediaType(userAuditRepository.findElementType(userAudit.getElementId()));
        userAudit.setActivityType(userAuditRepository.findElementType(userAudit.getElementId()));
        userAudit.setDateTime(jdf.format(new Date()));
        userAudit.setDifficulty(null);
        userAudit.setCompletionTime(null);
        userAudit.setText1(additionalInfo);

        userVideoSessions.get(getVideoSessionId(session)).getUserAudits().add(userAudit);
    }

    private void flushToDb(UserVideoSession userVideoSession) {
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                Thread.sleep(10000l);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            userVideoSession.getUserAudits().forEach(userAuditRepository::saveAndFlush);
        });
    }

    private String getVideoSessionId(HttpSession session) {
        if (Objects.isNull(session.getAttribute("video-session-id"))) {
            return "none";
        }

        return (String) session.getAttribute("video-session-id");
    }
}
