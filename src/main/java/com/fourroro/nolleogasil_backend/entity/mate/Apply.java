package com.fourroro.nolleogasil_backend.entity.mate;

import com.fourroro.nolleogasil_backend.dto.mate.ApplyDto;
import com.fourroro.nolleogasil_backend.dto.mate.ApplyStatus;
import com.fourroro.nolleogasil_backend.entity.users.Users;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Apply")
@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SequenceGenerator(
        name = "SEQ_Apply_Apply_ID",  //시퀀스 제너레이터 이름
        sequenceName = "SEQ_Apply_Apply_ID",  //시퀀스 이름
        initialValue = 1,  //시작값
        allocationSize = 1  //증가값
)
public class Apply {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_Apply_Apply_ID")
    private Long applyId;

    //Mate:Apply = 1:N 단방향 연관관계
    @ManyToOne
    @JoinColumn(name = "mateId")
    private Mate mate;

    //Users:Apply = 1:N 단방향 연관관계
    @ManyToOne
    @JoinColumn(name = "applicantId")
    private Users users;

    @Enumerated(EnumType.STRING)
    private ApplyStatus isApply;    //신청 상태("대기", "수락", "거절" 中 1)

    //dto -> entity
    public static Apply changeToEntity(ApplyDto dto, Mate mate, Users users) {
        return Apply.builder()
                .mate(mate)
                .users(users)
                .isApply(dto.getIsApply())
                .build();
    }
}
