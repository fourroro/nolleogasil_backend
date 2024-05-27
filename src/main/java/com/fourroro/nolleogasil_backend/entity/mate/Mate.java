package com.fourroro.nolleogasil_backend.entity.mate;

import com.fourroro.nolleogasil_backend.dto.mate.MateDto;
import com.fourroro.nolleogasil_backend.dto.mate.MateFormDto;
import com.fourroro.nolleogasil_backend.entity.chat.ChatRoom;
import com.fourroro.nolleogasil_backend.entity.place.Place;
import com.fourroro.nolleogasil_backend.entity.users.Users;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Mate")
@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SequenceGenerator(
        name = "SEQ_MATE_MATE_ID",  //시퀀스 제너레이터 이름
        sequenceName = "SEQ_MATE_MATE_ID",  //시퀀스 이름
        initialValue = 1,  //시작값
        allocationSize = 1  //증가값
)
public class Mate {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_MATE_MATE_ID")
    private Long mateId;

    @ManyToOne
    @JoinColumn(name = "usersId")
    private Users users;

    private String title;
    private LocalDate eatDate;
    private String eatTime;
    private String gender;
    private int count;
    private String comments;
    private int display;

    //Mate:Place = 1:1 단방향 연관관계
    @OneToOne
    @JoinColumn(name = "placeId")
    private Place place;

    //Mate:ChatRoom = 1:1 양방향 연관관계
    @OneToOne(mappedBy = "mate", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private ChatRoom chatRoom;

    //Mate:MateMember = 1:N 양방향 연관관계
    @OneToMany(mappedBy = "mate", fetch = FetchType.LAZY)
    private List<MateMember> mateMembers;

    public static Mate toEntity(MateFormDto requestMateDto, Users users, Place place) {
        return Mate.builder()
                .users(users)
                .title(requestMateDto.getTitle())
                .eatDate(requestMateDto.getEatDate())
                .eatTime(requestMateDto.getEatTime())
                .place(place)
                .comments(requestMateDto.getComments())
                .gender(requestMateDto.getGender())
                .count(requestMateDto.getCount())
                .display(1)
                .mateMembers(new ArrayList<>())
                .build();
    }

    public static Mate changeToEntity(MateDto dto, Users users) {
        return Mate.builder()
                .mateId(dto.getMateId())
                .users(users)
                .title(dto.getTitle())
                .eatDate(dto.getEatDate())
                .eatTime(dto.getEatTime())
                .gender(dto.getGender())
                .count(dto.getCount())
                .comments(dto.getComments())
                .display(dto.getDisplay())
                .place(Place.changeToEntity(dto.getPlace()))
                .build();
    }

}