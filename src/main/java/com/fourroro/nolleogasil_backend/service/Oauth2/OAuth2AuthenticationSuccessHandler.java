package com.fourroro.nolleogasil_backend.service.Oauth2;

import com.fourroro.nolleogasil_backend.auth.jwt.JwtTokenResponseDto;
import com.fourroro.nolleogasil_backend.auth.jwt.util.TokenProvider;
import com.fourroro.nolleogasil_backend.entity.users.PrincipalDetails;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Log4j2
@RequiredArgsConstructor
@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final TokenProvider tokenProvider;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        System.out.println("인증 성공");
        // OAuth2User에서 필요한 사용자 정보 확인
        PrincipalDetails oAuth2User = (PrincipalDetails) authentication.getPrincipal();
        log.info("OAuth2 인증 성공. 사용자: {}", oAuth2User.getAttributes());

        System.out.println(oAuth2User.getName());


        JwtTokenResponseDto jwtTokenResponseDto = tokenProvider.generateTokenDto(authentication,"SOCIAL");

        // 리다이렉트 URL 생성
        String redirectUrl = makeRedirectUrl(jwtTokenResponseDto.getAccessToken(), jwtTokenResponseDto.getRefreshToken());

        // 프론트엔드로 리다이렉트
        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }

    private String makeRedirectUrl(String accessToken, String refreshToken) {
        return UriComponentsBuilder.fromUriString("http://localhost:3000/oauth2/redirect")
                .queryParam("accessToken", accessToken)
                .queryParam("refreshToken", refreshToken)
                .build().toUriString();
    }
}

