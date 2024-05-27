package com.fourroro.nolleogasil_backend.dto.chat;

import com.fourroro.nolleogasil_backend.entity.chat.Chat;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;


@Builder
@Getter
public class ChatDto {

    private Long chatId;
    private Long usersId;
    private String nickname;
    private String message;
    private Long chatroomId;
    private LocalDateTime sendDate;
    private String messageType;


    public static ChatDto toChatDto(Chat chat) {
        return ChatDto.builder()
                .chatId(chat.getChatId())
                .usersId(chat.getUsers().getUsersId())
                .chatroomId(chat.getChatRoom().getChatroomId())
                .message(chat.getMessage())
                .sendDate(chat.getSendDate())
                .nickname(chat.getUsers().getNickname())
                .messageType(chat.getMessageType())
                .build();
    }

}
