package hyphin;

import hyphin.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import java.io.FileNotFoundException;

@SpringBootApplication
@EnableWebSecurity
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

    }


}


