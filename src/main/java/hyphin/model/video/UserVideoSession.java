package hyphin.model.video;

import hyphin.model.User;
import hyphin.model.UserAudit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.concurrent.ConcurrentLinkedQueue;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserVideoSession {

    private ConcurrentLinkedQueue<UserAudit> userAudits = new ConcurrentLinkedQueue<>();

    private String videoSessionId;

    private User user;

    private boolean paused;
    private long pauseEventTime;

    private boolean expired;

    private String elementId;
    private String videoLink;
    private String doneButtonLink;

    public void play() {
        paused = false;
    }

    public void pause() {
        paused = true;
        pauseEventTime = System.currentTimeMillis();
    }
}
