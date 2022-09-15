package hyphin.repository;
import hyphin.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {

    @Query("SELECT coalesce(max(uid),0) FROM User")
    int findMax();

    User getByEmail(String email);

}

