package hyphin.repository;

import hyphin.model.EcStaticDataDaily;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EcStaticDataDailyRepository extends CrudRepository<EcStaticDataDaily, Long> {

    EcStaticDataDaily getByCcyPair(String currencyPair);

}
