package com.shengming.common.interceptor;

import com.alibaba.fastjson2.JSON;
import com.shengming.common.utils.JwtUtil;
import com.shengming.common.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/*拦截器
* 拦截的逻辑*/
@Component
@Slf4j
public class JwtValidateInterceptor implements HandlerInterceptor {
    @Autowired
    JwtUtil jwtUtil;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("X-Token");
        log.debug(request.getRequestURI() + "需要验证" + token);
        if(token != null){
            try {
                jwtUtil.parseToken(token);
                log.debug(request.getRequestURI() + "验证已通过");
                /*放行*/
                return true;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        log.debug(request.getRequestURI() + "验证未通过");
        response.setContentType("application/json;charset=utf-8");
        Result<Object> fail = Result.fail(20003,"jwt无效");
        response.getWriter().write(JSON.toJSONString(fail));
        //拦截
        return false;
    }
}
