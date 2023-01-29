package hyphin.controller;

import hyphin.enums.AuditEventType;
import hyphin.model.video.UserVideoSession;
import hyphin.service.VideoService;
import hyphin.util.HyfinUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

import static hyphin.util.HyfinUtils.modelAndView;

@RestController
@RequestMapping("video")
@RequiredArgsConstructor
public class VideoController {

    private final VideoService videoService;

    @GetMapping
    public ModelAndView video(HttpSession session, @RequestParam String elementId) throws InterruptedException {
        UserVideoSession userVideoSession = videoService.startVideoSession(session, elementId);
        videoService.handleEvent(AuditEventType.START_SESSION, session, null);

        ModelAndView modelAndView = modelAndView("9");
        modelAndView.getModel().put("vimeoId", userVideoSession.getVideoLink());
        modelAndView.getModel().put("closeLink", videoService.getActiveVideoSession(session).getDoneButtonLink());

        return modelAndView;
    }

    @PostMapping
    public void videoEvent(@RequestParam AuditEventType eventType, @RequestParam(required = false) String position, HttpSession session) {
        videoService.handleEvent(eventType, session, position);
    }

    @GetMapping("/session/expired")
    public String isSessionExpired(HttpSession session) {
        return videoService.isSessionExpired(session).toString();
    }

    @GetMapping("/complete")
    public ModelAndView complete(HttpSession session) {
        String doneButtonLink = videoService.getActiveVideoSession(session).getDoneButtonLink();
        videoService.handleEvent(AuditEventType.COMPLETE, session, null);
        return HyfinUtils.modelAndView(doneButtonLink);
    }
}
