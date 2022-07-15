package hyphin;

import hyphin.model.User;
import hyphin.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.io.FileNotFoundException;

@SpringBootApplication
public class Application implements CommandLineRunner {
    @Autowired
    UserRepo userRepo;

    public Application(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public static void main(String[] args) throws FileNotFoundException {
        SpringApplication.run(Application.class, args);
  }

    @Override
    public void run(String... args) throws Exception
    {
        User user = new User(1,"MVP","Abhi","Sats","abhisanj@gmail.com");
        userRepo.save(user);
    }


}


