package com.fourroro.nolleogasil_backend.dto.users;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TokenRequest {
    private String grant_type;
    private String client_id;
    private String redirect_uri;
    private String client_secret;
    private String code;
}

