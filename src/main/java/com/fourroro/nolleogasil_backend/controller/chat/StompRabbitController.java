package com.fourroro.nolleogasil_backend.controller.chat;


import com.fourroro.nolleogasil_backend.dto.users.UsersDto;
import com.fourroro.nolleogasil_backend.service.chat.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Log4j2
public class StompRabbitController {

    private final ChatService chatService;
    private final Map<String, Long> simpSessionIdMap = new HashMap<>(); // stomp에 CONNECTION 한 유저 정보

    private final RabbitTemplate rabbitTemplate;
    private static final String CHAT_EXCHANGE_NAME = "chat.exchange";
    @MessageMapping("chat.enter")
    public void enterTest(@Payload Map<String, Object> message,
                          SimpMessageHeaderAccessor headerAccessor) {
        UsersDto usersSession = (UsersDto) headerAccessor.getSessionAttributes().get("users");
        System.out.println(usersSession.getUsersId());
        Long usersId = usersSession.getUsersId();
        System.out.println(usersId + "입장 중 ..");
        System.out.println(message.get("chatroomId").toString());
        Long chatroomId = Long.parseLong(message.get("chatroomId").toString());
        String enterMessage = usersId + "님이 입장하셨습니다.";

        chatService.enterChatRoom(chatroomId,usersId);

        rabbitTemplate.convertAndSend(CHAT_EXCHANGE_NAME,"room." + chatroomId, message);
    }

    @MessageMapping("chat.send")
    public void sendMessage(@Payload Map<String, Object> message, SimpMessageHeaderAccessor headerAccessor) {
        UsersDto usersSession = (UsersDto) headerAccessor.getSessionAttributes().get("users");
        System.out.println(usersSession.getUsersId());
        Long chatroomId = Long.parseLong(message.get("chatroomId").toString());
        Long usersId = usersSession.getUsersId();
        String sendMessage = message.get("newMessage").toString();

        System.out.println(chatroomId);
        chatService.sendMessage(chatroomId,usersId,sendMessage);

        try {
            System.out.println("sending...");
            rabbitTemplate.convertAndSend(CHAT_EXCHANGE_NAME, "room." + chatroomId, message);
            System.out.println("Message sent to RabbitMQ: " + message);

        } catch  (Exception e) {
            System.err.println("Failed to send message to RabbitMQ: " + e.getMessage());
        }
    }
}
