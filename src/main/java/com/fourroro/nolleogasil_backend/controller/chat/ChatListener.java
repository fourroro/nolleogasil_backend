package com.fourroro.nolleogasil_backend.controller.chat;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class ChatListener {

    private final SimpMessagingTemplate messagingTemplate;
    private final ObjectMapper objectMapper;

    @RabbitListener(queues = "chat.queue")
    public void receiveMessage(Map<String, Object> message) {
        try {
            Long chatroomId = Long.parseLong(message.get("chatroomId").toString());
            // WebSocket을 통 클라이언트로 브로드캐스트
            messagingTemplate.convertAndSend("/chat.exchange/room." + chatroomId, message);
        } catch (Exception e) {
            e.printStackTrace();
            // 예외 처리 로직 추가
        }
    }

}

