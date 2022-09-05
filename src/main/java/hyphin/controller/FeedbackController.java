package hyphin.controller;

import hyphin.model.feedback.FeedbackDto;
import hyphin.service.CaptchaService;
import hyphin.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackService feedbackService;
    private final CaptchaService captchaService;

    @PostMapping(value = "/feedback")
    public ModelAndView feedback(FeedbackDto feedbackDto,
                                 @RequestParam(name = "g-recaptcha-response") String gRecaptchaResponse,
                                 HttpServletRequest request) throws MessagingException {
        String ip = request.getRemoteAddr();
        String captchaVerifyMessage =
                captchaService.verifyRecaptcha(ip, gRecaptchaResponse);

        if (!"SUCCESS".equals(captchaVerifyMessage)) {
            ModelAndView modelAndView = redirectTo("feedback-sent");
            modelAndView.getModel().put("result", "You need to pass a test for a robot. Try again");
            return modelAndView;
        }

        feedbackService.handleFeedback(feedbackDto);
        ModelAndView modelAndView = redirectTo("feedback-sent");
        modelAndView.getModel().put("result", "Thank you for your feedback");
        return modelAndView;
    }

    public ModelAndView redirectTo(String pageTo) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName(pageTo);
        return mav;
    }
}
