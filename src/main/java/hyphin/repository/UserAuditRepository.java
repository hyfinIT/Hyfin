package hyphin.repository;

import hyphin.model.GameQuestions;
import hyphin.model.User;
import hyphin.model.UserAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserAuditRepository extends JpaRepository<UserAudit, Integer> {

    @Query("SELECT coalesce(max(id),0) FROM UserAudit")
    int findMaxUserAudit();

    @Query("select moduleId from LearningJourneys where modulePosition = '1' and classification = 'MVP'")
    String findModuleID();

    @Query("select moduleName from Modules where moduleId = ?1")
    String findModuleName(String moduleID);

    @Query("select elementId from Modules where moduleId = ?1 and elementType = ?2")
    String findElementID(String moduleID, String elementType);

    @Query("select elementType from Elements where elementID = ?1")
    String findElementType(String elementID);

    @Query("select mediaLocation from Elements where elementID = ?1")
    String findMedialocation(String elementID);

    @Query("select learningJourneyId from LearningJourneys where modulePosition = '1' and classification = 'MVP'")
    String findLearningJourneyId();

    @Query("select learningJourneyName from LearningJourneys where modulePosition = '1' and classification = 'MVP'")
    String findLearningJourneyName();

    @Query("select glossaryTerm from Glossary where moduleId = ?1 and learningJourney = ?2")
    String findGlossaryTerm(String moduleId, String learningJourney);


}
