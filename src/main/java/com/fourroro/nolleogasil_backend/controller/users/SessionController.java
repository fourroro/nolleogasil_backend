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
        System.out.println("!!!!!!!!!!!세션 컨트롤러!!!!!!!!!!!!");
        if (sessionInterceptor.isSessionExpired(request)) {
            //세션이 존재하지 않거나 만료된 경우
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Session expired");
        } else {
            // 세션이 유효한 경우
            return ResponseEntity.ok("Session valid");
        }
    }
}
