package hyphin.repository;

import hyphin.model.currency.CurrencyExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CurrencyExchangeRateRepository extends JpaRepository<CurrencyExchangeRate, Long> {

    @Query("SELECT max(id) FROM CurrencyExchangeRate")
    Optional<Long> maxId();

    @Query("select cer from CurrencyExchangeRate cer where cer.date = ?1 and sourceRef = ?2")
    List<CurrencyExchangeRate> getYesterdayEntry(String date, String sourceRef);
}
