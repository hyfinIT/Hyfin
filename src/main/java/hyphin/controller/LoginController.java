package hyphin.controller;

import hyphin.model.*;
import hyphin.model.user.Role;
import hyphin.model.user.User;
import hyphin.repository.CustomUserAuditRepository;
import hyphin.repository.CustomUserRepository;
import hyphin.repository.UserRepository;
import hyphin.service.UserService;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

/**
 * Created by Abhishek Satsangi on 27/06/2022
 */
@RestController
public class LoginController {

    private static final Logger LOGGER = LogManager.getLogger(LoginController.class);

    @Autowired
    CustomUserRepository customUserRepository;

    @Autowired
    CustomUserAuditRepository customAuditUserRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @ModelAttribute(value = "register")
    public User newUser() {
        return new User();
    }

    @ModelAttribute(value = "answer")
    public Answer newAnswer() {
        return new Answer();
    }

    @ModelAttribute(value = "login")
    public Login newLogin() {
        return new Login();
    }

    @ModelAttribute(value = "video")
    public Login newVideo() {
        return new Login();
    }


    @PostMapping("/Login")
    public ModelAndView loginUsers(@ModelAttribute("login") Login login, BindingResult bindingResult,HttpSession session) {
        if(bindingResult.hasErrors()){
            System.out.println("There was a error "+bindingResult);
        }

        User user = userService.findByUserNameAndPassword(login.getEmail(),login.getPassword());
        if (user != null) {
            session.setAttribute("User-entity",user);
            return redirectTo("1");
        }
        else {
            return redirectTo("LoginFailure");
        }
    }


    @PostMapping("/Register")
    public ModelAndView registerUsers(@ModelAttribute("register") User user, BindingResult bindingResult)
    {
        if(bindingResult.hasErrors()){
            LOGGER.log(Level.ERROR,"There was a form binding error " + bindingResult);
        }
        user.setRole(Role.USER);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        customUserRepository.save(user);
        return redirectTo("RegistrationSuccess");
    }

    @GetMapping("/access_denied")
    public ModelAndView accessDenied() {
        return redirectTo("access_denied");
    }

    public ModelAndView redirectTo(String pageTo) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName(pageTo);
        return mav;
    }

}


