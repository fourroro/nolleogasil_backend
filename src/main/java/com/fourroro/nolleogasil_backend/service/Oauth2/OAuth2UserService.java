package com.fourroro.nolleogasil_backend.service.Oauth2;/*
package com.fourroro.nolleogasil_backend.service.Oauth2;

import com.fourroro.nolleogasil_backend.entity.users.Users;
import com.fourroro.nolleogasil_backend.repository.users.UsersRepository;
import com.fourroro.nolleogasil_backend.service.users.UsersService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OAuth2UserService extends DefaultOAuth2UserService {


    @Autowired
    private final UsersService usersService;

    @Autowired
    private final UsersRepository usersRepository;


    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        // OAuth2UserService를 사용하여 OAuth2User 정보를 가져온다.
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // 클라이언트 등록 ID(google, naver, kakao)와 사용자 이름 속성을 가져온다.
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        // 사용자의 고유 식별자를 나타내는 속성
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        System.out.println(userNameAttributeName);

        // OAuth2UserService를 사용하여 가져온 OAuth2User 정보로 OAuth2Attribute 객체를 만든다.
        OAuth2Attribute oAuth2Attribute =
                OAuth2Attribute.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());


        // OAuth2Attribute의 속성값들을 Map으로 반환 받는다.
        Map<String, Object> usersAttribute = oAuth2Attribute.convertToMap();

        saveOrUpdate(oAuth2Attribute);


        // 회원의 권한과, 회원속성, 속성이름을 이용해 DefaultOAuth2User 객체를 생성해 반환한다.
        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USERS")),
                usersAttribute, userNameAttributeName);

    }

    private Users saveOrUpdate(OAuth2Attribute oAuth2Attribute) {
        Users users = usersRepository.findByEmailAndProvider(
                oAuth2Attribute.getEmail(),oAuth2Attribute.getProvider())
                .map(m -> m.update(oAuth2Attribute.getName(),oAuth2Attribute.getEmail()))
                .orElse(oAuth2Attribute.toUsers());

        return users;
    }


}

*/
