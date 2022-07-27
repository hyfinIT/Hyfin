package hyphin.repository;

import hyphin.model.User;
import hyphin.model.UserAudit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.text.SimpleDateFormat;
import java.util.Date;

@Repository("customUserRepository")
public abstract class CustomUserRepository implements UserRepository {

    private static final SimpleDateFormat jdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    @Autowired
    @Qualifier("userRepository")
    private UserRepository userRepository;

    @Override
    public User save(User user) {
        user.setClientType("MVP");
        if(findMax() == 0)
            user.setUid(1);
        else
            user.setUid(findMax() +1);
        user.setDateTime(jdf.format(new Date()));
        userRepository.save(user);
        return user;
    }

    @Override
    public UserAudit save(UserAudit userAudit,User user) {
        //TO-DO - get it from a sequence.
        if(findMaxUserAudit() == 0)
            userAudit.setId(1);
        else
            userAudit.setId(findMaxUserAudit() +1);
        userAudit.setUid(user.getUid());
        userAudit.setLearningJourney(userRepository.findLearningJourneyName());
        userAudit.setLearningJourney(userRepository.findLearningJourneyId());
        userAudit.setModuleId(userRepository.findModuleID());
        userAudit.setModule(userRepository.findModuleName(userAudit.getModuleId()));
        userAudit.setElementStatus(userAudit.getElementId());
        userAudit.setElementPosition(userAudit.getElementId());
        userAudit.setGlossaryTerm(userRepository.findGlossaryTerm(userAudit.getModuleId(),userAudit.getLearningJourney()));
        userAudit.setMediaType(userRepository.findElementType(userAudit.getModuleId()));
        userAudit.setActivityType(userRepository.findElementType(userAudit.getModuleId()));
        userAudit.setDateTime(jdf.format(new Date()));
        userAudit.setDifficulty(null);
        userAudit.setCompletionTime(null);
        userAudit.setModuleProgressPosition(null);
        userRepository.save(userAudit,user);
        return userAudit;
   }


    @Override
    public int findMax() {
        return userRepository.findMax();
    }

    @Override
    public int findMaxUserAudit() {
        return userRepository.findMaxUserAudit();
    }

    public String findModuleID() {
        return userRepository.findModuleID();
    }

    public String findElementID() {
        return userRepository.findElementID(findModuleID(),userRepository.findElementType(findModuleID()));
    }

}
