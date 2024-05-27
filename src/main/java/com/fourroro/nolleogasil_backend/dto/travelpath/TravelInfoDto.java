package com.fourroro.nolleogasil_backend.dto.travelpath;

import com.fourroro.nolleogasil_backend.entity.travelpath.TravelInfo;
import lombok.*;

@Setter
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TravelInfoDto {
    private Long travelinfoId;      //PK
    private String infos;           //여행 정보
    private Long recommendationId;  //FK (recommendationId)

    public static TravelInfoDto changeToDto(TravelInfo entity) {
        return TravelInfoDto.builder()
                .travelinfoId(entity.getTravelinfoId())
                .infos(entity.getInfos())
                .recommendationId(entity.getRecommendation().getRecommendationId())
                .build();
    }
}
