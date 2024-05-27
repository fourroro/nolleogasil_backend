package com.fourroro.nolleogasil_backend.dto.chat;

import com.fourroro.nolleogasil_backend.entity.chat.ChatRoom;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ChatRoomDto {

    private Long chatroomId;
    private String roomName;
    private Long usersId; //개설자
    private int maxNum;
    private Long mateId; // 맛집
    private int memberCnt;

    public static ChatRoomDto ChangeToDto(ChatRoom chatRoom,int memberCnt) {
        return ChatRoomDto.builder()
                .chatroomId(chatRoom.getChatroomId())
                .roomName(chatRoom.getRoomName())
                .usersId(chatRoom.getUsers().getUsersId())
                .maxNum(chatRoom.getMaxNum())
                .mateId(chatRoom.getMate().getMateId())
                .memberCnt(memberCnt)
                .build();

    }


}
