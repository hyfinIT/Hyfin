package hyphin.controller;

import hyphin.model.*;
import hyphin.repository.CustomUserAuditRepository;
import hyphin.repository.CustomUserRepository;
import hyphin.repository.GamesRepository;
import hyphin.repository.UserRepository;
import hyphin.service.UserService;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

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
    GamesRepository gamesRepository;

    private GameQuestions gameQuestion;

    @Autowired
    UserService userService;

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

    @PostMapping("/Video")
    public ModelAndView auditVideos(HttpSession session) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        System.out.println(dtf.format(now));
        User user = (User) session.getAttribute("User-entity");
        UserAudit userAudit = new UserAudit();
        userAudit.setModuleId(customAuditUserRepository.findModuleID());
        userAudit.setElementId(customAuditUserRepository.findElementID(customAuditUserRepository.findModuleID(),"VIDEO"));
        customAuditUserRepository.save(userAudit,user);
        System.out.println(dtf.format(LocalDateTime.now()));
        return redirectTo("9");

    }


    @PostMapping("/ClickThrough")
    public ModelAndView auditClickThroughs(HttpSession session) {
           DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
           LocalDateTime now = LocalDateTime.now();
           System.out.println(dtf.format(now));
           User user = (User) session.getAttribute("User-entity");
           UserAudit userAudit = new UserAudit();
           userAudit.setActivityType("CLICKTHROUGH");
           userAudit.setElementStatus("CLICKTHROUGH STARTED");
           userAudit.setModuleProgressPosition("INCOMPLETE, 18 PAGES LEFT");
           userAudit.setElementId(customAuditUserRepository.findElementID(customAuditUserRepository.findModuleID(), "CLICKTHROUGH"));
           customAuditUserRepository.save(userAudit, user);
           LocalDateTime now1 = LocalDateTime.now();
           System.out.println(dtf.format(now1));
           return redirectTo("5_8");
    }

    @PostMapping("/ClickThroughNext1")
    public ModelAndView auditClickThroughNext1(HttpSession session) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        System.out.println(dtf.format(now));

        User user = (User) session.getAttribute("User-entity");
        UserAudit userAudit = new UserAudit();
        userAudit.setElementStatus("ON PAGE 1 OUT OF 18");
        userAudit.setModuleProgressPosition("INCOMPLETE, 17 PAGES LEFT");
        userAudit.setElementId(customAuditUserRepository.findElementID(customAuditUserRepository.findModuleID(),"CLICKTHROUGH"));
        customAuditUserRepository.save(userAudit,user);
        LocalDateTime now1 = LocalDateTime.now();
        System.out.println(dtf.format(now1));
        return redirectTo("5_8_1");
    }

    @PostMapping("/ClickThroughPrev1")
    public ModelAndView auditClickThroughPrev1(HttpSession session) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        System.out.println(dtf.format(now));
        User user = (User) session.getAttribute("User-entity");
        UserAudit userAudit = new UserAudit();
        userAudit.setElementStatus("ON PAGE 1 OUT OF 18");
        userAudit.setModuleProgressPosition("INCOMPLETE, 17 PAGES LEFT");
        userAudit.setElementId(customAuditUserRepository.findElementID(customAuditUserRepository.findModuleID(),"CLICKTHROUGH"));
        LocalDateTime now1 = LocalDateTime.now();
        System.out.println(dtf.format(now1));
        customAuditUserRepository.save(userAudit,user);
        return redirectTo("4");
    }


    @PostMapping("/ClickThroughNext2")
    public ModelAndView auditClickThroughNext2(HttpSession session) {
        User user = (User) session.getAttribute("User-entity");
        UserAudit userAudit = new UserAudit();
        userAudit.setElementStatus("ON PAGE 2 OUT OF 18");
        userAudit.setModuleProgressPosition("INCOMPLETE, 16 PAGES LEFT");
        userAudit.setElementId(customAuditUserRepository.findElementID(customAuditUserRepository.findModuleID(),"CLICKTHROUGH"));
        customAuditUserRepository.save(userAudit,user);
        return redirectTo("5_8_2");
    }

    @PostMapping("/ClickThroughPrev2")
    public ModelAndView auditClickThroughPrev2(HttpSession session) {
        User user = (User) session.getAttribute("User-entity");
        UserAudit userAudit = new UserAudit();
        userAudit.setElementStatus("ON PAGE 2 OUT OF 18");
        userAudit.setModuleProgressPosition("INCOMPLETE, 16 PAGES LEFT");
        userAudit.setElementId(customAuditUserRepository.findElementID(customAuditUserRepository.findModuleID(),"CLICKTHROUGH"));
        customAuditUserRepository.save(userAudit,user);
        return redirectTo("5_8");
    }

    @PostMapping("/ClickThroughNext3")
    public ModelAndView auditClickThroughNext3(HttpSession session) {
        User user = (User) session.getAttribute("User-entity");
        UserAudit userAudit = new UserAudit();
        userAudit.setElementStatus("ON PAGE 3 OUT OF 18");
        userAudit.setModuleProgressPosition("INCOMPLETE, 15 PAGES LEFT");
        userAudit.setElementId(customAuditUserRepository.findElementID(customAuditUserRepository.findModuleID(),"CLICKTHROUGH"));
        customAuditUserRepository.save(userAudit,user);
        return redirectTo("5_8_3");
    }

    @PostMapping("/ClickThroughPrev3")
    public ModelAndView auditClickThroughPrev3(HttpSession session) {
        User user = (User) session.getAttribute("User-entity");
        UserAudit userAudit = new UserAudit();
        userAudit.setElementStatus("ON PAGE 3 OUT OF 18");
        userAudit.setModuleProgressPosition("INCOMPLETE, 15 PAGES LEFT");
        userAudit.setElementId(customAuditUserRepository.findElementID(customAuditUserRepository.findModuleID(),"CLICKTHROUGH"));
        customAuditUserRepository.save(userAudit,user);
        return redirectTo("5_8_1");
    }

    @PostMapping("/ClickThroughNext4")
    public ModelAndView auditClickThroughNext4(HttpSession session) {
        User user = (User) session.getAttribute("User-entity");
        UserAudit userAudit = new UserAudit();
        userAudit.setElementStatus("ON PAGE 4 OUT OF 18");
        userAudit.setModuleProgressPosition("INCOMPLETE, 14 PAGES LEFT");
        userAudit.setElementId(customAuditUserRepository.findElementID(customAuditUserRepository.findModuleID(),"CLICKTHROUGH"));
        customAuditUserRepository.save(userAudit,user);
        return redirectTo("5_8_4");
    }

    @PostMapping("/ClickThroughPrev4")
    public ModelAndView auditClickThroughPrev4(HttpSession session) {
        User user = (User) session.getAttribute("User-entity");
        UserAudit userAudit = new UserAudit();
        userAudit.setElementStatus("ON PAGE 4 OUT OF 18");
        userAudit.setModuleProgressPosition("INCOMPLETE, 14 PAGES LEFT");
        userAudit.setElementId(customAuditUserRepository.findElementID(customAuditUserRepository.findModuleID(),"CLICKTHROUGH"));
        customAuditUserRepository.save(userAudit,user);
        return redirectTo("5_8_2");
    }

    @PostMapping("/ClickThroughNext5")
    public ModelAndView auditClickThroughNext5(HttpSession session) {
        User user = (User) session.getAttribute("User-entity");
        UserAudit userAudit = new UserAudit();
        userAudit.setElementStatus("ON PAGE 5 OUT OF 18");
        userAudit.setModuleProgressPosition("INCOMPLETE, 13 PAGES LEFT");
        userAudit.setElementId(customAuditUserRepository.findElementID(customAuditUserRepository.findModuleID(),"CLICKTHROUGH"));
        customAuditUserRepository.save(userAudit,user);
        return redirectTo("5_8_5");
    }

    @PostMapping("/ClickThroughPrev5")
    public ModelAndView auditClickThroughPrev5(HttpSession session) {
        User user = (User) session.getAttribute("User-entity");
        UserAudit userAudit = new UserAudit();
        userAudit.setElementStatus("ON PAGE 5 OUT OF 18");
        userAudit.setModuleProgressPosition("INCOMPLETE, 13 PAGES LEFT");
        userAudit.setElementId(customAuditUserRepository.findElementID(customAuditUserRepository.findModuleID(),"CLICKTHROUGH"));
        customAuditUserRepository.save(userAudit,user);
        return redirectTo("5_8_3");
    }

    @PostMapping("/ClickThroughNext6")
    public ModelAndView auditClickThroughNext6(HttpSession session) {
        User user = (User) session.getAttribute("User-entity");
        UserAudit userAudit = new UserAudit();
        userAudit.setElementStatus("ON PAGE 6 OUT OF 18");
        userAudit.setModuleProgressPosition("INCOMPLETE, 12 PAGES LEFT");
        userAudit.setElementId(customAuditUserRepository.findElementID(customAuditUserRepository.findModuleID(),"CLICKTHROUGH"));
        customAuditUserRepository.save(userAudit,user);
        return redirectTo("5_8_6");
    }

    @PostMapping("/ClickThroughPrev6")
    public ModelAndView auditClickThroughPrev6(HttpSession session) {
        User user = (User) session.getAttribute("User-entity");
        UserAudit userAudit = new UserAudit();
        userAudit.setElementStatus("ON PAGE 6 OUT OF 18");
        userAudit.setModuleProgressPosition("INCOMPLETE, 12 PAGES LEFT");
        userAudit.setElementId(customAuditUserRepository.findElementID(customAuditUserRepository.findModuleID(),"CLICKTHROUGH"));
        customAuditUserRepository.save(userAudit,user);
        return redirectTo("5_8_4");
    }

    @PostMapping("/ClickThroughNext7")
    public ModelAndView auditClickThroughNext7(HttpSession session) {
        User user = (User) session.getAttribute("User-entity");
        UserAudit userAudit = new UserAudit();
        userAudit.setElementStatus("ON PAGE 7 OUT OF 18");
        userAudit.setModuleProgressPosition("INCOMPLETE, 11 PAGES LEFT");
        userAudit.setElementId(customAuditUserRepository.findElementID(customAuditUserRepository.findModuleID(),"CLICKTHROUGH"));
        customAuditUserRepository.save(userAudit,user);
        return redirectTo("5_8_7");
    }

    @PostMapping("/ClickThroughPrev7")
    public ModelAndView auditClickThroughPrev7(HttpSession session) {
        User user = (User) session.getAttribute("User-entity");
        UserAudit userAudit = new UserAudit();
        userAudit.setElementStatus("ON PAGE 7 OUT OF 18");
        userAudit.setModuleProgressPosition("INCOMPLETE, 11 PAGES LEFT");
        userAudit.setElementId(customAuditUserRepository.findElementID(customAuditUserRepository.findModuleID(),"CLICKTHROUGH"));
        customAuditUserRepository.save(userAudit,user);
        return redirectTo("5_8_5");
    }

    @PostMapping("/ClickThroughNext8")
    public ModelAndView auditClickThroughNext8(HttpSession session) {
        User user = (User) session.getAttribute("User-entity");
        UserAudit userAudit = new UserAudit();
        userAudit.setElementStatus("ON PAGE 8 OUT OF 18");
        userAudit.setModuleProgressPosition("INCOMPLETE, 10 PAGES LEFT");
        userAudit.setElementId(customAuditUserRepository.findElementID(customAuditUserRepository.findModuleID(),"CLICKTHROUGH"));
        customAuditUserRepository.save(userAudit,user);
        return redirectTo("5_8_8");
    }

    @PostMapping("/ClickThroughPrev8")
    public ModelAndView auditClickThroughPrev8(HttpSession session) {
        User user = (User) session.getAttribute("User-entity");
        UserAudit userAudit = new UserAudit();
        userAudit.setElementStatus("ON PAGE 8 OUT OF 18");
        userAudit.setModuleProgressPosition("INCOMPLETE, 10 PAGES LEFT");
        userAudit.setElementId(customAuditUserRepository.findElementID(customAuditUserRepository.findModuleID(),"CLICKTHROUGH"));
        customAuditUserRepository.save(userAudit,user);
        return redirectTo("5_8_6");
    }


    @PostMapping("/ClickThroughNext9")
    public ModelAndView auditClickThroughNext9(HttpSession session) {
        User user = (User) session.getAttribute("User-entity");
        UserAudit userAudit = new UserAudit();
        userAudit.setElementStatus("ON PAGE 9 OUT OF 18");
        userAudit.setModuleProgressPosition("INCOMPLETE, 9 PAGES LEFT");
        userAudit.setElementId(customAuditUserRepository.findElementID(customAuditUserRepository.findModuleID(),"CLICKTHROUGH"));
        customAuditUserRepository.save(userAudit,user);
        return redirectTo("5_8_9");
    }

    @PostMapping("/ClickThroughPrev9")
    public ModelAndView auditClickThroughPrev9(HttpSession session) {
        User user = (User) session.getAttribute("User-entity");
        UserAudit userAudit = new UserAudit();
        userAudit.setElementStatus("ON PAGE 9 OUT OF 18");
        userAudit.setModuleProgressPosition("INCOMPLETE, 9 PAGES LEFT");
        userAudit.setElementId(customAuditUserRepository.findElementID(customAuditUserRepository.findModuleID(),"CLICKTHROUGH"));
        customAuditUserRepository.save(userAudit,user);
        return redirectTo("5_8_7");
    }

    @PostMapping("/ClickThroughNext10")
    public ModelAndView auditClickThroughNext10(HttpSession session) {
        User user = (User) session.getAttribute("User-entity");
        UserAudit userAudit = new UserAudit();
        userAudit.setElementStatus("ON PAGE 10 OUT OF 18");
        userAudit.setModuleProgressPosition("INCOMPLETE, 8 PAGES LEFT");
        userAudit.setElementId(customAuditUserRepository.findElementID(customAuditUserRepository.findModuleID(),"CLICKTHROUGH"));
        customAuditUserRepository.save(userAudit,user);
        return redirectTo("5_8_10");
    }

    @PostMapping("/ClickThroughPrev10")
    public ModelAndView auditClickThroughPrev10(HttpSession session) {
        User user = (User) session.getAttribute("User-entity");
        UserAudit userAudit = new UserAudit();
        userAudit.setElementStatus("ON PAGE 10 OUT OF 18");
        userAudit.setModuleProgressPosition("INCOMPLETE, 8 PAGES LEFT");
        userAudit.setElementId(customAuditUserRepository.findElementID(customAuditUserRepository.findModuleID(),"CLICKTHROUGH"));
        customAuditUserRepository.save(userAudit,user);
        return redirectTo("5_8_8");
    }

    @PostMapping("/ClickThroughNext11")
    public ModelAndView auditClickThroughNext11(HttpSession session) {
        User user = (User) session.getAttribute("User-entity");
        UserAudit userAudit = new UserAudit();
        userAudit.setElementStatus("ON PAGE 11 OUT OF 18");
        userAudit.setModuleProgressPosition("INCOMPLETE, 7 PAGES LEFT");
        userAudit.setElementId(customAuditUserRepository.findElementID(customAuditUserRepository.findModuleID(),"CLICKTHROUGH"));
        customAuditUserRepository.save(userAudit,user);
        return redirectTo("5_8_11");
    }

    @PostMapping("/ClickThroughPrev11")
    public ModelAndView auditClickThroughPrev11(HttpSession session) {
        User user = (User) session.getAttribute("User-entity");
        UserAudit userAudit = new UserAudit();
        userAudit.setElementStatus("ON PAGE 11 OUT OF 18");
        userAudit.setModuleProgressPosition("INCOMPLETE, 7 PAGES LEFT");
        userAudit.setElementId(customAuditUserRepository.findElementID(customAuditUserRepository.findModuleID(),"CLICKTHROUGH"));
        customAuditUserRepository.save(userAudit,user);
        return redirectTo("5_8_9");
    }

    @PostMapping("/ClickThroughNext12")
    public ModelAndView auditClickThroughNext12(HttpSession session) {
        User user = (User) session.getAttribute("User-entity");
        UserAudit userAudit = new UserAudit();
        userAudit.setElementStatus("ON PAGE 12 OUT OF 18");
        userAudit.setModuleProgressPosition("INCOMPLETE, 6 PAGES LEFT");
        userAudit.setElementId(customAuditUserRepository.findElementID(customAuditUserRepository.findModuleID(),"CLICKTHROUGH"));
        customAuditUserRepository.save(userAudit,user);
        return redirectTo("5_8_12");
    }

    @PostMapping("/ClickThroughPrev12")
    public ModelAndView auditClickThroughPrev12(HttpSession session) {
        User user = (User) session.getAttribute("User-entity");
        UserAudit userAudit = new UserAudit();
        userAudit.setElementStatus("ON PAGE 12 OUT OF 18");
        userAudit.setModuleProgressPosition("INCOMPLETE, 6 PAGES LEFT");
        userAudit.setElementId(customAuditUserRepository.findElementID(customAuditUserRepository.findModuleID(),"CLICKTHROUGH"));
        customAuditUserRepository.save(userAudit,user);
        return redirectTo("5_8_10");
    }

    @PostMapping("/ClickThroughNext13")
    public ModelAndView auditClickThroughNext13(HttpSession session) {
        User user = (User) session.getAttribute("User-entity");
        UserAudit userAudit = new UserAudit();
        userAudit.setElementStatus("ON PAGE 13 OUT OF 18");
        userAudit.setModuleProgressPosition("INCOMPLETE, 5 PAGES LEFT");
        userAudit.setElementId(customAuditUserRepository.findElementID(customAuditUserRepository.findModuleID(),"CLICKTHROUGH"));
        customAuditUserRepository.save(userAudit,user);
        return redirectTo("5_8_13");
    }

    @PostMapping("/ClickThroughPrev13")
    public ModelAndView auditClickThroughPrev13(HttpSession session) {
        User user = (User) session.getAttribute("User-entity");
        UserAudit userAudit = new UserAudit();
        userAudit.setElementStatus("ON PAGE 13 OUT OF 18");
        userAudit.setModuleProgressPosition("INCOMPLETE, 5 PAGES LEFT");
        userAudit.setElementId(customAuditUserRepository.findElementID(customAuditUserRepository.findModuleID(),"CLICKTHROUGH"));
        customAuditUserRepository.save(userAudit,user);
        return redirectTo("5_8_11");
    }

    @PostMapping("/ClickThroughNext14")
    public ModelAndView auditClickThroughNext14(HttpSession session) {
        User user = (User) session.getAttribute("User-entity");
        UserAudit userAudit = new UserAudit();
        userAudit.setElementStatus("ON PAGE 14 OUT OF 18");
        userAudit.setModuleProgressPosition("INCOMPLETE, 4 PAGES LEFT");
        userAudit.setElementId(customAuditUserRepository.findElementID(customAuditUserRepository.findModuleID(),"CLICKTHROUGH"));
        customAuditUserRepository.save(userAudit,user);
        return redirectTo("5_8_14");
    }

    @PostMapping("/ClickThroughPrev14")
    public ModelAndView auditClickThroughPrev14(HttpSession session) {
        User user = (User) session.getAttribute("User-entity");
        UserAudit userAudit = new UserAudit();
        userAudit.setElementStatus("ON PAGE 14 OUT OF 18");
        userAudit.setModuleProgressPosition("INCOMPLETE, 4 PAGES LEFT");
        userAudit.setElementId(customAuditUserRepository.findElementID(customAuditUserRepository.findModuleID(),"CLICKTHROUGH"));
        customAuditUserRepository.save(userAudit,user);
        return redirectTo("5_8_12");
    }

    @PostMapping("/ClickThroughNext15")
    public ModelAndView auditClickThroughNext15(HttpSession session) {
        User user = (User) session.getAttribute("User-entity");
        UserAudit userAudit = new UserAudit();
        userAudit.setElementStatus("ON PAGE 15 OUT OF 18");
        userAudit.setModuleProgressPosition("INCOMPLETE, 3 PAGES LEFT");
        userAudit.setElementId(customAuditUserRepository.findElementID(customAuditUserRepository.findModuleID(),"CLICKTHROUGH"));
        customAuditUserRepository.save(userAudit,user);
        return redirectTo("5_8_15");
    }

    @PostMapping("/ClickThroughPrev15")
    public ModelAndView auditClickThroughPrev15(HttpSession session) {
        User user = (User) session.getAttribute("User-entity");
        UserAudit userAudit = new UserAudit();
        userAudit.setElementStatus("ON PAGE 15 OUT OF 18");
        userAudit.setModuleProgressPosition("INCOMPLETE, 3 PAGES LEFT");
        userAudit.setElementId(customAuditUserRepository.findElementID(customAuditUserRepository.findModuleID(),"CLICKTHROUGH"));
        customAuditUserRepository.save(userAudit,user);
        return redirectTo("5_8_13");
    }

    @PostMapping("/ClickThroughNext16")
    public ModelAndView auditClickThroughNext16(HttpSession session) {
        User user = (User) session.getAttribute("User-entity");
        UserAudit userAudit = new UserAudit();
        userAudit.setElementStatus("ON PAGE 16 OUT OF 18");
        userAudit.setModuleProgressPosition("INCOMPLETE, 2 PAGES LEFT");
        userAudit.setElementId(customAuditUserRepository.findElementID(customAuditUserRepository.findModuleID(),"CLICKTHROUGH"));
        customAuditUserRepository.save(userAudit,user);
        return redirectTo("5_8_16");
    }

    @PostMapping("/ClickThroughPrev16")
    public ModelAndView auditClickThroughPrev16(HttpSession session) {
        User user = (User) session.getAttribute("User-entity");
        UserAudit userAudit = new UserAudit();
        userAudit.setElementStatus("ON PAGE 16 OUT OF 18");
        userAudit.setModuleProgressPosition("INCOMPLETE, 2 PAGES LEFT");
        userAudit.setElementId(customAuditUserRepository.findElementID(customAuditUserRepository.findModuleID(),"CLICKTHROUGH"));
        customAuditUserRepository.save(userAudit,user);
        return redirectTo("5_8_14");
    }

    @PostMapping("/ClickThroughNext17")
    public ModelAndView auditClickThroughNext17(HttpSession session) {
        User user = (User) session.getAttribute("User-entity");
        UserAudit userAudit = new UserAudit();
        userAudit.setElementStatus("ON PAGE 17 OUT OF 18");
        userAudit.setModuleProgressPosition("INCOMPLETE, 1 PAGE LEFT");
        userAudit.setElementId(customAuditUserRepository.findElementID(customAuditUserRepository.findModuleID(),"CLICKTHROUGH"));
        customAuditUserRepository.save(userAudit,user);
        return redirectTo("5_8_17");
    }

    @PostMapping("/ClickThroughPrev17")
    public ModelAndView auditClickThroughPrev17(HttpSession session) {
        User user = (User) session.getAttribute("User-entity");
        UserAudit userAudit = new UserAudit();
        userAudit.setElementStatus("ON PAGE 17 OUT OF 18");
        userAudit.setModuleProgressPosition("INCOMPLETE, 1 PAGE LEFT");
        userAudit.setElementId(customAuditUserRepository.findElementID(customAuditUserRepository.findModuleID(),"CLICKTHROUGH"));
        customAuditUserRepository.save(userAudit,user);
        return redirectTo("5_8_15");
    }

    @PostMapping("/ClickThroughNext18")
    public ModelAndView auditClickThroughNext18(HttpSession session) {
        User user = (User) session.getAttribute("User-entity");
        UserAudit userAudit = new UserAudit();
        userAudit.setElementStatus("ON PAGE 18 OUT OF 18");
        userAudit.setModuleProgressPosition("COMPLETE, ALL DONE!");
        userAudit.setElementId(customAuditUserRepository.findElementID(customAuditUserRepository.findModuleID(),"CLICKTHROUGH"));
        customAuditUserRepository.save(userAudit,user);
        return redirectTo("5_8_18");
    }

    @PostMapping("/ClickThroughPrev18")
    public ModelAndView auditClickThroughPrev18(HttpSession session) {
        User user = (User) session.getAttribute("User-entity");
        UserAudit userAudit = new UserAudit();
        userAudit.setElementStatus("ON PAGE 18 OUT OF 18");
        userAudit.setModuleProgressPosition("COMPLETE, ALL DONE!");
        userAudit.setElementId(customAuditUserRepository.findElementID(customAuditUserRepository.findModuleID(),"CLICKTHROUGH"));
        customAuditUserRepository.save(userAudit,user);
        return redirectTo("5_8_16");
    }

    @PostMapping("/ClickThroughPrev19")
    public ModelAndView auditClickThroughPrev19(HttpSession session) {
        User user = (User) session.getAttribute("User-entity");
        UserAudit userAudit = new UserAudit();
        userAudit.setElementStatus("ON PAGE 18 OUT OF 18");
        userAudit.setModuleProgressPosition("COMPLETE, ALL DONE!");
        userAudit.setElementId(customAuditUserRepository.findElementID(customAuditUserRepository.findModuleID(),"CLICKTHROUGH"));
        customAuditUserRepository.save(userAudit,user);
        return redirectTo("5_8_17");
    }


    @PostMapping("/Games")
    public ModelAndView auditGames(HttpSession session, Model model) {
        User user = (User) session.getAttribute("User-entity");
        UserAudit userAudit = new UserAudit();
        userAudit.setActivityType("IN MODULE GAME");
        userAudit.setElementStatus("GAME MODULE STARTED");
        userAudit.setElementId(customAuditUserRepository.findElementID(customAuditUserRepository.findModuleID(), "IN MODULE GAME"));
        List<GameQuestions> gameQuestionsList = gamesRepository.findAll();
        gameQuestion = findRandomQuestion(gameQuestionsList);
        model.addAttribute("Question", gameQuestion);
        model.addAttribute("QuestionText", gameQuestion.getQuestionText());
        model.addAttribute("QuestionPolyMorph", gameQuestion.getQuestionPolyMorph());
        model.addAttribute("AnswerOption01", gameQuestion.getAnswerOption01());
        model.addAttribute("AnswerOption02", gameQuestion.getAnswerOption02());
        model.addAttribute("AnswerCorrect", gameQuestion.getAnswerCorrect());
        customAuditUserRepository.save(userAudit, user, gameQuestion, null);
        return redirectTo("10");
    }

    @PostMapping("/NextGame")
    public ModelAndView auditNextGame(HttpSession session, Model model) {
        User user = (User) session.getAttribute("User-entity");
        UserAudit userAudit = new UserAudit();
        userAudit.setActivityType("IN MODULE GAME");
        userAudit.setElementStatus("GAME MODULE STARTED");
        userAudit.setElementId(customAuditUserRepository.findElementID(customAuditUserRepository.findModuleID(), "IN MODULE GAME"));
        List<GameQuestions> gameQuestionsList = gamesRepository.findAll();
        GameQuestions gameQuestion = findRandomQuestion(gameQuestionsList);
        model.addAttribute("GameQuestion", gameQuestion);
        model.addAttribute("QuestionPolyMorph", gameQuestion.getQuestionPolyMorph());
        model.addAttribute("AnswerOption01", gameQuestion.getAnswerOption01());
        model.addAttribute("AnswerOption02", gameQuestion.getAnswerOption02());
        model.addAttribute("AnswerCorrect", gameQuestion.getAnswerCorrect());
        customAuditUserRepository.save(userAudit, user, gameQuestion, null);
        return redirectTo("10");
    }

    @PostMapping("/Answer")
    public ModelAndView auditAnswer(HttpSession session, @RequestParam String answer) {
        User user = (User) session.getAttribute("User-entity");
        UserAudit userAudit = new UserAudit();
        userAudit.setActivityType("IN MODULE GAME");
        userAudit.setElementStatus("GAME QUESTION ANSWERED");
        userAudit.setElementId(customAuditUserRepository.findElementID(customAuditUserRepository.findModuleID(), "IN MODULE GAME"));
        customAuditUserRepository.save(userAudit, user, gameQuestion, answer);
        return redirectTo("10");
    }

    private GameQuestions findRandomQuestion(List<GameQuestions> gameQuestionsList) {
        int randomElementIndex
                = ThreadLocalRandom.current().nextInt(gameQuestionsList.size()) % gameQuestionsList.size();
        GameQuestions gameQuestion = gameQuestionsList.get(randomElementIndex);
        return gameQuestion;
    }

    @PostMapping("/PrevOrNext")
    public ModelAndView auditPrevOrNext(HttpSession session) {
        User user = (User) session.getAttribute("User-entity");
        UserAudit userAudit = new UserAudit();
        userAudit.setActivityType("CLICKTHROUGH");
        customAuditUserRepository.save(userAudit, user);
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


