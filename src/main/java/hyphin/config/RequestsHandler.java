package hyphin.config;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Component
public class RequestsHandler implements HandlerInterceptor {

    private static final Set<String> publicUriSet;

    static {
        publicUriSet = new HashSet<>();
        publicUriSet.add("/");
        publicUriSet.add("/start");
        publicUriSet.add("/start.html");
        publicUriSet.add("/Login");
        publicUriSet.add("/LoginFailure");
        publicUriSet.add("/Register");
    }

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        String requestURI = httpServletRequest.getRequestURI();

        if (!publicUriSet.contains(requestURI) && Objects.isNull(httpServletRequest.getSession().getAttribute("User-entity"))
                && Objects.nonNull(modelAndView)) {
            modelAndView.setViewName("access-denied");
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
