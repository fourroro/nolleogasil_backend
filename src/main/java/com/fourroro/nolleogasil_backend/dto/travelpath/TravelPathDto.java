package com.fourroro.nolleogasil_backend.dto.travelpath;

import com.fourroro.nolleogasil_backend.entity.travelpath.TravelPath;
import lombok.*;


@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TravelPathDto {

    private Long travelpathId;            //PK
    private String arrival;               //목적지
    private String startDate;             //출발 날짜
    private String endDate;		          //오는 날짜
    private Long usersId;                 //FK (usersId)


    public static TravelPathDto changeToDto(TravelPath entity){
        return TravelPathDto.builder()
                .travelpathId(entity.getTravelpathId())
                .arrival(entity.getArrival())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .usersId(entity.getUsers().getUsersId())
                .build();

    }





}
