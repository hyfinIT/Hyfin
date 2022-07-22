package hyphin.controller;

import hyphin.model.Login;
import hyphin.model.User;
import hyphin.repository.CustomUserRepository;
import hyphin.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;


/**
 * Created by Abhishek Satsangi on 27/06/2022
 */
@RestController
public class LoginController {

    private static final Logger LOGGER = LogManager.getLogger(LoginController.class);

    @Autowired
    CustomUserRepository customUserRepository;

    @Autowired
    UserService userService;

    @ModelAttribute(value = "register")
    public User newUser()
    {
        return new User();
    }

    @ModelAttribute(value = "login")
    public Login newLogin()
    {
        return new Login();
    }


    @PostMapping("/Login")
    public ModelAndView loginUsers(@ModelAttribute("login") Login login, BindingResult bindingResult) {

        if(bindingResult.hasErrors()){
            System.out.println("There was a error "+bindingResult);
        }
        User user = userService.findByUserNameAndPassword(login.getEmail(),login.getPassword());
        if (user != null) {
            ModelAndView mav = new ModelAndView();
            mav.setViewName("1");
            return mav;
        }
        else {
            ModelAndView mav = new ModelAndView();
            mav.setViewName("LoginFailure");
            return mav;
        }
    }

    @PostMapping("/Register")
    public ModelAndView registerUsers(@ModelAttribute("register") User user, BindingResult bindingResult)
    {
        if(bindingResult.hasErrors()){
            System.out.println("There was a error "+bindingResult);
        }
        customUserRepository.save(user);
        ModelAndView mav = new ModelAndView();
        mav.setViewName("RegistrationSuccess");
        return mav;
    }
}


