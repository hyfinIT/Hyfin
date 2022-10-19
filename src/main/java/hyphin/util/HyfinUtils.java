package hyphin.util;

import org.springframework.web.servlet.ModelAndView;

public class HyfinUtils {

    public static ModelAndView modelAndView(String viewName) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName(viewName);
        return mav;
    }
}
