package hyphin.repository;

import hyphin.model.CcyPair;
import hyphin.model.EcStaticDataDaily;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EcStaticDataDailyRepository extends CrudRepository<EcStaticDataDaily, Long> {

    EcStaticDataDaily getByCcyPair(String ccyPair);

    EcStaticDataDaily getByCcyPairAndDate(String currencyPair, LocalDate date);


//    @Query("SELECT c FROM CcyPair c WHERE c.region = ?1 AND c.preferenceType = ?2 and c.displayPriority = ?3")
//    List<CcyPair> getCcyPairsByUserParams(String region, String preferenceType, String displayPriority);



    @Query("Select e from EcStaticDataDaily e where e.ccyPair = ?1 and e.date = (select max(e.date) from EcStaticDataDaily e)")
    EcStaticDataDaily getByCcyPairAndMaxDate(String currencyPair);
}
