package com.fourroro.nolleogasil_backend.auth.jwt.util;

import com.fourroro.nolleogasil_backend.auth.jwt.JwtTokenResponseDto;
import com.fourroro.nolleogasil_backend.entity.users.PrincipalDetails;
import com.fourroro.nolleogasil_backend.entity.users.Role;
import com.fourroro.nolleogasil_backend.entity.users.Users;
import com.fourroro.nolleogasil_backend.service.Oauth2.OAuth2Attribute;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class TokenProvider {

    private static final String AUTHORITIES_KEY = "auth";
    private static final String BEARER_TYPE = "bearer";
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30; // 30분
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7; // 7일

    private Key key;

    // application.yml에서 주입받은 secret 값을 base64 decode하여 key 변수에 할당
    public TokenProvider(@Value("${jwt.security-key}") String secret) {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public JwtTokenResponseDto generateTokenDto(Authentication authentication, String loginType) {

        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = (new Date()).getTime();
        Date accessExprTime = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);
        Date refreshExprTime = new Date(now + REFRESH_TOKEN_EXPIRE_TIME);

        String accessToken = "";
        String refreshToken = "";

        if(loginType.equals("SOCIAL")) {
            PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
            String subject = principalDetails.getSubject();

            accessToken = Jwts.builder()
                    .setSubject(subject)
                    .claim("attributes", principalDetails.getAttributes())
                    .claim(AUTHORITIES_KEY, authorities)
                    .signWith(key, SignatureAlgorithm.HS512)
                    .setExpiration(accessExprTime)
                    .compact();

            refreshToken = Jwts.builder()
                    .setSubject(subject)
                    .claim(AUTHORITIES_KEY, authorities)
                    .signWith(key, SignatureAlgorithm.HS512)
                    .setExpiration(refreshExprTime)
                    .compact();

        } else if (loginType.equals("COMMON")) {
            accessToken = Jwts.builder()
                    .setSubject(authentication.getName())       // payload "sub": memberNo
                    .claim(AUTHORITIES_KEY, authorities)        // payload "auth": "ROLE_*"
                    .signWith(key, SignatureAlgorithm.HS512)    // header "alg": "HS512"
                    .setExpiration(accessExprTime)              // payload "exp": 1516239022 (예시)
                    .compact();

            refreshToken = Jwts.builder()
                    .setSubject(authentication.getName())
                    .claim(AUTHORITIES_KEY, authorities)
                    .signWith(key, SignatureAlgorithm.HS512)
                    .setExpiration(refreshExprTime)
                    .compact();
        }

        return JwtTokenResponseDto.builder()
                .grantType(BEARER_TYPE)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .accessTokenExpires(ACCESS_TOKEN_EXPIRE_TIME - 5000) // 자동 로그아웃 기능을 위한 간극
                .accessTokenExpiresDate(accessExprTime)
                .build();

    }


    // JWT토큰을 복호화하여 토큰에 들어있는 정보를 꺼냅니다.
    public Authentication getAuthentication(String token) {


        // 서명 검증 : JWT 토큰이 유효한지와 변조되지 않았는지 확인
        // 무결성과 발신자의 신뢰성을 보장
        //  Jwts.parserBuilder():
        //      JJWT 라이브러리의 Jwts 클래스는 JWT 토큰을 생성하거나 검증하기 위한 도구.
        //      parserBuilder()를 사용해 JWT 파서를 생성하기 위한 빌더를 시작한다.
        //  setSigningKey(key):
        //      JWT의 서명을 검증하기 위해 서명에 사용된 비밀키 또는 공개키를 설정.
        //      이 **키(key)**는 JWT 토큰을 생성할 때 사용된 것과 동일한 키여야 한다.
        //      이 키를 통해 JWT가 서명된 방식과 일치하는지, 즉 토큰이 발급된 이후 변조되지 않았는지를 확인.
        //  build():
        //      빌더 패턴을 통해 JWT 파서를 생성합니다. 이제 이 파서를 사용하여 JWT의 서명 검증을 수행할 수 있게 됨.
        //  parseClaimsJws(token):
        //      전달받은 JWT 토큰(token)을 파싱하여 서명 검증을 수행한다.
        //      이 과정에서 토큰의 Header, Payload, Signature를 분리하고, 서명을 검증하여 유효성을 확인한다.
        //      서명이 올바르지 않거나, 서명에 사용된 키가 잘못되었거나, 토큰이 변조되었다면 예외가 발생한다 (SignatureException, ExpiredJwtException 등).
        //  .getBody():
        //      서명 검증이 성공한 경우, **토큰의 페이로드(Payload)**에서 클레임(Claims) 정보를 추출한다.
        //      Claims는 JWT의 페이로드에 있는 정보를 담고 있으며, 사용자 ID, 권한, 만료 시간 등과 같은 정보를 포함할 수 있다.
        //  서명 검증이 성공한 이후, 토큰의 클레임에서 **권한 정보(AUTHORITIES_KEY)**를 확인합니다.
        //  만약 권한 정보가 없으면, 예외를 발생시켜 해당 JWT가 유효하지 않음을 나타냅니다.
        Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        if(claims.get(AUTHORITIES_KEY) == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        // 사용자 정보 가져오기
        String email = claims.get("email").toString();
        String roleKey = claims.get(AUTHORITIES_KEY).toString();

        // Users 객체 생성
        Users user = Users.builder()
                .email(email)
                .role(Role.valueOf(roleKey)) // 역할 필드 설정
                .build();

        PrincipalDetails principal;
        // 토큰에서 OAuth2 사용자인지 여부를 판단할 수 있는 속성이 있는 경우 분기
        if (claims.get("provider") != null) {
            // OAuth2 로그인 처리
            Map<String, Object> attributes = claims.get("attributes", Map.class);
            principal = new PrincipalDetails(user, attributes, claims.getSubject());

        } else {
            // 일반 로그인 처리
            principal = new PrincipalDetails(user);
        }

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        }catch(io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서면입니다.");
        }catch(ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
        }catch(UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT토큰입니다.");
        }catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }

}
