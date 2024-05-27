package com.fourroro.nolleogasil_backend.controller.chat;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
@Log4j2
public class StompHandler implements ChannelInterceptor {

    // JWT 토큰 사용 시..
/*
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public ChatBubble<?> preSend(ChatBubble<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        if(accessor.getCommand() == StompCommand.CONNECT) {
            if(!jwtTokenProvider.validateToken(accessor.getFirstNativeHeader("token")))
                try {
                    throw new AccessDeniedException("");
                } catch (AccessDeniedException e) {
                    e.printStackTrace();
                }
        }
        return message;
    }*/
  /*  @Override
    public ChatBubble<?> preSend(ChatBubble<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        if(accessor.getCommand() == StompCommand.CONNECT) {
            if(!tokenProvider.validateToken(accessor.getFirstNativeHeader("Authorization")))
                throw new AccessDeniedException("");
        }
        return message;
    }*/


    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        log.info("Stomp Hanler 실행");
        return message;
    }

}
