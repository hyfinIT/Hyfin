package hyphin.config;

import hyphin.model.User;
import hyphin.repository.UserRepository;
import hyphin.util.HyfinUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class RequestsHandler implements HandlerInterceptor {

    private final UserRepository userRepository;

    @Value("${disable.security:false}")
    private Boolean disable;


    private static final Set<String> publicUriSet;

    static {
        publicUriSet = new HashSet<>();
        publicUriSet.add("/");
        publicUriSet.add("/start");
        publicUriSet.add("/start.html");
        publicUriSet.add("/Login");
        publicUriSet.add("/LoginFailure");
        publicUriSet.add("/Register");
        publicUriSet.add("/forgotten-password");
        publicUriSet.add("/forgotten-password.html");
        publicUriSet.add("/14");
        publicUriSet.add("/14.html");
        publicUriSet.add("/send-reset-link");
        publicUriSet.add("/email-does-not-exist");
        publicUriSet.add("/restore-password");
        publicUriSet.add("/apply-new-password");
    }

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        if (disable) {
            if (Objects.isNull(httpServletRequest.getSession().getAttribute("User-entity"))) {
                User user = userRepository.findOne(51);
                httpServletRequest.getSession().setAttribute("User-entity", user);
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        String requestURI = httpServletRequest.getRequestURI();
        User user = (User) httpServletRequest.getSession().getAttribute("User-entity");

        if (!publicUriSet.contains(requestURI) && Objects.isNull(user)
                && Objects.nonNull(modelAndView)) {
            modelAndView.setViewName("access-denied");
        }

        if (!publicUriSet.contains(requestURI) && Objects.nonNull(user) && user.getActive().equals(false)) {
            if (HyfinUtils.canRestoreAccount(user)) {
                modelAndView.setViewName("restore");
            } else {
                modelAndView.setViewName("access-denied");
            }
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
