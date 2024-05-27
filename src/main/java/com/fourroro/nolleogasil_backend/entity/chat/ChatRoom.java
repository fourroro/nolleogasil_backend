package com.fourroro.nolleogasil_backend.entity.chat;

import com.fourroro.nolleogasil_backend.dto.chat.ChatRoomDto;
import com.fourroro.nolleogasil_backend.entity.mate.Mate;
import com.fourroro.nolleogasil_backend.entity.mate.MateMember;
import com.fourroro.nolleogasil_backend.entity.users.Users;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="Chatroom")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SequenceGenerator(
        name = "SEQ_CHATROOM_CHATROOM_ID",  //시퀀스 제너레이터 이름
        sequenceName = "SEQ_CHATROOM_CHATROOM_ID",  //시퀀스 이름
        initialValue = 1,  //시작값
        allocationSize = 1  //증가값
)
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_CHATROOM_CHATROOM_ID")
    @Column(name = "chatroomId")
    private Long chatroomId;
    private String roomName; //채팅방 이름

    @ManyToOne(fetch = FetchType.LAZY) // 챗방 -- 유저(개설자) 다대일
    @JoinColumn(name = "masterId")
    private Users users;

    @Column(name = "maxNum")
    private int maxNum; //채팅방 최대 참여자수

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mateId", nullable = false, unique = true)
    private Mate mate;

/*
    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL,  orphanRemoval = true) // 챗방 -- 챗 (일대다관계)
    private List<Chat> chats = new ArrayList<>();
*/
    @OneToMany(mappedBy = "chatRoom" ,cascade = CascadeType.ALL, orphanRemoval = true) // 챗방 -- 메이트멤버 (일대다관계)
    private List<MateMember> mateMembers;

    public static ChatRoom toEntity(ChatRoomDto chatRoomDto,Users users,Mate mate) {
        return ChatRoom.builder()
                .chatroomId(chatRoomDto.getChatroomId())
                .roomName(chatRoomDto.getRoomName())
                .maxNum(chatRoomDto.getMaxNum())
                .mate(mate)
                .users(users)
                .mateMembers(new ArrayList<>())
                .build();
    }

    public static ChatRoom creatChatroom(Mate savedMate,Users users) {
        return ChatRoom.builder()
                .roomName(savedMate.getTitle())
                .users(users)
                .maxNum(savedMate.getCount())
                .mate(savedMate)
                .mateMembers(new ArrayList<>())
                .build();
    }





}
