package com.fourroro.nolleogasil_backend.dto.users;

import lombok.*;

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