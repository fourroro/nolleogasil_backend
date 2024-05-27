package com.fourroro.nolleogasil_backend.config;/*
package com.fourroro.nolleogasil_backend.config;


import com.fourroro.nolleogasil_backend.repository.users.CookieAuthorizationRequestRepository;
import com.fourroro.nolleogasil_backend.service.Oauth2.OAuth2AuthenticationFailureHandler;
import com.fourroro.nolleogasil_backend.service.Oauth2.OAuth2AuthenticationSuccessHandler;
import com.fourroro.nolleogasil_backend.service.Oauth2.OAuth2UserService;
import jakarta.servlet.DispatcherType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Collections;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {


    @Autowired
    private final OAuth2UserService oAuth2UserService;

    @Autowired
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

    @Autowired
    private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;
    @Autowired
    private CookieAuthorizationRequestRepository cookieAuthorizationRequestRepository;

    // ⭐️ CORS 설정
    CorsConfigurationSource corsConfigurationSource() {
        return request -> {
            CorsConfiguration config = new CorsConfiguration();
            config.setAllowedHeaders(Collections.singletonList("*"));
            config.setAllowedMethods(Collections.singletonList("*"));
            config.setAllowedOriginPatterns(Collections.singletonList("http://localhost:3000")); // ⭐️ 허용할 origin
            config.setAllowCredentials(true);
            return config;
        };
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
       return http
               .csrf(csrf -> csrf // CSRF 보호를 설정
                       .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()) // CSRF 토큰 저장소 설정
               )
               .cors(corsConfigurer -> corsConfigurer.configurationSource(corsConfigurationSource()))
               .sessionManagement(sessionManagement ->
                       sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
               )
               .formLogin(formLogin ->
                       formLogin.disable() // 폼 로그인 비활성화
               )
               .httpBasic(httpBasic ->
                       httpBasic.disable() // HTTP 기본 인증 비활성화
               )
               .authorizeHttpRequests(authorizeRequest -> authorizeRequest
                       .requestMatchers(
                               AntPathRequestMatcher
                                       .antMatcher("/auth/**")
                                       .antMatcher("/oauth2/**")
                                       .antMatcher("/mate/mateForm")
                                       .antMatcher("/map/*")
                                       .antMatcher("/checkingWishStatus")

                       ).permitAll()
                       .anyRequest().authenticated())
               .oauth2Login(oauth2 -> oauth2
                       //소셜 로그인 url
                       //authorizationEndpoint : 프론트엔드에서 백엔드로 소셜로그인 요청을 보내는 URI입니다
                       //기본 URI는 /oauth2/authorization/{provider} 입니다. ex) /oauth2/authorization/google
                       //URI를 변경하고 싶으면 baseUri(uri)를 사용하여 설정합니다.
                       .authorizationEndpoint(authorize -> authorize
                               .baseUri("/oauth/authorize")
                               //권한 요청과 관련된 모든 상태 이곳에 저장.
                               .authorizationRequestRepository(cookieAuthorizationRequestRepository))

                       .defaultSuccessUrl("/")

                       //인증후 리다이렉트되는 uri
                       .redirectionEndpoint(redirect -> redirect
                               .baseUri("/api/user/callback/*"))
                       .failureUrl("/users/login")
                       // OAuth2 인증 과정에서 Authentication 생성에 필요한 OAuth2User 를 반환하는 클래스를 지정한다.
                       .userInfoEndpoint(userInfo -> userInfo.userService(oAuth2UserService))
                       // 회원정보처리
                       .successHandler(oAuth2AuthenticationSuccessHandler)
                       .failureHandler(oAuth2AuthenticationFailureHandler)
               ).build();

    }


}
*/
