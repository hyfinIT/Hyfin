package hyphin.controller;

import hyphin.model.User;
import hyphin.repository.UserRepo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

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

    public RestTemplate getRestTemplate() {
        restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new FormHttpMessageConverter());
        FormHttpMessageConverter converter = new FormHttpMessageConverter();
        restTemplate.getMessageConverters().add(converter);
        return restTemplate;
    }

    @PostMapping("/addUsers")
    public void addUsers() {
        User user = new User(1, "MVP");
        //userRepo.save(user);
    }

     private HttpHeaders setHeaders() throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        //if(getOauth() != null) {
          //  headers.set("authorization", "Bearer " + getOauth().substring(1, getOauth().length() - 1));
        //}
        return headers;
    }




}
