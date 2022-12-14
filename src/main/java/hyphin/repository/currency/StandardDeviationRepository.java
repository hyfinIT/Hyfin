package hyphin.repository.currency;

import hyphin.model.currency.StandardDeviation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StandardDeviationRepository extends JpaRepository<StandardDeviation, Long> {

    @Query("SELECT max(id) FROM StandardDeviation")
    Optional<Long> maxId();
}
