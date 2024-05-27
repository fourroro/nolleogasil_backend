package com.fourroro.nolleogasil_backend.dto.users;

import com.fourroro.nolleogasil_backend.dto.place.PlaceDto;
import com.fourroro.nolleogasil_backend.entity.users.Wish;
import lombok.*;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class WishDto {

    private Long wishId;        //PK
    private Long usersId;       //사용자 id
    private PlaceDto place;    //장소 id

    public static WishDto changeToDto(Wish entity) {
        return WishDto.builder()
                .wishId(entity.getWishId())
                .usersId(entity.getUsers().getUsersId())
                .place(PlaceDto.changeToDto(entity.getPlace()))
                .build();
    }

}
