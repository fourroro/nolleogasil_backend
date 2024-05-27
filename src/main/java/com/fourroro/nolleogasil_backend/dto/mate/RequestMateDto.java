package com.fourroro.nolleogasil_backend.dto.mate;


import com.fourroro.nolleogasil_backend.dto.place.PlaceDto;
import lombok.*;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RequestMateDto {

    private MateFormDto mateFormDto;
    private PlaceDto placeDto;


}
