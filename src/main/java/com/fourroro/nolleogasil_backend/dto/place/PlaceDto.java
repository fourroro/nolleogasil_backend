package com.fourroro.nolleogasil_backend.dto.place;

import com.fourroro.nolleogasil_backend.entity.place.Place;
import lombok.*;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PlaceDto {

    private Integer placeId;            //PK //장소 id
    private String placeName;           //장소 이름
    private String placeAddress;        //주소
    private String placeRoadAddress;    //도로명 주소
    private String placePhone;          //장소 전화번호
    private String placeUrl;            //장소 url
    private double placeLat;            //위도
    private double placeLng;            //경도
    private int placeCat;               //맛집(1), 카페(2), 숙소(3), 관광지(4) 中 1
    private double distance;               //현재위치에서부터의 거리

    public void setPlaceCat(int placeCat) {
        this.placeCat = placeCat;
    }

    public void setDistance(double distance) { this.distance = distance; }

    public static PlaceDto changeToDto(Place entity) {
        return PlaceDto.builder()
                .placeId(entity.getPlaceId())
                .placeName(entity.getPlaceName())
                .placeAddress(entity.getPlaceAddress())
                .placeRoadAddress(entity.getPlaceRoadAddress())
                .placePhone(entity.getPlacePhone())
                .placeUrl(entity.getPlaceUrl())
                .placeLat(entity.getPlaceLat())
                .placeLng(entity.getPlaceLng())
                .placeCat(entity.getPlaceCat())
                .build();
    }

}
