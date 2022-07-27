package hyphin.repository;
import hyphin.model.User;
import hyphin.model.UserAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {

    User save(User user);

    UserAudit save(UserAudit userAudit,User user);

    @Query("SELECT max(uid) FROM User")
    int findMax();

    @Query("SELECT max(id) FROM UserAudit")
    int findMaxUserAudit();

    @Query("select moduleId from LearningJourneys where modulePosition = '1' and classification = 'MVP'")
    String findModuleID();

    @Query("select moduleName from Modules where moduleId = ?1")
    String findModuleName(String moduleID);

    @Query("select elementId from Modules where moduleId = ?1 and elementPosition = ?2")
    String findElementID(String moduleID,String elementPosition);

    @Query("select elementType from Elements where elementID = ?1")
    String findElementType(String elementID);

    @Query("select mediaLocation from Elements where elementID = ?1")
    String findMedialocation(String elementID);

    @Query("select learningJourneyId from LearningJourneys where modulePosition = '1' and classification = 'MVP'")
    String findLearningJourneyId();

    @Query("select learningJourneyName from LearningJourneys where modulePosition = '1' and classification = 'MVP'")
    String findLearningJourneyName();

    @Query("select glossaryTerm from Glossary where moduleId = ?1 and learningJourney = ?2")
    String findGlossaryTerm(String moduleId,String learningJourney);


}

