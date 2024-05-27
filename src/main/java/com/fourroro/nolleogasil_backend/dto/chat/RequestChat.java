package com.fourroro.nolleogasil_backend.dto.chat;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonFormat
@Builder
public class RequestChat {

    private String nickname;
    private String message;


}
