package com.fourroro.nolleogasil_backend.entity.mate;

import com.fourroro.nolleogasil_backend.dto.mate.MateMemberDto;
import com.fourroro.nolleogasil_backend.entity.chat.ChatRoom;
import com.fourroro.nolleogasil_backend.entity.users.Users;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Matemember")
@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SequenceGenerator(
        name = "SEQ_MATEMEMBER_MATEMEMBER_ID",  //시퀀스 제너레이터 이름
        sequenceName = "SEQ_MATEMEMBER_MATEMEMBER_ID",  //시퀀스 이름
        initialValue = 1,  //시작값
        allocationSize = 1  //증가값
)
public class MateMember {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_MATEMEMBER_MATEMEMBER_ID")
    private Long matememberId;

    //member의 usersId
    //MateMember:Users = N:1 단방향 연관관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usersId")
    private Users users;

    //MateMember:Mate = N:1 양방향 연관관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mateId")
    private Mate mate;

    //MateMember:ChatRoom = N:1 단방향 연관관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chatroomId")
    private ChatRoom chatRoom;

    private int isFirst;

    private int isGiven;

    public void setIsFirst(int isFirst) {
        this.isFirst = isFirst;
    }

    public void setIsGiven(int isGiven) {
        this.isGiven = isGiven;
    }

    public static MateMember changeToEntity(MateMemberDto dto, Users users, Mate mate, ChatRoom chatRoom) {
        return MateMember.builder()
                .matememberId(dto.getMatememberId())
                .users(users)
                .mate(mate)
                .chatRoom(chatRoom)
                .isFirst(1) // 첫 입장 true
                .isGiven(dto.getIsGiven())
                .build();
    }

}
