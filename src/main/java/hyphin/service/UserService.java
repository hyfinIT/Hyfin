package hyphin.service;

import hyphin.model.User;
import hyphin.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService{
    @Autowired
    private UserRepository userRepository;

    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(8);

    public User findByUserNameAndPassword(String userName, String password) {
        List<User> users = (List<User>) userRepository.findAll();
        for (User user : users) {
            if(user != null && user.getEmail() != null && user.getEmail().equalsIgnoreCase(userName) && user.getPassword() != null &&
                    (user.getPassword().equals(password) || passwordEncoder.matches(password, user.getPassword())))
                return user;
        }
        return null;
    }

}

