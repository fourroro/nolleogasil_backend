package com.fourroro.nolleogasil_backend.entity.travelpath;

import com.fourroro.nolleogasil_backend.dto.travelpath.RecommendationDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
@Entity
@Table(name="Recommendation")
@Builder
@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) //기본 생성자
@AllArgsConstructor //전체 변수 생성하는 생성자
@SequenceGenerator(
        name = "SEQ_RECOMMENDATION_RECOMMENDATION_ID",  //시퀀스 제너레이터 이름
        sequenceName = "SEQ_RECOMMENDATION_RECOMMENDATION_ID",  //시퀀스 이름
        initialValue = 1,  //시작값
        allocationSize = 1  //증가값
)
public class Recommendation {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_RECOMMENDATION_RECOMMENDATION_ID")
    private Long recommendationId;  //PK

    //Recommendation : TravelDate = 1 : N (단방향 연관관계)
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "recommendation", fetch = FetchType.LAZY)
    private List<TravelDate> dates = new ArrayList<>(); //여행 일자

    //Recommendation : TravelInfo = 1 : N (단방향 연관관계)
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "recommendation", fetch = FetchType.LAZY)
    private List<TravelInfo> infos = new ArrayList<>(); //여행 정보

    //Recommendation : TravelPath = 1 : 1 (양방향 연관관계)
    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "travelpathId")
    private TravelPath travelPath;  //TravelPath


    public static Recommendation changeToEntity(RecommendationDto dto, TravelPath travelPath) {
        return Recommendation.builder()
                .travelPath(travelPath)
                .build();
    }

}
