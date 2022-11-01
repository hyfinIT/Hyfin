package hyphin.repository;

import hyphin.model.InflationRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InflationRateRepository extends JpaRepository<InflationRate, Long> {
}
