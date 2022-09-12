package hyphin.controller;

import hyphin.model.*;
import hyphin.repository.CustomUserAuditRepository;
import hyphin.repository.CustomUserRepository;
import hyphin.repository.GamesRepository;
import hyphin.repository.UserRepository;
import hyphin.service.GameService;
import hyphin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@RestController
public class GamesController {

    @Autowired
    CustomUserRepository customUserRepository;

    @Autowired
    private GameService gameService;

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
        gameService.dropCounter(session.getId());
        gameService.getOrCreateAnswersCounter(session.getId());
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
        model.addAttribute("questionId", gameQuestion.getQuidNumber());

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
    public ModelAndView auditNextGame(HttpSession session, Model model, String answer, String questionId) {
        if (Objects.nonNull(answer)) {
            gameService.increaseAnswersCounter(session.getId());
            Optional<GameQuestions> question = gamesRepository.findByQuidNumber(questionId);
            question.ifPresent(q -> {
                if (q.getAnswerCorrect().equals(answer)) {
                    gameService.increaseCorrectAnswersCounter(session.getId());
                }
            });
        }
        int round = gameService.getRound(session.getId());
        int answeredQuestionsCounter = gameService.getOrCreateAnswersCounter(session.getId());
        if (answeredQuestionsCounter == 10 && round < 3) {
            int correctAnswersCounter = gameService.getCorrectAnswersCounter(session.getId());
            gameService.increaseRound(session.getId());
            return totalResult(correctAnswersCounter);
        } else if (answeredQuestionsCounter == 10 && round == 3) {
            return finalTotalResult(gameService.getCorrectAnswersCounter(session.getId()));
        } else {
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
            model.addAttribute("questionId", gameQuestion.getQuidNumber());

            User user = (User) session.getAttribute("User-entity");
            UserAudit userAudit = new UserAudit();
            userAudit.setActivityType("IN MODULE GAME");
            userAudit.setModuleProgressPosition("INCOMPLETE," + "GAME QUESTION NO " + ++counter + "" + " ASKED");
            userAudit.setElementStatus("GAME QUESTION NO " + counter + "" + " ASKED");
            userAudit.setElementId(customAuditUserRepository.findElementID(customAuditUserRepository.findModuleID(), "IN MODULE GAME"));

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    customAuditUserRepository.save(userAudit, user, gameQuestion, null);
                }
            });
            thread.start();

            return redirectTo("10");
        }


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

    @GetMapping("/ready")
    public ModelAndView ready() {
        return redirectTo("ready");
    }

    @GetMapping("/rules")
    public ModelAndView rules() {
        return redirectTo("rules");
    }

    public ModelAndView totalResult(int correctAnswersCounter) {
        ModelAndView mav = redirectTo("total_result");
        mav.getModel().put("result", correctAnswersCounter);
        return mav;
    }

    public ModelAndView finalTotalResult(int correctAnswersCounter) {
        ModelAndView mav = redirectTo("final_total_result");
        mav.getModel().put("result", correctAnswersCounter);
        return mav;
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




