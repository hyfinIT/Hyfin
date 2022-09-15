package hyphin.service;

import hyphin.model.user.User;
import hyphin.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService{
    @Autowired
    private UserRepository userRepository;

    public User findByUserNameAndPassword(String userName, String password) {
        List<User> users = (List<User>) userRepository.findAll();
        for (User user : users) {
            if(user != null && user.getEmail() != null && user.getEmail().equalsIgnoreCase(userName) && user.getPassword() != null && user.getPassword().equals(password))
                return user;
        }
        return null;
    }

}

