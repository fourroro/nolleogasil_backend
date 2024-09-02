/**
 * 맛집메이트의 채팅메시지 Entity 객체 필드 및 연관관계를 나타내고, 관리하는 클래스입니다.
 * @author 홍유리
 * @since 2024-01-05
 *
 *
 * 리팩토링 내용:
 * 1. public Chat(ChatRoom room, Users users, String message, String messageType) 생성자 제거: 빌더 패턴을 사용하여 객체 생성을 간소화하고, 중복되는 생성 로직을 줄이기 위함.
 * 2. @NoArgsConstructor(access = AccessLevel.PROTECTED) 추가: 외부에서 무분별하게 객체를 생성하는 것을 방지하고, 빌더 또는 정적 팩토리 메서드를 통해 객체를 생성하도록 제한.
 * 3. GenerationType.SEQUENCE에서 GenerationType.IDENTITY로 변경하여 MySQL의 자동 증가(Auto Increment)를 사용하도록 수정.
 *
 **/
package com.fourroro.nolleogasil_backend.entity.chat;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fourroro.nolleogasil_backend.entity.users.Users;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;




@Entity
@Table(name="Chat")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Chat {

    @Id
    @Column(name="chatId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatId;

    private String message;

    @ManyToOne(fetch = FetchType.LAZY) //유저 -- 챗 (일대다관계)
    @JsonIgnore
    @JoinColumn(name="sender")
    private Users users;

    @ManyToOne(fetch = FetchType.LAZY) // 챗방 -- 챗 (일대다관계)
    @JsonIgnore
    @JoinColumn(name = "chatroomId")
    private ChatRoom chatRoom;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime sendDate;

    private String messageType;

    //구독한 챗방.보내는 유저.메세지 내용 을 받아 챗 객체 생성.
    public static Chat createChat(ChatRoom chatRoom, Users users, String message,LocalDateTime sendDate,String messageType) {
        return Chat.builder()
                .chatRoom(chatRoom)
                .users(users)
                .message(message)
                .sendDate(sendDate)
                .messageType(messageType)
                .build();
    }


}
