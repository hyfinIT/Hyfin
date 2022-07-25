package hyphin.repository;
import hyphin.model.User;
import hyphin.model.UserAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
    UserAudit save(UserAudit userAudit);

    @Query("SELECT max(uid) FROM User")
    int findMax();

}

