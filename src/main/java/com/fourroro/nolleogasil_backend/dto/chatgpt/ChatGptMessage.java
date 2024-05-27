package com.fourroro.nolleogasil_backend.dto.chatgpt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatGptMessage {
    private String role;
    private String content;

}
