package hyphin.repository;

import hyphin.model.GameQuestions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GamesRepository extends JpaRepository<GameQuestions, String> {

    Optional<GameQuestions> findByQuidNumber(String questionId);
}
