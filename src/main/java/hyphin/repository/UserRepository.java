package hyphin.repository;

import hyphin.model.User;
import hyphin.model.UserAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("SELECT coalesce(max(uid),0) FROM User")
    int findMax();

    List<User> findByActive(boolean active);

    User findByEmail(String email);
}

