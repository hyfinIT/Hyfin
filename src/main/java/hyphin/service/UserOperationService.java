package hyphin.service;

import hyphin.model.User;
import hyphin.model.currency.OperationAudit;
import hyphin.repository.UserRepository;
import hyphin.repository.currency.OperationAuditRepository;
import hyphin.util.HyfinUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserOperationService {

    private final UserRepository userRepository;
    private final OperationAuditRepository operationAuditRepository;

    @Scheduled(cron = "0 0 3 * * ?")
    public void scheduledMethod() {
        List<User> inactiveUsers = userRepository.findByActive(false);
        inactiveUsers
                .stream()
                .filter(user -> !HyfinUtils.canRestoreAccount(user))
                .map(this::clearPersonalData)
                .forEach(userRepository::save);
        OperationAudit operationAudit = new OperationAudit();
        operationAudit.setName("DELETED_USERS_PROCESSING");
        operationAudit.setStatus("SUCCESS");
        operationAudit.setDateTime(LocalDateTime.now().toString());

        operationAuditRepository.save(operationAudit);
    }

    private User clearPersonalData(User user){
        user.setFirstName(null);
        user.setSurName(null);
        user.setEmail(null);
        user.setPassword(null);

        return user;
    }
}
