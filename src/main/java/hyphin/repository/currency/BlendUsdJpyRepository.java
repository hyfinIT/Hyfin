package hyphin.repository.currency;

import hyphin.model.currency.BlendUsdJpy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BlendUsdJpyRepository extends JpaRepository<BlendUsdJpy, Long> {

    @Query("SELECT max(id) FROM BlendUsdJpy")
    Optional<Long> maxId();

    Optional<BlendUsdJpy> findByDate(String date);

}
