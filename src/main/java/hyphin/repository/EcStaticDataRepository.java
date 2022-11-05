package hyphin.repository;

import hyphin.model.EcStaticData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EcStaticDataRepository extends JpaRepository<EcStaticData, Long> {
}
