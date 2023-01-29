package hyphin.repository.currency;

import hyphin.model.currency.OperationAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OperationAuditRepository extends JpaRepository<OperationAudit, Long> {

    @Query( nativeQuery = true, value = "Select status from hyfin.public.OPSAUDIT where DATETIME > ?1 and DATETIME < dateadd(day, 1, to_date(?1)) and name = ?2")
    String findOperationByDateAndName(String date, String name);
}
