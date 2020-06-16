package top.hellooooo.jobsubmission.interceptor;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        boolean flag = true;
        Object sessionUser = session.getAttribute("user");
        if (sessionUser == null) {
            flag = false;
        }
        response.sendRedirect("/job/index");
        return flag;
    }
}
