/**
 * 맛집메이트의 채팅메시지 Entity 객체 필드 및 연관관계를 나타내고, 관리하는 클래스입니다.
 * @author 홍유리
 * @since 2024-01-05
 */
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
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SequenceGenerator(
        name = "SEQ_CHAT_CHAT_ID",  //시퀀스 제너레이터 이름
        sequenceName = "SEQ_CHAT_CHAT_ID",  //시퀀스 이름
        initialValue = 1,  //시작값
        allocationSize = 1  //증가값
)
public class Chat {

    @Id
    @Column(name="chatId")
    @GeneratedValue(strategy = GenerationType.AUTO,generator = "SEQ_CHAT_CHAT_ID")
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

    public Chat(ChatRoom room, Users users, String message,String messageType) {
        this.chatRoom = room;
        this.users = users;
        this.message = message;
        this.sendDate = LocalDateTime.now();
        this.messageType = messageType;

    }

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
