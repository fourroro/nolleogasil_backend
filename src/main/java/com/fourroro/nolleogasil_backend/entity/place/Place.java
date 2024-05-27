package com.fourroro.nolleogasil_backend.entity.place;

import com.fourroro.nolleogasil_backend.dto.place.PlaceDto;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "Place")
@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Place {

    @Id
    private Integer placeId;
    private String placeName;
    private String placeAddress;
    private String placeRoadAddress;
    private String placePhone;
    private String placeUrl;
    private double placeLat;
    private double placeLng;
    private int placeCat;

    public static Place changeToEntity(PlaceDto dto) {
        return Place.builder()
                .placeId(dto.getPlaceId())
                .placeName(dto.getPlaceName())
                .placeAddress(dto.getPlaceAddress())
                .placeRoadAddress(dto.getPlaceRoadAddress())
                .placePhone(dto.getPlacePhone())
                .placeUrl(dto.getPlaceUrl())
                .placeLat(dto.getPlaceLat())
                .placeLng(dto.getPlaceLng())
                .placeCat(dto.getPlaceCat())
                .build();
    }

}
