package hyphin.repository.currency;

import hyphin.model.currency.OperationAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OperationAuditRepository extends JpaRepository<OperationAudit, Long> {

    @Query("SELECT max(id) FROM OperationAudit")
    Optional<Long> maxId();
}
