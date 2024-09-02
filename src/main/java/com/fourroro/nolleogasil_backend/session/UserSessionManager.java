/*
package com.fourroro.nolleogasil_backend.session;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class UserSessionManager {
    private Map<String, UserSession> userSessions;
    private static final String SESSION_COOKIE_NAME = "mySessionId";

    public UserSessionManager(){
        this.userSessions = new HashMap<>();
    }

    public void createSession(UserSession usersSession, HttpServletRequest request, HttpServletResponse response) {
        String sessionId = UUID.randomUUID().toString();
        userSessions.put(sessionId, usersSession);

        HttpSession session = request.getSession();
        session.setAttribute("usersSession",usersSession);

        Cookie mySessionCookie = new Cookie(SESSION_COOKIE_NAME, sessionId);
        response.addCookie(mySessionCookie);

    }

    public UserSession getUserSession(HttpServletRequest request) {
        Cookie sessionCookie = findCookie(request, SESSION_COOKIE_NAME);

        if(sessionCookie == null) {
            return null;
        }

        return userSessions.get(sessionCookie.getValue());
    }

    private Cookie findCookie(HttpServletRequest request,String cookieName) {
        return Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals(cookieName))
                .findAny()
                .orElse(null);
    }

    public void removeUserSession(Long sessionId){
        userSessions.remove(sessionId);
    }
}*/
