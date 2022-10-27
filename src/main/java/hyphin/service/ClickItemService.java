package hyphin.service;

import hyphin.enums.AuditEventType;
import hyphin.model.User;
import hyphin.model.UserAudit;
import hyphin.model.click.ClickItem;
import hyphin.model.click.ClickSession;
import hyphin.repository.ClickItemRepository;
import hyphin.repository.UserAuditRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClickItemService {

    private final ClickItemRepository clickItemRepository;
    private final UserAuditRepository userAuditRepository;
    private List<ClickItem> clickItemList;

    private static final SimpleDateFormat jdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    private static final long SESSION_LIVE_TIME = 1000 * 60 * 60;
    private static final long CLICK_TIMEOUT_MILLIS = 1000 * 60 * 2;
    private static final String MODULE_ID = "1";
    private static final String ELEMENT_ID = "2";
    private ConcurrentHashMap<String, ClickSession> clickSessions = new ConcurrentHashMap<>();

    Timer timer = new Timer();

    @PostConstruct
    public void init() {
        clickItemList = clickItemRepository.findAll();
    }

    {
        checkExpired();
    }

    public int getClickItemListSize(){
        return clickItemList.size();
    }

    public void checkExpired() {
        timer.schedule(new TimerTask() {
            public void run() {
                for (ClickSession clickSession : clickSessions.values()) {
                    if (isTimeOut(clickSession) && !clickSession.isExpired()) {
                        clickSession.setExpired(true);
                        flushToDb(clickSession);
                    }
                }
            }
        }, 0, 3 * 1000);

        timer.schedule(new TimerTask() {
            public void run() {
                log.info("session count before cleaning: {}", clickSessions.size());
                for (ClickSession clickSession : clickSessions.values()) {
                    if (clickSession.isExpired() && isSessionLiveTimeOut(clickSession)) {
                        clickSessions.remove(clickSession.getActivitySessionId());
                    }
                }
                log.info("session count after cleaning: {}", clickSessions.size());
            }
        }, 0, SESSION_LIVE_TIME);
    }

    public void startClickSession(HttpSession session) {

        String activitySessionId = session.getId() + System.currentTimeMillis();

        clickSessions.put(activitySessionId,
                ClickSession
                        .builder()
                        .activitySessionId(activitySessionId)
                        .user((User) session.getAttribute("User-entity"))
                        .build());

        session.setAttribute("click-session-id", activitySessionId);

        handleEvent(AuditEventType.START_SESSION, session);
    }

    public ClickItem prevStep(HttpSession session) {
        ClickSession clickSession = clickSessions.get(getClickSessionId(session));
        if (Objects.isNull(clickSession)) {
            return null;
        }

        touch(session);
        clickSession.prevStep();
        handleEvent(AuditEventType.CLICK_PREV, session);

        return clickItemList
                .stream()
                .filter(clickItem -> clickItem.getNumber().equals(clickSession.getStepNumber()))
                .findAny()
                .orElse(null);
    }

    public ClickItem nextStep(HttpSession session) {
        ClickSession clickSession = clickSessions.get(getClickSessionId(session));
        if (Objects.isNull(clickSession)) {
            return null;
        }

        touch(session);
        clickSession.nextStep();
        handleEvent(AuditEventType.CLICK_NEXT, session);

        return clickItemList
                .stream()
                .filter(clickItem -> clickItem.getNumber().equals(clickSession.getStepNumber()))
                .findAny()
                .orElse(null);
    }


    public ClickItem currentStep(HttpSession session) {
        ClickSession clickSession = clickSessions.get(getClickSessionId(session));
        if (Objects.isNull(clickSession)) {
            return null;
        }

        return clickItemList
                .stream()
                .filter(clickItem -> clickItem.getNumber().equals(clickSession.getStepNumber()))
                .findAny()
                .orElse(null);
    }

    public Boolean isSessionExpired(HttpSession session) {
        ClickSession clickSession = clickSessions.get(getClickSessionId(session));
        if (Objects.isNull(clickSession)) {
            return true;
        }
        return clickSessions.get(getClickSessionId(session)).isExpired();
    }

    public void touch(HttpSession session) {
        if (Objects.isNull(clickSessions.get(getClickSessionId(session)))) {
            return;
        }
        clickSessions.get(getClickSessionId(session)).setLastActivityTime(System.currentTimeMillis());
    }

    public void handleEvent(AuditEventType eventType, HttpSession session) {
        if (!clickSessions.containsKey(getClickSessionId(session))) {
            return;
        }

        if (eventType.equals(AuditEventType.LEAVE)) {
            clickSessions.get(getClickSessionId(session)).setExpired(true);
        }

        User user = (User) session.getAttribute("User-entity");

        UserAudit userAudit = new UserAudit();
        userAudit.setActivityType("CLICKTHROUGH");
        userAudit.setElementId(ELEMENT_ID);
        if (user != null)
            userAudit.setUid(user.getUid());
        userAudit.setLearningJourney(userAuditRepository.findLearningJourneyName());
        userAudit.setLearningJourneyId(userAuditRepository.findLearningJourneyId());
        userAudit.setModuleId(userAuditRepository.findModuleID());
        userAudit.setModule(userAuditRepository.findModuleName(userAudit.getModuleId()));
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
            userAudit.setModuleProgressPosition("INCOMPLETE, STEP: " + currentStep(session).getNumber()); //NPI
            userAudit.setElementStatus(eventType.name());
            addAuditToList(session, userAudit);
            clickSessions.get(getClickSessionId(session)).setExpired(true);
            flushToDb(clickSessions.get(getClickSessionId(session)));
        }

        if (eventType.equals(AuditEventType.COMPLETE)) {
            userAudit.setModuleProgressPosition("Complete");
            userAudit.setElementStatus(eventType.name());
            addAuditToList(session, userAudit);
            clickSessions.get(getClickSessionId(session)).setExpired(true);
            flushToDb(clickSessions.get(getClickSessionId(session)));
        }

        if (eventType.equals(AuditEventType.START_SESSION)) {
            userAudit.setModuleProgressPosition("INCOMPLETE, " + clickItemList.size() + " PAGES LEFT");
            userAudit.setElementStatus(eventType.name());
            addAuditToList(session, userAudit);
        }

        //next events should be fired AFTER session processing
        if (!clickSessions.get(getClickSessionId(session)).isExpired()) {
            if (eventType.equals(AuditEventType.CLICK_PREV)) {
                userAudit.setModuleProgressPosition("INCOMPLETE, " + (clickItemList.size() - currentStep(session).getNumber()) + " PAGES LEFT");
                userAudit.setElementStatus(eventType.name() + "ON PAGE " + currentStep(session).getNumber() + " OUT OF " + clickItemList.size());

                addAuditToList(session, userAudit);
            }

            if (eventType.equals(AuditEventType.CLICK_NEXT)) {
                userAudit.setModuleProgressPosition("INCOMPLETE, " + (clickItemList.size() - currentStep(session).getNumber()) + " PAGES LEFT"); //NPI
                userAudit.setElementStatus(eventType.name() + "ON PAGE " + currentStep(session).getNumber() + " OUT OF " + clickItemList.size());
                addAuditToList(session, userAudit);
            }
        }
    }

    public void addAuditToList(HttpSession session, UserAudit userAudit) {
        if (Objects.isNull(clickSessions.get(getClickSessionId(session)))) {
            return;
        }

        clickSessions.get(getClickSessionId(session)).getUserAudits().add(userAudit);
    }

    private void flushToDb(ClickSession clickSession) {
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                Thread.sleep(10000L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            clickSession.getUserAudits().forEach(userAuditRepository::saveAndFlush);
        });
    }

    private String getClickSessionId(HttpSession session) {
        if (Objects.isNull(session.getAttribute("click-session-id"))) {
            return "none";
        }

        return (String) session.getAttribute("click-session-id");
    }

    private boolean isTimeOut(ClickSession clickSession) {
        return System.currentTimeMillis() - clickSession.getLastActivityTime() > CLICK_TIMEOUT_MILLIS;
    }

    private boolean isSessionLiveTimeOut(ClickSession clickSession) {
        return System.currentTimeMillis() - clickSession.getLastActivityTime() > SESSION_LIVE_TIME;
    }
}
