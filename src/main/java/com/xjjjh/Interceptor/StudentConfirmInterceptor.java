package com.xjjjh.Interceptor;


import com.xjjjh.pojo.Student;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Slf4j
public class StudentConfirmInterceptor implements HandlerInterceptor {
    /**
     * Controller方法执行前调用
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        Student studentInfo = (Student)session.getAttribute("studentInfo");
        //如果未进行学生认证
        if (studentInfo==null){
            log.info("请先进行学生认证");
            //获取项目根路径
            String path = session.getServletContext().getContextPath();
            //重定向回学生认证页面
            response.sendRedirect("/register");
            return false;
        }else {
            return true;
        }
    }
    //Controller执行后调用
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
