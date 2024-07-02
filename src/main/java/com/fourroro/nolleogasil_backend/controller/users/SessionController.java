package com.fourroro.nolleogasil_backend.controller.users;

import com.fourroro.nolleogasil_backend.config.SessionInterceptor;
import com.fourroro.nolleogasil_backend.dto.users.UsersDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
public class SessionController {
    private final SessionInterceptor sessionInterceptor;

    @GetMapping("/session/check")
    public ResponseEntity<String> checkerSession(HttpServletRequest request){
        if (sessionInterceptor.isSessionExpired(request)) {
            //세션이 존재하지 않거나 만료된 경우
            System.out.println("!!!!!!!!!!!!세션 없음! 로그인 필수!!!!!!!!!!!!");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Session expired");
        } else {
            // 세션이 유효한 경우
            HttpSession session = request.getSession();
            UsersDto users = (UsersDto) session.getAttribute("users");
            System.out.println("!!!!!!!!!!!세션 컨트롤러!!!!!!!!!!!!");
            System.out.println(users.getName());
            return ResponseEntity.ok("Session valid");
        }
    }
}
