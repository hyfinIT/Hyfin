package hyphin.repository;

import hyphin.model.GameQuestions;
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

@Repository("customUserAuditRepository")
public class CustomUserAuditRepository implements UserAuditRepository {

    private static final SimpleDateFormat jdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    @Autowired
    @Qualifier("userAuditRepository")
    private UserAuditRepository userAuditRepository;

    public synchronized UserAudit save(UserAudit userAudit, User user) {
        //TO-DO - get it from a sequence.
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
        userAudit.setDifficulty(null);
        userAudit.setCompletionTime(null);
        userAuditRepository.saveAndFlush(userAudit);
        return userAudit;
    }


    public synchronized UserAudit save(UserAudit userAudit, User user, GameQuestions gameQuestions, String answer) {
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
        userAudit.setQuidNumber(gameQuestions.getQuidNumber());
        if (answer != null && answer.equalsIgnoreCase(gameQuestions.getAnswerCorrect()))
            userAudit.setQuidNumberOutcome("CORRECT");
        else if ("default".equals(answer)) {
            userAudit.setQuidNumberOutcome("NO ANSWER");
        }
        else if (answer != null && !answer.equalsIgnoreCase(gameQuestions.getAnswerCorrect()))
            userAudit.setQuidNumberOutcome("INCORRECT");
         else
            userAudit.setQuidNumberOutcome(null);
        userAudit.setDifficulty(null);
        userAudit.setCompletionTime(null);
        userAudit.setQuidAnswer(answer);
        userAuditRepository.saveAndFlush(userAudit);
        return userAudit;
    }


    // Delegate other methods here ...

    @Override
    public List<UserAudit> findAll() {
        return userAuditRepository.findAll();
    }

    @Override
    public List<UserAudit> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<UserAudit> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public List<UserAudit> findAll(Iterable<Integer> iterable) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public int findMaxUserAudit() {
        return userAuditRepository.findMaxUserAudit();
    }

    public String findModuleID() {
        return userAuditRepository.findModuleID();
    }

    @Override
    public String findModuleName(String moduleID) {
        return userAuditRepository.findModuleName(moduleID);
    }

    @Override
    public String findElementID(String moduleID, String elementType) {
        return userAuditRepository.findElementID(moduleID,elementType);
    }

    @Override
    public String findElementType(String elementID) {
        return userAuditRepository.findElementType(elementID);
    }

    @Override
    public String findMedialocation(String elementID) {
        return userAuditRepository.findMedialocation(elementID);
    }

    @Override
    public String findLearningJourneyId() {
        return userAuditRepository.findLearningJourneyId();
    }

    @Override
    public String findLearningJourneyName() {
        return userAuditRepository.findLearningJourneyName();
    }

    @Override
    public String findGlossaryTerm(String moduleId, String learningJourney) {
        return userAuditRepository.findGlossaryTerm(moduleId, learningJourney);
    }

    public String findElementID() {
        return userAuditRepository.findElementID(findModuleID(), userAuditRepository.findElementType(findModuleID()));
    }

    @Override
    public void delete(Integer integer) {

    }

    @Override
    public void delete(UserAudit userAudit) {

    }

    @Override
    public void delete(Iterable<? extends UserAudit> iterable) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public <S extends UserAudit> S save(S s) {
        return null;
    }

    @Override
    public <S extends UserAudit> List<S> save(Iterable<S> iterable) {
        return null;
    }

    @Override
    public UserAudit findOne(Integer integer) {
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
    public <S extends UserAudit> S saveAndFlush(S s) {
        return null;
    }

    @Override
    public void deleteInBatch(Iterable<UserAudit> iterable) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public UserAudit getOne(Integer integer) {
        return null;
    }

    @Override
    public <S extends UserAudit> S findOne(Example<S> example) {
        return null;
    }

    @Override
    public <S extends UserAudit> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends UserAudit> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends UserAudit> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends UserAudit> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends UserAudit> boolean exists(Example<S> example) {
        return false;
    }
}
