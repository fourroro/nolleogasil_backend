package com.fourroro.nolleogasil_backend.dto.travelpath;

import lombok.*;

import java.util.List;


@Setter
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ResultDto {
    private List<String> dates;     //여행 일자
    private List<String> infos;     //여행 정보



}
