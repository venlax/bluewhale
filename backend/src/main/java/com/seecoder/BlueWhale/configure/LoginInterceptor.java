package com.seecoder.BlueWhale.configure;

import com.seecoder.BlueWhale.exception.BlueWhaleException;
import com.seecoder.BlueWhale.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 * @Author: DingXiaoyu
 * @Date: 0:17 2023/11/26
 * 这个类定制了一个登录的拦截器，
 * SpringBoot的拦截器标准为HandlerInterceptor接口，
 * 这个类实现了这个接口，表示是SpringBoot标准下的，
 * 在preHandle方法中，通过获取请求头Header中的token，
 * 判断了token是否合法，如果不合法则抛异常，
 * 合法则将用户信息存储到request的session中。
*/
@Component
public class LoginInterceptor implements HandlerInterceptor {

     //需要放行的接口路径列表
    private final List<String> allowedPaths = Arrays.asList("/api/stores");


    @Autowired
    TokenUtil tokenUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String path = request.getRequestURI();

        // 检查请求路径是否在允许放行的列表中
        if (allowedPaths.contains(path)) {
            return true; // 放行
        }

        String token = request.getHeader("token");
        if (token != null && tokenUtil.verifyToken(token)) {
            request.getSession().setAttribute("currentUser",tokenUtil.getUser(token));
            return true;
        }else {
            throw BlueWhaleException.notLogin();
        }
    }

}
