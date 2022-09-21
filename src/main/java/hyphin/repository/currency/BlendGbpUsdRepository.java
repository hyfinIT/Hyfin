package hyphin.repository.currency;

import hyphin.model.currency.BlendGbpUsd;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BlendGbpUsdRepository extends JpaRepository<BlendGbpUsd, Long> {

    @Query("SELECT max(id) FROM BlendGbpUsd")
    Optional<Long> maxId();

    Optional<BlendGbpUsd> findByDate(String date);

}
