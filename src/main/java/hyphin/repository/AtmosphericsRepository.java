package hyphin.repository;

import hyphin.model.Atmospherics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AtmosphericsRepository extends JpaRepository<Atmospherics, Long> {
}
