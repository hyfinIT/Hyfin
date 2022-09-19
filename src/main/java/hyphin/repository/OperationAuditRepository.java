package hyphin.repository;

import hyphin.model.currency.OperationAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface OperationAuditRepository extends JpaRepository<OperationAudit, Long> {

    @Query("SELECT max(id) FROM OperationAudit")
    Optional<Long> maxId();
}
