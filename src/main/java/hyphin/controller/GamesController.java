package hyphin.controller;

import hyphin.model.*;
import hyphin.repository.CustomUserAuditRepository;
import hyphin.repository.CustomUserRepository;
import hyphin.repository.GamesRepository;
import hyphin.repository.UserRepository;
import hyphin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@RestController
public class GamesController {

    @Autowired
    CustomUserRepository customUserRepository;

    @Autowired
    CustomUserAuditRepository customAuditUserRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    GamesRepository gamesRepository;

    private GameQuestions gameQuestion;

    private volatile static int counter;

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


    @PostMapping("/Games")
    public ModelAndView auditGames(HttpSession session, Model model) {
        User user = (User) session.getAttribute("User-entity");
        UserAudit userAudit = new UserAudit();
        userAudit.setActivityType("IN MODULE GAME");
        userAudit.setModuleProgressPosition("INCOMPLETE," + "GAME QUESTION NO " + ++counter + "" + " ASKED");
        userAudit.setElementStatus("GAME QUESTION NO " + counter + "" + " ASKED");
        userAudit.setElementId(customAuditUserRepository.findElementID(customAuditUserRepository.findModuleID(), "IN MODULE GAME"));
        List<GameQuestions> gameQuestionsList = gamesRepository.findAll();
        gameQuestion = findRandomQuestion(gameQuestionsList);

        String[] answers = {gameQuestion.getAnswerOption01(), gameQuestion.getAnswerOption02(),
                gameQuestion.getAnswerCorrect()};
        List<String> list = Arrays.asList(answers);
        Collections.shuffle(list);
        model.addAttribute("answers", list);

        model.addAttribute("Question", gameQuestion);
        model.addAttribute("QuestionText", gameQuestion.getQuestionText());
        model.addAttribute("QuestionPolyMorph", gameQuestion.getQuestionPolyMorph());
        model.addAttribute("AnswerOption01", gameQuestion.getAnswerOption01());
        model.addAttribute("AnswerOption02", gameQuestion.getAnswerOption02());
        model.addAttribute("AnswerCorrect", gameQuestion.getAnswerCorrect());

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                customAuditUserRepository.save(userAudit, user, gameQuestion, null);
            }
        });

        thread.start();

        return redirectTo("10");
    }

    @PostMapping("/NextGame")
    public ModelAndView auditNextGame(HttpSession session, Model model) {
        User user = (User) session.getAttribute("User-entity");
        UserAudit userAudit = new UserAudit();
        userAudit.setActivityType("IN MODULE GAME");
        userAudit.setModuleProgressPosition("INCOMPLETE," + "GAME QUESTION NO " + ++counter + "" + " ASKED");
        userAudit.setElementStatus("GAME QUESTION NO " + counter + "" + " ASKED");
        userAudit.setElementId(customAuditUserRepository.findElementID(customAuditUserRepository.findModuleID(), "IN MODULE GAME"));
        List<GameQuestions> gameQuestionsList = gamesRepository.findAll();
        GameQuestions gameQuestion = findRandomQuestion(gameQuestionsList);
        String[] answers = {gameQuestion.getAnswerOption01(), gameQuestion.getAnswerOption02(),
                gameQuestion.getAnswerCorrect()};
        List<String> list = Arrays.asList(answers);
        Collections.shuffle(list);
        model.addAttribute("answers", list);

        model.addAttribute("Question", gameQuestion);
        model.addAttribute("QuestionText", gameQuestion.getQuestionText());
        model.addAttribute("QuestionPolyMorph", gameQuestion.getQuestionPolyMorph());
        model.addAttribute("AnswerOption01", gameQuestion.getAnswerOption01());
        model.addAttribute("AnswerOption02", gameQuestion.getAnswerOption02());
        model.addAttribute("AnswerCorrect", gameQuestion.getAnswerCorrect());

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                customAuditUserRepository.save(userAudit, user, gameQuestion, null);
            }
        });
        thread.start();

        return redirectTo("10");
    }

    @PostMapping("/Answer")
    public ModelAndView auditAnswer(HttpSession session, @RequestParam String answer) {
        User user = (User) session.getAttribute("User-entity");
        UserAudit userAudit = new UserAudit();
        userAudit.setActivityType("IN MODULE GAME");
        userAudit.setModuleProgressPosition("INCOMPLETE," + "GAME QUESTION NO " + counter + "" + " ANSWERED");
        userAudit.setElementStatus("GAME QUESTION NO " + counter + "" + " ANSWERED");
        userAudit.setElementId(customAuditUserRepository.findElementID(customAuditUserRepository.findModuleID(), "IN MODULE GAME"));
        customAuditUserRepository.save(userAudit, user, gameQuestion, answer);
        return redirectTo("10");
    }


    public ModelAndView redirectTo(String pageTo) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName(pageTo);
        return mav;
    }

    private synchronized GameQuestions findRandomQuestion(List<GameQuestions> gameQuestionsList) {
        int randomElementIndex
                = ThreadLocalRandom.current().nextInt(gameQuestionsList.size()) % gameQuestionsList.size();
        GameQuestions gameQuestion = gameQuestionsList.get(randomElementIndex);
        return gameQuestion;
    }
}




