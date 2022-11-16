package hyphin.repository;

import hyphin.model.Atmospherics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AtmosphericsRepository extends JpaRepository<Atmospherics, Long> {

    List<Atmospherics> getAllByCcyAndStage(String ccy, String stage);

}
