package com.fourroro.nolleogasil_backend.dto.chatgpt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
//Chat GPT 답변을 담을 DTO
public class ChatGptResponseDto {
    private List<Choice> choices;
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Choice {
        private int index;
        private ChatGptMessage message;

    }
}
