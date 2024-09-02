/**
 * 맛집메이트의 채팅 메세지 관리를 위한 Service Interface입니다.
 * @author 홍유리
 * @since 2024-01-05
 */
package com.fourroro.nolleogasil_backend.service.chat;

import com.fourroro.nolleogasil_backend.dto.chat.ChatDto;

import java.util.List;

public interface ChatService {

    void enterChatRoom(Long chatroomId, Long usersId);
    void sendMessage(Long chatroomId, Long usersId, String sendMessage);
    List<ChatDto> getChatList(Long chatRoomId);

}
