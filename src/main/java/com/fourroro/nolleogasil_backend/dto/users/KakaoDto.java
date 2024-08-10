package com.fourroro.nolleogasil_backend.dto.users;

import lombok.*;

/**
 * 이 클래스는 카카오 로그인을 위한 DTO입니다.
 * @author 장민정
 * @since 2024-01-05
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class KakaoDto{
    private String name;
    private String email;
    private String nickname;
    private String phone;
    private String gender;

    public UsersDto toDto(){
        return UsersDto.builder()
                .name(this.name)
                .email(this.email)
                .nickname(this.nickname)
                .phone(this.phone)
                .gender(this.gender)
                .build();
    }
}