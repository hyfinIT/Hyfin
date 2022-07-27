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
    public ModelAndView auditVideos(HttpSession session) {
        SimpleDateFormat jdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        User user = (User) session.getAttribute("User-entity");
        UserAudit userAudit = new UserAudit();
        userAudit.setId(userRepository.findMaxUserAudit());
        userAudit.setUid(user.getUid());
        userAudit.setActivityType("PLAY VIDEO");
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
        customUserRepository.save(userAudit);
        ModelAndView mav = new ModelAndView();
        mav.setViewName("9");
        return mav;
    }


    @PostMapping("/ClickThrough")
    public ModelAndView auditClickThroughs(HttpSession session) {
        SimpleDateFormat jdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        User user = (User) session.getAttribute("User-entity");
        UserAudit userAudit = new UserAudit();
        userAudit.setId(userRepository.findMaxUserAudit());
        userAudit.setUid(user.getUid());
        userAudit.setActivityType("CLICK");
        userAudit.setMediaType("CLICKTHROUGH");
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
        customUserRepository.save(userAudit);
        ModelAndView mav = new ModelAndView();
        mav.setViewName("5_8");
        return mav;
    }

    @PostMapping("/Games")
    public ModelAndView auditGames(HttpSession session) {
        SimpleDateFormat jdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        User user = (User) session.getAttribute("User-entity");
        UserAudit userAudit = new UserAudit();
        userAudit.setId(userRepository.findMaxUserAudit());
        userAudit.setUid(user.getUid());
        userAudit.setActivityType("CLICK");
        userAudit.setMediaType("INMODULEGAME");
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
        customUserRepository.save(userAudit);
        ModelAndView mav = new ModelAndView();
        mav.setViewName("5_8");
        return mav;
    }

    @PostMapping("/PrevOrNext")
    public ModelAndView auditPrevOrNext(HttpSession session) {
        SimpleDateFormat jdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        User user = (User) session.getAttribute("User-entity");
        UserAudit userAudit = new UserAudit();
        userAudit.setId(userRepository.findMaxUserAudit());
        userAudit.setUid(user.getUid());
        userAudit.setActivityType("NEXT");
        userAudit.setMediaType("CLICKTHROUGH");
        userAudit.setDateTime(jdf.format(new Date()));
        userAudit.setGlossaryTerm(null);
        userAudit.setDifficulty(null);
        userAudit.setCompletionTime(null);
        userAudit.setElementId(null);
        userAudit.setElementPosition(null);
        userAudit.setElementStatus(null);
        userAudit.setLearningJourney(null);
        userAudit.setLearningJourneyId(null);
        userAudit.setModuleId(userRepository.findModuleID());
        userAudit.setModuleProgressPosition(null);
        userAudit.setModule(null);
        customUserRepository.save(userAudit);
        ModelAndView mav = new ModelAndView();
        mav.setViewName("5_8");
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


