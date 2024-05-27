package com.fourroro.nolleogasil_backend.dto.travelpath;


import com.fourroro.nolleogasil_backend.entity.travelpath.TravelDate;
import lombok.*;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TravelDateDto {

    private Long traveldateId;      //PK
    private String dates;           //여행 일자
    private Long recommendationId;  //FK (recommendationId)

    public static TravelDateDto changeToDto(TravelDate entity) {
        return TravelDateDto.builder()
                .traveldateId(entity.getTraveldateId())
                .dates(entity.getDates())
                .recommendationId(entity.getRecommendation().getRecommendationId())
                .build();
    }
}
