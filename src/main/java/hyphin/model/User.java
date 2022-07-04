package hyphin.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int id;
    String clientType;
    User() {}
    public User(int id, String clientType)
    {
        this.id = id;
        this.clientType = clientType;
    }
}
