package hyphin.controller;

import hyphin.dto.NewPasswordRequest;
import hyphin.model.Answer;
import hyphin.model.Login;
import hyphin.model.User;
import hyphin.repository.CustomUserAuditRepository;
import hyphin.repository.UserRepository;
import hyphin.service.UserService;
import hyphin.util.HyfinUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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
    UserService userService;

    @Autowired
    CustomUserAuditRepository customAuditUserRepository;

    @Autowired
    UserRepository userRepository;

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
    public ModelAndView loginUsers(@ModelAttribute("login") Login login, BindingResult bindingResult, HttpSession session) {
        if (bindingResult.hasErrors()) {
            System.out.println("There was a error " + bindingResult);
        }

        User user = userService.findByUserNameAndPassword(login.getEmail(), login.getPassword());
        if (user != null) {
            session.setAttribute("User-entity", user);
            if (user.getActive()) {
                return redirectTo("1");
            } else {
                if (HyfinUtils.canRestoreAccount(user)) {
                    return HyfinUtils.modelAndView("restore");
                } else {
                    return HyfinUtils.modelAndView("access-denied");
                }
            }
        } else {
            return redirectTo("LoginFailure");
        }
    }


    @PostMapping("/Register")
    public ModelAndView registerUsers(HttpSession session, @ModelAttribute("register") User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            LOGGER.log(Level.ERROR, "There was a form binding error " + bindingResult);
        }
        userService.save(user);
        session.setAttribute("User-entity", user);
        return HyfinUtils.modelAndView("RegistrationSuccess");
    }

    @PostMapping("/send-reset-link")
    public ModelAndView sendResetLink(@RequestParam String email) {
        if (userService.sendRestorePasswordLinkEmail(email)) {
            return HyfinUtils.modelAndView("restore-password-email-was-sent");
        } else {
            return HyfinUtils.modelAndView("email-does-not-exist");
        }
    }

    @GetMapping("/restore-password")
    public ModelAndView restorePassword(HttpSession session, @RequestParam String hash) {
        if (userService.restorePassword(session, hash)) {
            return HyfinUtils.modelAndView("14");
        } else {
            return HyfinUtils.modelAndView("something-went-wrong");
        }
    }

    @PostMapping("/apply-new-password")
    public ModelAndView applyNewPassword(HttpSession session, NewPasswordRequest request) {
        if (userService.applyNewPassword(session, request)) {
            return HyfinUtils.modelAndView("restore-success");
        } else {
            //case when session is over or passwords don't match
            return HyfinUtils.modelAndView("something-went-wrong");
        }
    }

    @GetMapping("/access-denied")
    public ModelAndView accessDenied() {
        return redirectTo("access-denied");
    }

    public ModelAndView redirectTo(String pageTo) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName(pageTo);
        return mav;
    }
}
