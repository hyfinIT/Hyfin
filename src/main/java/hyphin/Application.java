package hyphin;

import hyphin.model.User;
import hyphin.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.io.FileNotFoundException;

@SpringBootApplication
public class Application implements CommandLineRunner {
    @Autowired
    UserRepository userRepository;

    public Application(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public static void main(String[] args) throws FileNotFoundException {
        SpringApplication.run(Application.class, args);
  }

    @Override
    public void run(String... args) throws Exception
    {
        User user = new User(1,"MVP","Abhi","Sats","abhisanj@gmail.com","abc");
        userRepository.save(user);
    }


}


