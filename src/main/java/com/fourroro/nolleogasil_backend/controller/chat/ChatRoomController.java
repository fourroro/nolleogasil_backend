package com.fourroro.nolleogasil_backend.controller.chat;


import com.fourroro.nolleogasil_backend.dto.chat.ChatRoomAndPlaceDto;
import com.fourroro.nolleogasil_backend.dto.users.UsersDto;
import com.fourroro.nolleogasil_backend.service.chat.ChatRoomService;
import com.fourroro.nolleogasil_backend.service.mate.MateMemberServiceImpl;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/chatRoom")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;
    private final MateMemberServiceImpl mateMemberService;
//    private final RedisTemplate<String, Object> redisTemplate;

    private Long getSessionUsersId(HttpSession session) {
//        ValueOperations<String, Object> operations = redisTemplate.opsForValue();
//        UsersDto usersDto = (UsersDto) operations.get("users");
        UsersDto usersDto = (UsersDto) session.getAttribute("users");

        return usersDto.getUsersId();
    }


    @GetMapping("/{chatroomId}")
    public ChatRoomAndPlaceDto getChatRoom(@PathVariable("chatroomId") String chatroomId) {
        Long chatRoomId = Long.parseLong(chatroomId);

        ChatRoomAndPlaceDto chatRoomAndPlaceDto = chatRoomService.getChatRoomAndPlace(chatRoomId);

        return chatRoomAndPlaceDto;

    }

    @GetMapping("/myRooms")
    public List<ChatRoomAndPlaceDto> getMyRooms(@RequestParam String sortedBy, HttpSession session) {
        System.out.println(sortedBy);
        Long userId = getSessionUsersId(session);
        List<ChatRoomAndPlaceDto> chatRoomAndPlaceDtoList = null;

        if(sortedBy.equals("기본순")) {
            chatRoomAndPlaceDtoList = chatRoomService.getChatRoomListByMaster(userId);
        } else {
            chatRoomAndPlaceDtoList = chatRoomService.getMyRoomsBySorted(userId,sortedBy);
        }

        return chatRoomAndPlaceDtoList;
    }

    @GetMapping("/joinedRooms")
    public List<ChatRoomAndPlaceDto> getJoinedRooms(@RequestParam String sortedBy, HttpSession session) {

        Long userId = getSessionUsersId(session);
        List<ChatRoomAndPlaceDto> chatRoomAndPlaceDtoList = null;
          if(sortedBy.equals("기본순")) {
              chatRoomAndPlaceDtoList = mateMemberService.getChatRoomListByMate(userId);
          } else {
              chatRoomAndPlaceDtoList = mateMemberService.getJoinedRoomsBySorted(userId,sortedBy);
          }

        return chatRoomAndPlaceDtoList;
    }


}
