package hyphin.controller;

import hyphin.model.Login;
import hyphin.model.User;
import hyphin.repository.UserRepo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;


/**
 * Created by Abhishek Satsangi on 27/06/2022
 */
@RestController
public class HyphinController {

    private static final Logger LOGGER = LogManager.getLogger(HyphinController.class);

    private RestTemplate restTemplate;

    @Autowired
    UserRepo userRepo;

    @Value("spring.datasource.url")
    private String jdbcUrl;

    @Value("spring.datasource.username")
    private String username;

    @Value("spring.datasource.password")
    private String password;

    @ModelAttribute(value = "register")
    public User newUser()
    {
        return new User();
    }

    @GetMapping("/")
    public ModelAndView viewHome() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("start");
        return mav;
    }

    @GetMapping("/faqs")
    public ModelAndView viewFaqs() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("faqs");
        return mav;
    }

    @GetMapping("/1")
    public ModelAndView viewCourses() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("1");
        return mav;
    }

    @GetMapping("/glossary")
    public ModelAndView viewGlossary() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("glossary");
        return mav;
    }

    @PostMapping("/Login")
    public void loginUsers(@ModelAttribute("login") Login login, BindingResult bindingResult) {

        if(bindingResult.hasErrors()){
            System.out.println("There was a error "+bindingResult);
        }

    }

    @PostMapping("/Register")
    public void registerUsers(@ModelAttribute("register") User user, BindingResult bindingResult)
    {
        if(bindingResult.hasErrors()){
            System.out.println("There was a error "+bindingResult);
        }
       userRepo.save(user);
    }
}
