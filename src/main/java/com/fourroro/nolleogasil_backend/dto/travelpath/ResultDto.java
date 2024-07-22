package com.fourroro.nolleogasil_backend.dto.travelpath;

import lombok.*;

import java.io.Serializable;
import java.util.List;


@Setter
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ResultDto implements Serializable {
    private List<String> dates;     //여행 일자
    private List<String> infos;     //여행 정보



}
