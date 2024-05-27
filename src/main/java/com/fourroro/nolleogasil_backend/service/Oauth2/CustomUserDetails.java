package com.fourroro.nolleogasil_backend.service.Oauth2;/*
package com.fourroro.nolleogasil_backend.service.Oauth2;

import com.fourroro.nolleogasil_backend.entity.users.Users;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;


@Getter
@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails, OAuth2User {
    private Long id;
    private String email;
    private Collection<? extends GrantedAuthority> authorities;
    private Map<String, Object> attributes;

    public static CustomUserDetails create(Users users) {
        List<GrantedAuthority> authorities = Collections.
                singletonList(new SimpleGrantedAuthority("ROLE_USER"));

        return CustomUserDetails.builder()
                .id(users.getUsersId())
                .email(users.getEmail())
                .authorities(authorities)
                .build();
    }

    public static CustomUserDetails create(Users users, Map<String, Object> attributes) {
        CustomUserDetails userDetails = CustomUserDetails.create(users);
        userDetails.setAttributes(attributes);
        return userDetails;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }
}

*/
