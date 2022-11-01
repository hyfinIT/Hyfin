package hyphin.repository;

import hyphin.model.FlagIcon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FlagIconRepository extends JpaRepository<FlagIcon, Long> {
}
