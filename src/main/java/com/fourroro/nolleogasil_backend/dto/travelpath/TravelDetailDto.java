package com.fourroro.nolleogasil_backend.dto.travelpath;

import lombok.*;


@Setter
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TravelDetailDto {
    private String destination;                 //목적지
    private String startDate;                   //출발 날짜
    private String endDate;		                //오는 날짜
    private String partyItems;                  //일행
    private String placeItems;                  //장소
    private String conceptItems;                //컨셉
    private String foodItems;                   //음식
    private ResultDto resultDto;                //여행 일자 + 정보

    public boolean checkNullField() {
        return destination == null || startDate == null || endDate == null ||
                partyItems == null || placeItems == null || conceptItems == null || foodItems == null ||
                resultDto.getDates() == null || resultDto.getInfos() == null;
    }
}
