package hyphin.controller;

import hyphin.model.Login;
import hyphin.model.User;
import hyphin.model.UserAudit;
import hyphin.repository.CustomUserRepository;
import hyphin.repository.UserRepository;
import hyphin.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

/**
 * Created by Abhishek Satsangi on 27/06/2022
 */
@RestController
public class LoginController {

    private static final Logger LOGGER = LogManager.getLogger(LoginController.class);

    @Autowired
    CustomUserRepository customUserRepository;

    @Autowired
    UserRepository userRepository;

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

    @ModelAttribute(value = "video")
    public Login newVideo()
    {
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

    @PostMapping("/Video")
    public ModelAndView auditUsers(HttpSession session) {
        SimpleDateFormat jdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        User user = (User) session.getAttribute("User-entity");
        UserAudit userAudit = new UserAudit();
        userAudit.setUid(user.getUid());
        userAudit.setActivityType("CLICK");
        userAudit.setMediaType("VIDEO");
        userAudit.setDateTime(jdf.format(new Date()));
        userAudit.setGlossaryTerm(null);
        userAudit.setDifficulty(null);
        userAudit.setCompletionTime(null);
        userAudit.setElementId(null);
        userAudit.setElementPosition(null);
        userAudit.setElementStatus(null);
        userAudit.setLearningJourney(null);
        userAudit.setLearningJourneyId(null);
        userAudit.setModuleId(null);
        userAudit.setModuleProgressPosition(null);
        userAudit.setModule(null);
        userRepository.save(userAudit);
        ModelAndView mav = new ModelAndView();
        mav.setViewName("9");
        return mav;
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


