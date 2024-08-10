/**
 * 이 클래스는 맛집메이트 채팅관리를 위한 Controller입니다.
 * @author 홍유리
 * @since 2024-01-05
 */
package com.fourroro.nolleogasil_backend.controller.chat;

import com.fourroro.nolleogasil_backend.dto.chat.ChatDto;
import com.fourroro.nolleogasil_backend.service.chat.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService chatService;

    @GetMapping("/messages/{chatroomId}")
    public List<ChatDto> getChatList(@PathVariable Long chatroomId) {

        List<ChatDto> chatList = chatService.getChatList(chatroomId);
        return chatList;
    }

}

