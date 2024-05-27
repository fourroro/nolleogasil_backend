package com.fourroro.nolleogasil_backend.session;

import com.fourroro.nolleogasil_backend.dto.users.UsersDto;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSession {

    private UsersDto userInfo;

    public static UserSession creatUsersSession(UsersDto usersDto) {
        return UserSession.builder()
                .userInfo(usersDto)
                .build();
    }


}
