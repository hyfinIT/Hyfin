package hyphin.repository;

import hyphin.model.User;
import hyphin.model.UserAudit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Repository("customUserRepository")
public abstract class CustomUserRepository implements UserRepository {

    private static final SimpleDateFormat jdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    @Autowired
    @Qualifier("userRepository") // inject Spring implementation here
    private UserRepository userRepository;

    @Override
    public User save(User user) {

        user.setClientType("MVP");
        if(findMax() == 0) {
            user.setUid(1);
        }
        else {
            user.setUid(findMax() +1);
        }
        user.setDateTime(jdf.format(new Date()));
        userRepository.save(user);
        return user;
    }

    @Override
    public UserAudit save(UserAudit userAudit,User user) {

        //TO-DO - get it from a sequence.
        if(findMaxUserAudit() == 0) {
            userAudit.setId(1);
        }

        else {
            userAudit.setId(findMaxUserAudit() +1);
        }
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



    // Delegate other methods here ...

    @Override
    public List<User> findAll() {
        return null;
    }

    @Override
    public List<User> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public List<User> findAll(Iterable<Integer> iterable) {
        return null;
    }

    @Override
    public long count() {
        return 0;
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
        return userRepository.findElementID(findModuleID());
    }

    @Override
    public void delete(Integer integer) {

    }

    @Override
    public void delete(User user) {

    }

    @Override
    public void delete(Iterable<? extends User> iterable) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public <S extends User> List<S> save(Iterable<S> iterable) {
        return null;
    }

    @Override
    public User findOne(Integer integer) {
        return null;
    }

    @Override
    public boolean exists(Integer integer) {
        return false;
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends User> S saveAndFlush(S s) {
        return null;
    }

    @Override
    public void deleteInBatch(Iterable<User> iterable) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public User getOne(Integer integer) {
        return null;
    }

    @Override
    public <S extends User> S findOne(Example<S> example) {
        return null;
    }

    @Override
    public <S extends User> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends User> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends User> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends User> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends User> boolean exists(Example<S> example) {
        return false;
    }
}
