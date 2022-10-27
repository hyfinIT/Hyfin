package hyphin.model.click;

import hyphin.model.User;
import hyphin.model.UserAudit;
import lombok.Builder;
import lombok.Data;

import java.util.concurrent.ConcurrentLinkedQueue;

@Data
@Builder
public class ClickSession {

    @Builder.Default
    private ConcurrentLinkedQueue<UserAudit> userAudits = new ConcurrentLinkedQueue<>();
    private String activitySessionId;
    private User user;
    @Builder.Default
    private boolean expired = false;
    @Builder.Default
    private long lastActivityTime = System.currentTimeMillis();
    @Builder.Default
    private int stepNumber = 1;

    public void nextStep() {
        this.stepNumber = stepNumber + 1;
    };

    public void prevStep() {
        this.stepNumber = stepNumber - 1;
    };
}
