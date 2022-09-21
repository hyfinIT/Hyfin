package hyphin.repository.currency;

import hyphin.model.currency.BlendEurUsd;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BlendEurUsdRepository extends JpaRepository<BlendEurUsd, Long> {

    @Query("SELECT max(id) FROM BlendEurUsd")
    Optional<Long> maxId();

    Optional<BlendEurUsd> findByDate(String date);
}
