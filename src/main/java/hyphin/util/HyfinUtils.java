package hyphin.util;

import hyphin.model.User;
import org.springframework.web.servlet.ModelAndView;

public class HyfinUtils {

    private static final long RESTORE_PERIOD_MILLIS = 7776000000L;

    public static ModelAndView modelAndView(String viewName) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName(viewName);
        return mav;
    }

    public static boolean canRestoreAccount(User user) {
        return System.currentTimeMillis() - user.getDeletionDate() < RESTORE_PERIOD_MILLIS;
    }
}
