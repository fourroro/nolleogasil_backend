/*
 * 맛집메이트의 멤버 Entity 객체 필드 및 연관관계를 나타내고, 관리하는 클래스입니다.
 * @author 박초은
 * @since 2024-01-05
 */
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

    private int isFirst;    //채팅방에 처음 입장인지(처음입장이라면 1, 아니면 0)

    private int isGiven;    //동일 mate의 member들에게 온도를 부여했는지(default: 1, 부여했다면 0으로 변경)

    public void setIsFirst(int isFirst) {
        this.isFirst = isFirst;
    }

    public void setIsGiven(int isGiven) {
        this.isGiven = isGiven;
    }

    //dto -> entity
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
