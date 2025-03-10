package com.fourroro.nolleogasil_backend.service.users;

import com.nimbusds.oauth2.sdk.token.RefreshToken;

public interface RefreshTokenService {
    void deleteByEmail(String userId);
    void saveToken(String userId, String refreshToken);
}
