package hyphin.controller;

import hyphin.enums.VideoEventType;
import hyphin.service.VideoService;
import hyphin.util.HyfinUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

import static hyphin.util.HyfinUtils.modelAndView;

@RestController
@RequestMapping("video")
@RequiredArgsConstructor
public class VideoController {

    private final VideoService videoService;

    @GetMapping
    public ModelAndView video(HttpSession session) throws InterruptedException {
        videoService.startVideoSession(session);
        videoService.handleEvent(VideoEventType.START_SESSION, session, null);
        return modelAndView("9");
    }

    @PostMapping
    public void videoEvent(@RequestParam VideoEventType eventType, @RequestParam(required = false) String position, HttpSession session) {
        videoService.handleEvent(eventType, session, position);
    }

    @GetMapping("/session/expired")
    public String isSessionExpired(HttpSession session) {
        return videoService.isSessionExpired(session).toString();
    }

    @GetMapping("/complete")
    public ModelAndView complete(HttpSession session) {
        videoService.handleEvent(VideoEventType.COMPLETE, session, null);
        return HyfinUtils.modelAndView("4");
    }
}
