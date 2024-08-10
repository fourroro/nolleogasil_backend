/**
 * 이 클래스는 맛집메이트 채팅관리를 위한 Controller입니다.
 * @author 홍유리
 * @since 2024-01-05
 */
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
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Log4j2
public class StompRabbitController {

    private final ChatService chatService;
    private final RabbitTemplate rabbitTemplate;
    @MessageMapping("chat.enter")
    public void enterTest(@Payload Map<String, Object> message,
                          SimpMessageHeaderAccessor headerAccessor) {
        UsersDto usersSession = (UsersDto) headerAccessor.getSessionAttributes().get("users");
        Long usersId = usersSession.getUsersId();
        Long chatroomId = Long.parseLong(message.get("chatroomId").toString());

        chatService.enterChatRoom(chatroomId,usersId);

        rabbitTemplate.convertAndSend("amq.topic","room." + chatroomId, message);
    }

    @MessageMapping("chat.send")
    public void sendMessage(@Payload Map<String, Object> message, SimpMessageHeaderAccessor headerAccessor) {
        UsersDto usersSession = (UsersDto) headerAccessor.getSessionAttributes().get("users");
        Long chatroomId = Long.parseLong(message.get("chatroomId").toString());
        Long usersId = usersSession.getUsersId();
        String sendMessage = message.get("message").toString();

        chatService.sendMessage(chatroomId,usersId,sendMessage);

        try {
            rabbitTemplate.convertAndSend("amq.topic","room." + chatroomId, message);
        } catch  (Exception e) {
            System.err.println("Failed to send message to RabbitMQ: " + e.getMessage());
        }
    }
}
