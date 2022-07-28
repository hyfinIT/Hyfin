package hyphin.controller;

import hyphin.model.Login;
import hyphin.model.User;
import hyphin.model.UserAudit;
import hyphin.repository.CustomUserAuditRepository;
import hyphin.repository.CustomUserRepository;
import hyphin.repository.UserRepository;
import hyphin.service.UserService;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
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
    CustomUserAuditRepository customAuditUserRepository;

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
            return redirectTo("1");
        }
        else {
            return redirectTo("LoginFailure");
        }
    }

    @PostMapping("/Video")
    public ModelAndView auditVideos(HttpSession session) {
        User user = (User) session.getAttribute("User-entity");
        UserAudit userAudit = new UserAudit();
        userAudit.setActivityType("VIDEO");
        userAudit.setElementId(customAuditUserRepository.findElementID(customAuditUserRepository.findModuleID(),"VIDEO"));
        customAuditUserRepository.save(userAudit,user);
        return redirectTo("9");
    }


    @PostMapping("/ClickThrough")
    public ModelAndView auditClickThroughs(HttpSession session) {
        User user = (User) session.getAttribute("User-entity");
        UserAudit userAudit = new UserAudit();
        userAudit.setActivityType("CLICKTHROUGH");
        userAudit.setElementId(customAuditUserRepository.findElementID(customAuditUserRepository.findModuleID(),"CLICKTHROUGH"));
        customAuditUserRepository.save(userAudit,user);
        return redirectTo("5_8");
    }

    @PostMapping("/Games")
    public ModelAndView auditGames(HttpSession session) {
        User user = (User) session.getAttribute("User-entity");
        UserAudit userAudit = new UserAudit();
        userAudit.setActivityType("IN MODULE GAME");
        userAudit.setElementId(customAuditUserRepository.findElementID(customAuditUserRepository.findModuleID(),"IN MODULE GAME"));
        customAuditUserRepository.save(userAudit,user);
        return redirectTo("5_8");
    }

    @PostMapping("/PrevOrNext")
    public ModelAndView auditPrevOrNext(HttpSession session) {
        User user = (User) session.getAttribute("User-entity");
        UserAudit userAudit = new UserAudit();
        userAudit.setActivityType("CLICKTHROUGH");
        customAuditUserRepository.save(userAudit,user);
        return redirectTo("5_8");
    }

    @PostMapping("/Register")
    public ModelAndView registerUsers(@ModelAttribute("register") User user, BindingResult bindingResult)
    {
        if(bindingResult.hasErrors()){
            LOGGER.log(Level.ERROR,"There was a form binding error " + bindingResult);
        }
        customUserRepository.save(user);
        return redirectTo("RegistrationSuccess");
    }

    public ModelAndView redirectTo(String pageTo) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName(pageTo);
        return mav;
    }

}


