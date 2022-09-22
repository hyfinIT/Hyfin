package hyphin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("end-challenge")
@RequiredArgsConstructor
public class EndChallengeController {

    @GetMapping("/ready")
    public ModelAndView ready(){
        return redirectTo("end_challenge/ready");
    }

    @GetMapping("/start")
    public ModelAndView start() {
        return redirectTo("end_challenge/start");
    }

    public ModelAndView redirectTo(String pageTo) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName(pageTo);
        return mav;
    }
}
