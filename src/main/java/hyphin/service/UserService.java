package hyphin.service;

import hyphin.model.User;
import hyphin.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService{
    @Autowired
    private UserRepo userRepo;

    public User findByUserNamePassword(String userName, String password) {
        User user = userRepo.findByUserNamePassword(userName,password);
        return user;
    }

}

