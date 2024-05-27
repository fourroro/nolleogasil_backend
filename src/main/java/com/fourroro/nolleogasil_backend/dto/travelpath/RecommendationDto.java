package com.fourroro.nolleogasil_backend.dto.travelpath;

import com.fourroro.nolleogasil_backend.entity.travelpath.Recommendation;
import lombok.*;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RecommendationDto {
    private Long recommendationId;  //PK
    private Long travelpathId;      //FK (travelpathId)


    public static RecommendationDto changeToDto(Recommendation entity) {
        return RecommendationDto.builder()
                .recommendationId(entity.getRecommendationId())
                .travelpathId(entity.getTravelPath().getTravelpathId())
                .build();
    }

}
