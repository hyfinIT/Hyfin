package hyphin.repository;

import hyphin.model.InflationRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InflationRateRepository extends JpaRepository<InflationRate, Long> {

    List<InflationRate> getAllByPosition(String position);

}
