package hyphin.repository;
import hyphin.model.User;
import hyphin.model.UserAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {

    UserAudit save(UserAudit userAudit,User user);

    @Query("SELECT max(uid) FROM User")
    int findMax();

    @Query("SELECT max(id) FROM UserAudit")
    int findMaxUserAudit();

    @Query("select MODULEID from LEARNINGJOURNEYS where MODULEPOSITION = '1' and CLASSIFICATION = 'MVP'")
    String findModuleID();

    @Query("select MODULENAME from MODULES where MODULEID = ?1")
    String findModuleName(String moduleID);

    @Query("select ELEMENTID from MODULES where MODULEID = ?1 and ELEMENTPOSITION = ?2")
    String findElementID(String moduleID,String elementPosition);

    @Query("select elementtype from ELEMENTS where elementid = ?1")
    String findElementType(String elementID);

    @Query("select medialocation from ELEMENTS where elementid = ?1")
    String findMedialocation(String elementID);

    @Query("select elementStatus from ELEMENTS where elementid = ?1")
    String findElementStatus(String elementID);

    @Query("select LEARNINGJOURNEYID from LEARNINGJOURNEYS where MODULEPOSITION = '1' and CLASSIFICATION = 'MVP'")
    String findLearningJourneyId();

    @Query("select LEARNINGJOURNEYNAME from LEARNINGJOURNEYS where MODULEPOSITION = '1' and CLASSIFICATION = 'MVP'")
    String findLearningJourneyName();

    @Query("select GLOSSARYTERM from GLOSSARY where MODULEID = ?1 and LEARNINGJOURNEY = ?2")
    String findGlossaryTerm(String moduleId,String learningJourney);



    User save(User user);

}

