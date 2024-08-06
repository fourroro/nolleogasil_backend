/**
 * 위시(내 장소) Dto 객체를 구성, 관리하는 클래스입니다.
 * @author 박초은
 * @since 2024-01-05
 */
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
    private PlaceDto place;     //장소 id

    //entity -> dto
    public static WishDto changeToDto(Wish entity) {
        return WishDto.builder()
                .wishId(entity.getWishId())
                .usersId(entity.getUsers().getUsersId())
                .place(PlaceDto.changeToDto(entity.getPlace()))
                .build();
    }

}
