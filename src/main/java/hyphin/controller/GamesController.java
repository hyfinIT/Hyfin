package hyphin.controller;

import hyphin.enums.AuditEventType;
import hyphin.model.*;
import hyphin.repository.CustomUserAuditRepository;
import hyphin.repository.CustomUserRepository;
import hyphin.repository.GamesRepository;
import hyphin.repository.UserAuditRepository;
import hyphin.repository.UserRepository;
import hyphin.service.GameService;
import hyphin.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequiredArgsConstructor
@Slf4j
public class GamesController {

    CustomUserRepository customUserRepository;

    @Autowired
    private GameService gameService;

    @Autowired
    CustomUserAuditRepository customAuditUserRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    GamesRepository gamesRepository;

    @Autowired
    private final UserAuditRepository userAuditRepository;

    private static final SimpleDateFormat jdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    private GameQuestions gameQuestion;

    private volatile static int counter;

    @Autowired
    UserService userService;

    private static final String GAMES_ELEMENT_ID = "3";

    private List<GameQuestions> gameQuestionsList;

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
        gameQuestionsList = gamesRepository.findAll();
        gameQuestion = findRandomQuestion(gameQuestionsList);
        gameService.dropCounter(session);
        User user = (User) session.getAttribute("User-entity");
        gameService.getOrCreateAnswersCounter(session, user);

        gameService.handleEvent(AuditEventType.START_SESSION, session);

        UserAudit userAudit = new UserAudit();
        userAudit.setActivityType("IN MODULE GAME");
        userAudit.setModuleProgressPosition("INCOMPLETE," + "GAME QUESTION NO " + gameService.getQuestionNumber(session) + "" + " ASKED");
        userAudit.setElementStatus("GAME QUESTION NO " + gameService.getQuestionNumber(session) + "" + " ASKED");
        userAudit.setElementId(GAMES_ELEMENT_ID);
        if (user != null)
            userAudit.setUid(user.getUid());
        userAudit.setLearningJourney(userAuditRepository.findLearningJourneyName());
        userAudit.setLearningJourneyId(userAuditRepository.findLearningJourneyId());
        userAudit.setModuleId(userAuditRepository.findModuleID());
        userAudit.setModule(userAuditRepository.findModuleName(userAudit.getModuleId()));
        userAudit.setElementPosition(userAudit.getElementId());
        userAudit.setGlossaryTerm(userAuditRepository.findGlossaryTerm(userAudit.getModuleId(), userAudit.getLearningJourney()));
        userAudit.setMediaType(userAuditRepository.findElementType(userAudit.getElementId()));
        userAudit.setActivityType(userAuditRepository.findElementType(userAudit.getElementId()));
        userAudit.setDateTime(jdf.format(new Date()));
        userAudit.setQuidNumber(gameQuestion.getQuidNumber());
        userAudit.setQuidNumberOutcome(null);
        userAudit.setDifficulty(null);
        userAudit.setCompletionTime(null);
        userAudit.setQuidAnswer(null);

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

        gameService.addAuditToList(session, userAudit);

        return redirectTo("10");
    }

    @PostMapping("/NextGame")
    public ModelAndView auditNextGame(HttpSession session, Model model, String answer, String questionId) {
        gameService.touch(session);
        String answerTime = jdf.format(new Date());
        User user = (User) session.getAttribute("User-entity");

        if (Objects.nonNull(questionId)) {
            gameService.increaseAnswersCounter(session);

            Thread thread = new Thread(() -> {
                Optional<GameQuestions> question = gamesRepository.findByQuidNumber(questionId);
                question.ifPresent(q -> {
                    UserAudit userAudit = new UserAudit();
                    userAudit.setActivityType("IN MODULE GAME");
                    if (!"default".equals(answer)) {
                        userAudit.setModuleProgressPosition("INCOMPLETE," + "GAME QUESTION NO " + (gameService.getQuestionNumber(session) - 1) + "" + " ANSWERED");
                        userAudit.setElementStatus("GAME QUESTION NO " + (gameService.getQuestionNumber(session) - 1) + "" + " ANSWERED");
                    } else {
                        userAudit.setModuleProgressPosition("INCOMPLETE," + "GAME QUESTION NO " + (gameService.getQuestionNumber(session) - 1) + "" + " SKIPPED");
                        userAudit.setElementStatus("GAME QUESTION NO " + (gameService.getQuestionNumber(session) - 1) + "" + " SKIPPED");
                    }
                    userAudit.setElementId(GAMES_ELEMENT_ID);

                    if (user != null) {
                        userAudit.setUid(user.getUid());
                    }
                    userAudit.setLearningJourney(userAuditRepository.findLearningJourneyName());
                    userAudit.setLearningJourneyId(userAuditRepository.findLearningJourneyId());
                    userAudit.setModuleId(userAuditRepository.findModuleID());
                    userAudit.setModule(userAuditRepository.findModuleName(userAudit.getModuleId()));
                    userAudit.setElementPosition(userAudit.getElementId());
                    userAudit.setGlossaryTerm(userAuditRepository.findGlossaryTerm(userAudit.getModuleId(), userAudit.getLearningJourney()));
                    userAudit.setMediaType(userAuditRepository.findElementType(userAudit.getElementId()));
                    userAudit.setActivityType(userAuditRepository.findElementType(userAudit.getElementId()));
                    userAudit.setDateTime(answerTime);
                    userAudit.setQuidNumber(q.getQuidNumber());
                    if (answer != null && answer.equalsIgnoreCase(q.getAnswerCorrect()))
                        userAudit.setQuidNumberOutcome("CORRECT");
                    else if ("default".equals(answer)) {
                        userAudit.setQuidNumberOutcome("NO ANSWER");
                    }
                    else if (answer != null && !answer.equalsIgnoreCase(q.getAnswerCorrect()))
                        userAudit.setQuidNumberOutcome("INCORRECT");
                    else
                        userAudit.setQuidNumberOutcome(null);
                    userAudit.setDifficulty(null);
                    userAudit.setCompletionTime(null);
                    userAudit.setQuidAnswer(answer);

                    gameService.addAuditToList(session, userAudit);

                    if (q.getAnswerCorrect().equals(answer)) {
                        gameService.increaseCorrectAnswersCounter(session);
                    }
                });
            });

            thread.start();
        }

        int round = gameService.getRound(session);
        int answeredQuestionsCounter = gameService.getOrCreateAnswersCounter(session, user);
        if (answeredQuestionsCounter == 10 && round < 3) {
            int correctAnswersCounter = gameService.getCorrectAnswersCounter(session);
            gameService.increaseRound(session);
            return totalResult(correctAnswersCounter);
        } else if (answeredQuestionsCounter == 10 && round == 3) {
            gameService.handleEvent(AuditEventType.COMPLETE, session);
            return finalTotalResult(gameService.getCorrectAnswersCounter(session));
        } else {
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

            Thread thread = new Thread(() -> {
                UserAudit userAudit = new UserAudit();
                userAudit.setActivityType("IN MODULE GAME");
                userAudit.setModuleProgressPosition("INCOMPLETE," + "GAME QUESTION NO " + gameService.getQuestionNumber(session) + "" + " ASKED");
                userAudit.setElementStatus("GAME QUESTION NO " + gameService.getQuestionNumber(session) + "" + " ASKED");
                userAudit.setElementId(GAMES_ELEMENT_ID);

                if (user != null) {
                    userAudit.setUid(user.getUid());
                }
                userAudit.setLearningJourney(userAuditRepository.findLearningJourneyName());
                userAudit.setLearningJourneyId(userAuditRepository.findLearningJourneyId());
                userAudit.setModuleId(userAuditRepository.findModuleID());
                userAudit.setModule(userAuditRepository.findModuleName(userAudit.getModuleId()));
                userAudit.setElementPosition(userAudit.getElementId());
                userAudit.setGlossaryTerm(userAuditRepository.findGlossaryTerm(userAudit.getModuleId(), userAudit.getLearningJourney()));
                userAudit.setMediaType(userAuditRepository.findElementType(userAudit.getElementId()));
                userAudit.setActivityType(userAuditRepository.findElementType(userAudit.getElementId()));
                userAudit.setDateTime(jdf.format(new Date()));
                userAudit.setQuidNumber(gameQuestion.getQuidNumber());
                if (answer != null && answer.equalsIgnoreCase(gameQuestion.getAnswerCorrect()))
                    userAudit.setQuidNumberOutcome("CORRECT");
                else if ("default".equals(answer)) {
                    userAudit.setQuidNumberOutcome("NO ANSWER");
                } else if (answer != null && !answer.equalsIgnoreCase(gameQuestion.getAnswerCorrect()))
                    userAudit.setQuidNumberOutcome("INCORRECT");
                else
                    userAudit.setQuidNumberOutcome(null);
                userAudit.setDifficulty(null);
                userAudit.setCompletionTime(null);
                userAudit.setQuidAnswer(answer);

                gameService.addAuditToList(session, userAudit);
            });

            thread.start();


            return redirectTo("10");
        }
    }

    @GetMapping("/ready")
    public ModelAndView ready() {
        return redirectTo("ready");
    }

    @GetMapping("/rules")
    public ModelAndView rules() {
        return redirectTo("rules");
    }

    @PostMapping("game/event")
    public void videoEvent(@RequestParam AuditEventType eventType, HttpSession session) {
        log.info("<========================================================================================");
        log.info("eventType: {}", eventType);
        log.info("<========================================================================================");
        gameService.handleEvent(eventType, session);
    }

    @GetMapping("/game/session/expired")
    public String isSessionExpired(HttpSession session) {
        return gameService.isSessionExpired(session).toString();
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




