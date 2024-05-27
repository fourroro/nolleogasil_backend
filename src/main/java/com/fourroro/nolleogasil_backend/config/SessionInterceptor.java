package com.fourroro.nolleogasil_backend.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class SessionInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //세션 만료 여부 확인
        if(!isSessionExpired(request)){
            response.setStatus(HttpStatus.OK.value());
            return true;
        }else {
            //세션이 만료되었다면, 401 상태 코드 전달
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }
    }

    public boolean isSessionExpired(HttpServletRequest request){
        HttpSession session = request.getSession(false);

        if(session == null){
            //세션이 존재하지 않거나 만료된 경우
            return true;
        }else{
            //세션이 유효한 경우
            return false;
        }
    }
}
