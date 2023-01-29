package hyphin.repository;

import hyphin.model.CcyPair;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CcyPairRepository extends CrudRepository<CcyPair, Long> {

    @Query("SELECT c FROM CcyPair c WHERE c.region = ?1 AND c.preferenceType = ?2 and c.displayPriority = ?3")
    List<CcyPair> getCcyPairsByUserParams(String region, String preferenceType, String displayPriority);

    @Query("SELECT c FROM CcyPair c WHERE c.region = 'EMEA' AND c.preferenceType = 'Global' and c.displayPriority = 'Default' and c.currencyPair = ?1")
    CcyPair getDefaultByName(String ccyPairName);
}
