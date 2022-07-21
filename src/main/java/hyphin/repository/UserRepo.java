package hyphin.repository;
import hyphin.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepo extends CrudRepository<User, String> {

    User findByUserNamePassword(String lastName,String password);

}

