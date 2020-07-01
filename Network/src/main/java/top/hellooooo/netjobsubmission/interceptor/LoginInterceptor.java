package top.hellooooo.netjobsubmission.interceptor;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginInterceptor extends HandlerInterceptorAdapter {

    /**
     * 请求预处理
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        boolean flag = true;
        Object sessionUser = session.getAttribute("user");
        if (sessionUser == null) {
            flag = false;
            response.sendRedirect("/job/user/index");
        }
        return flag;
    }
}
