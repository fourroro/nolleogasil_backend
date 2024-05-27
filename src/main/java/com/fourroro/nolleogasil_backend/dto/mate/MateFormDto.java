package com.fourroro.nolleogasil_backend.dto.mate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@JsonFormat
public class MateFormDto {

    private String title;
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate eatDate;
    private String eatTime;
    private String gender;
    private int count;
    private String comments;

    public MateFormDto(String title, LocalDate eatDate, String eatTime, String gender, String comments, int count) {
        this.title = title;
        this.eatDate = eatDate;
        this.eatTime = eatTime;
        this.gender = gender;
        this.comments = comments;
        this.count = count;
    }


   /* public Mate toEntity(RequestMateDto requestMateDto, Users users) {
        return Mate.builder()
                .users(users)
                .title(requestMateDto.getTitle())
                .eatDate(requestMateDto.getEatDate())
                .eatTime(requestMateDto.getEatTime())
                .placeId(requestMateDto.getPlaceId())
                .placeName(requestMateDto.getPlaceName())
                .comments(requestMateDto.getComments())
                .gender(requestMateDto.getGender())
                .count(requestMateDto.getCount())
                .display(1)
                .build();

    }*/




}
