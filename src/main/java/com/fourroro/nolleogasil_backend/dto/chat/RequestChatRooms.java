package com.fourroro.nolleogasil_backend.dto.chat;

import com.fourroro.nolleogasil_backend.entity.chat.ChatRoom;
import com.fourroro.nolleogasil_backend.entity.mate.Mate;
import com.fourroro.nolleogasil_backend.entity.users.Users;

public class RequestChatRooms {
    private String roomName;

    private int maxNum;

    private Long usersId;


    public ChatRoom toEntity(Users users, Mate mate) {

        return ChatRoom.builder()
                .users(users)
                .mate(mate)
                .maxNum(maxNum)
                .roomName(roomName)
                .build();
    }

}
