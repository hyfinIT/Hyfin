package hyphin.service;

import hyphin.model.User;
import hyphin.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class UserService {

    private static final SimpleDateFormat jdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    @Autowired
    private UserRepository userRepository;

    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(8);

    public User findByUserNameAndPassword(String userName, String password) {
        List<User> users = (List<User>) userRepository.findAll();
        for (User user : users) {
            if (user != null && user.getEmail() != null && user.getEmail().equalsIgnoreCase(userName) && user.getPassword() != null &&
                    (user.getPassword().equals(password) || passwordEncoder.matches(password, user.getPassword())))
                return user;
        }
        return null;
    }

    public User save(User user) {
        user.setClientType("MVP");
        if (userRepository.findMax() == 0)
            user.setUid(1);
        else
            user.setUid(userRepository.findMax() + 1);
        user.setDateTime(jdf.format(new Date()));
        user.setActive(true);
        user.setRegion("EMEA");
        user.setPreferenceType("Global");
        user.setDisplayPriority("Default");


        userRepository.saveAndFlush(user);
        return user;
    }

}

