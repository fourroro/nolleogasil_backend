package com.fourroro.nolleogasil_backend.controller.users;

import com.fourroro.nolleogasil_backend.dto.place.PlaceDto;
import com.fourroro.nolleogasil_backend.dto.users.UsersDto;
import com.fourroro.nolleogasil_backend.dto.users.WishDto;
import com.fourroro.nolleogasil_backend.entity.users.Wish;
import com.fourroro.nolleogasil_backend.service.place.PlaceService;
import com.fourroro.nolleogasil_backend.service.users.WishService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/wish")
public class WishController {

    private final WishService wishService;
    private final PlaceService placeService;
    private final RedisTemplate<String, Object> redisTemplate;

    //session에 있는 usersId 가져오기
    private Long getSessionUsersId(HttpSession session) {
//        ValueOperations<String, Object> operations = redisTemplate.opsForValue();
//        UsersDto usersDto = (UsersDto) operations.get("users");
        UsersDto usersDto = (UsersDto) session.getAttribute("users");

        return usersDto.getUsersId();
    }

    //wish 추가
    @PostMapping("/insertWish")
    public String insertWish(@RequestBody PlaceDto placeDto, @RequestParam String category, HttpSession session) {
        int placeCat = placeService.changeToPlaceCat(category);
        if (placeCat == 0) {
            return "failed";
        }

        Long usersId = getSessionUsersId(session);
        placeDto.setPlaceCat(placeCat);
        WishDto wishDto = WishDto.builder()
                        .usersId(usersId)
                        .place(placeDto)
                        .build();

        try {
            //이미 place table에 해당 place가 존재하는지 구분
            if (placeService.checkPlaceColumn(placeDto.getPlaceId())) {
                wishService.insertWish(wishDto);
            } else {
                placeService.insertPlace(placeDto);
                wishService.insertWish(wishDto);
            }
            return "successful";
        } catch(Exception e) {
            e.printStackTrace();
            return "failed";
        }
    }

    //wish column 유무 확인 -> wish에 없으면 false반환
    @GetMapping("/checkingWishStatus")
    public boolean checkingWishStatus(Integer placeId, HttpSession session) {
        Long usersId = getSessionUsersId(session);
        return wishService.checkWishColumn(usersId, placeId);
    }

    //wishList 조회
    @GetMapping("/getWishList")
    public List<WishDto> getWishList(int placeCat, String sortBy, HttpSession session) {
        Long usersId = getSessionUsersId(session);
        List<Wish> wishList;

        if (sortBy.equals("기본순")) {
            wishList = wishService.getWishList(usersId, placeCat);
        } else {
            wishList = wishService.getSortedWishList(usersId, placeCat, sortBy);
        }

        List<WishDto> wishDtoList = new ArrayList<>();
        for (Wish wish : wishList) {
            WishDto wishDto = WishDto.changeToDto(wish);
            wishDtoList.add(wishDto);
        }
        return wishDtoList;
    }

    //wish개수 조회
    @GetMapping("/countWish")
    public Long countWish(int placeCat, HttpSession session) {
        Long usersId = getSessionUsersId(session);

        if (placeCat == 0) {  //0: 전체
            return wishService.countWish(usersId);
        } else {
            return wishService.countWishByPlaceCat(usersId, placeCat);
        }
    }

    //wish 삭제
    @PostMapping("/deleteWish")
    public String deleteWish(@RequestBody PlaceDto placeDto, @RequestParam Long wishId, HttpSession session) {
        try {
            Long usersId = getSessionUsersId(session);
            Integer placeId = placeDto.getPlaceId();

            if (wishId == 0) {
                Wish wish = wishService.getWishByUsersIdAndPlaceId(usersId, placeId);
                wishService.deleteWish(wish.getWishId());
            } else {
                wishService.deleteWish(wishId);
            }
            return "successful";
        }  catch(Exception e) {
            e.printStackTrace();
            return "failed";
        }
    }
}