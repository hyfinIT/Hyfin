package hyphin.controller;

import hyphin.dto.ClickItemDto;
import hyphin.enums.AuditEventType;
import hyphin.model.click.ClickItem;
import hyphin.service.ClickItemService;
import hyphin.util.HyfinUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/click")
@RequiredArgsConstructor
public class ClickThroughControllerV2 {

    private final ClickItemService clickItemService;

    @GetMapping
    public ModelAndView click(@RequestParam(required = false) String type,
                              ModelAndView modelAndView,
                              HttpSession session) {

        ClickItemDto clickItemDto = new ClickItemDto();
        ClickItem clickItem = null;

        if ("start".equalsIgnoreCase(type)) {
            clickItemService.startClickSession(session);
            clickItem = clickItemService.currentStep(session);
        } else if (clickItemService.isSessionExpired(session)) {
            clickItemDto.setIsSessionExpired(true);
            modelAndView.setViewName("click");
            modelAndView.getModel().put("clickItem", clickItemDto);
            return modelAndView;
        }

        if ("prev".equalsIgnoreCase(type)) {
            clickItem = clickItemService.prevStep(session);
        } else if ("next".equalsIgnoreCase(type)) {
            clickItem = clickItemService.nextStep(session);
        }

        clickItemDto.setImageData(clickItem.getImageData());
        clickItemDto.setStyle(clickItem.getStyle());
        clickItemDto.setText(clickItem.getText());
        clickItemDto.setIsFirst(clickItem.getNumber().equals(1));
        clickItemDto.setIsLast(clickItem.getNumber().equals(clickItemService.getClickItemListSize()));

        modelAndView.setViewName("click");
        modelAndView.getModel().put("clickItem", clickItemDto);

        return modelAndView;
    }

    @PostMapping
    public void event(@RequestParam AuditEventType eventType, HttpSession session) {
        clickItemService.handleEvent(eventType, session);
    }

    @PostMapping("/done")
    public ModelAndView done(HttpSession session) {
        clickItemService.handleEvent(AuditEventType.COMPLETE, session);
        return HyfinUtils.modelAndView("4");
    }

    @GetMapping("/session/expired")
    public String isSessionExpired(HttpSession session) {
        return clickItemService.isSessionExpired(session).toString();
    }

}
