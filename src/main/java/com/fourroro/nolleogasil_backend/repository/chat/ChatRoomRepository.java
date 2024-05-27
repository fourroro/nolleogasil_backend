package com.fourroro.nolleogasil_backend.repository.chat;

import com.fourroro.nolleogasil_backend.entity.chat.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRoomRepository extends JpaRepository<ChatRoom,Long>{
    public ChatRoom findByMateMateId(Long mateId);

    public List<ChatRoom> findAllByUsersUsersId(Long usersId);

    public List<ChatRoom> findAllByUsersUsersIdOrderByMateEatDateDesc(Long usersId);

    public List<ChatRoom> findAllByUsersUsersIdOrderByMate_EatDate(Long usersId);

    // public ChatRoomByMate findById(Long chatRoomId);
   // public Optional<ChatRoom> findChatRoomByMate_MateId(Long mateId);
}
