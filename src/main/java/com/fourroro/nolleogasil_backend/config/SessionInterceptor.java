package com.fourroro.nolleogasil_backend.config;

import com.fourroro.nolleogasil_backend.dto.users.UsersDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class SessionInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        System.out.println("!!!!!!!!!!!!!세션 인터셉터!!!!!!!!!!!!!!!");
        //세션 만료 여부 확인
        if(!isSessionExpired(request)){
            response.setStatus(HttpStatus.OK.value());
            System.out.println("Session is valid.");
            return true;
        }else {
            //세션이 만료되었다면, 401 상태 코드 전달
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            System.out.println("Session is invalid or expired.");
            return false;
        }
    }

    public boolean isSessionExpired(HttpServletRequest request){
        System.out.println("!!!!!!isSessionExpired!!!!!!!");
        HttpSession session = request.getSession(false);

        if(session == null){
            //세션이 존재하지 않거나 만료된 경우
            return true;
        }else{
            //세션이 유효한 경우
            UsersDto usersDto = (UsersDto) session.getAttribute("users");
            System.out.println(usersDto.getName());
            if (usersDto == null) {
                //세션에 사용자 정보가 없는 경우
                return true;
            } else {
                //세션에 사용자 정보가 있는 경우
                return false;
            }
        }
    }
}
