package com.fourroro.nolleogasil_backend.service.chat;

import com.fourroro.nolleogasil_backend.dto.chat.ChatDto;

import java.util.List;

public interface ChatService {

    public void enterChatRoom(Long chatroomId, Long usersId);
    void sendMessage(Long chatroomId, Long usersId, String sendMessage);

    public List<ChatDto> getChatList(Long chatRoomId);

}
