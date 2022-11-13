package hyphin.repository.currency;

import hyphin.model.currency.CurrencyRatesBlend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CurrencyRatesBlendRepository extends JpaRepository<CurrencyRatesBlend, Long> {

    Optional<CurrencyRatesBlend> findByDate(String date);

    Optional<CurrencyRatesBlend> findByDateAndCcyPair(String date, String ccyPair);
}
