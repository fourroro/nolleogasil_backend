package com.fourroro.nolleogasil_backend.dto.chatgpt;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ChatGptRequestDto {
    private String model;
    private List<ChatGptMessage> messages;

    public ChatGptRequestDto(String model, String prompt) {
        this.model = model;
        this.messages =  new ArrayList<>();
        this.messages.add(new ChatGptMessage("user", prompt));
    }
}
