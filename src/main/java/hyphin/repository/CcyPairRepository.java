package hyphin.repository;

import hyphin.model.CcyPair;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CcyPairRepository extends CrudRepository<CcyPair, Long> {
}
