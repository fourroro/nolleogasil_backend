package com.fourroro.nolleogasil_backend.entity.travelpath;


import com.fourroro.nolleogasil_backend.dto.travelpath.TravelInfoDto;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="Travelinfo")
@Builder
@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) //기본 생성자
@AllArgsConstructor //전체 변수 생성하는 생성자
@SequenceGenerator(
        name = "SEQ_TRAVELINFO_TRAVELINFO_ID",  //시퀀스 제너레이터 이름
        sequenceName = "SEQ_TRAVELINFO_TRAVELINFO_ID",  //시퀀스 이름
        initialValue = 1,  //시작값
        allocationSize = 1  //증가값
)
public class TravelInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_TRAVELINFO_TRAVELINFO_ID")
    private Long travelinfoId;              //PK

    private String infos;                   //여행 정보

    @ManyToOne
    @JoinColumn(name="recommendationId")
    private Recommendation recommendation;  //Recommendation

    public static TravelInfo changeToEntity(TravelInfoDto dto, Recommendation recommendation) {
        if (dto.getTravelinfoId() != null) { // 이미 travelInfoId가 존재하는 경우
            return TravelInfo.builder()
                    .travelinfoId(dto.getTravelinfoId()) // 기존의 travelInfoId 사용
                    .infos(dto.getInfos())
                    .recommendation(recommendation)
                    .build();
        } else {                            // travelInfoId가 존재하지 않는 경우
            return TravelInfo.builder()
                    .infos(dto.getInfos())
                    .recommendation(recommendation)
                    .build();
        }
    }
}
