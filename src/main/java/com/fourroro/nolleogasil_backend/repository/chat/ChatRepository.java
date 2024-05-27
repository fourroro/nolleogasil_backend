package com.fourroro.nolleogasil_backend.repository.chat;

import com.fourroro.nolleogasil_backend.entity.chat.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ChatRepository extends JpaRepository<Chat, Long> {

    public List<Chat> findChatsByChatRoomChatroomId(Long chatroomId);

    public Chat findChatByChatRoomChatroomIdAndMessageType(Long chatroomId,String message);
}