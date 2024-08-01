package com.fourroro.nolleogasil_backend.controller.users;

import com.fourroro.nolleogasil_backend.dto.place.PlaceDto;
import com.fourroro.nolleogasil_backend.dto.users.UsersDto;
import com.fourroro.nolleogasil_backend.dto.users.WishDto;
import com.fourroro.nolleogasil_backend.entity.users.Wish;
import com.fourroro.nolleogasil_backend.service.place.PlaceService;
import com.fourroro.nolleogasil_backend.service.users.WishService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/wish")
public class WishController {

    private final WishService wishService;
    private final PlaceService placeService;

    //session에 있는 usersId 가져오기
    private Long getSessionUsersId(HttpSession session) {
        UsersDto usersSession = (UsersDto) session.getAttribute("users");
        return usersSession.getUsersId();
    }

    //위시에 추가 버튼(빈 하트) 클릭시, insert wish
    @PostMapping
    public ResponseEntity<Void> createWish(@RequestBody PlaceDto placeDto, @RequestParam String category, HttpSession session) {
        try {
            int placeCat = placeService.changeToPlaceCat(category);
            if (placeCat == 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }

            Long usersId = getSessionUsersId(session);
            placeDto.setPlaceCat(placeCat);
            WishDto wishDto = WishDto.builder()
                    .usersId(usersId)
                    .place(placeDto)
                    .build();

            //이미 place table에 해당 place가 존재하는지
            if (placeService.checkPlaceColumn(placeDto.getPlaceId())) {
                wishService.insertWish(wishDto);
            } else {
                //해당 place가 DB에 존재하지 않는다면, insert place
                placeService.insertPlace(placeDto);
                wishService.insertWish(wishDto);
            }
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch(Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    //로그인한 사용자의 '내 장소'(wishList)에서 해당 장소가 있는지 확인 -> wish에 있으면 true, 없으면 false반환
    @GetMapping("/{placeId}/status")
    public ResponseEntity<Boolean> checkingWishStatus(@PathVariable Integer placeId, HttpSession session) {
        try {
            Long usersId = getSessionUsersId(session);
            boolean isWish = wishService.checkWishColumn(usersId, placeId);

            return ResponseEntity.ok(isWish);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    //로그인한 사용자의 '내 장소'(wishList) 조회
    @GetMapping("/{placeCat}")
    public ResponseEntity<List<WishDto>> getWishList(@PathVariable int placeCat, @RequestParam String sortBy,
                                                     HttpSession session) {
        try {
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

            return ResponseEntity.ok(wishDtoList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    //로그인한 사용자의 '내 장소'(wishList)에서 해당 카테고리의 장소 개수 조회
    @GetMapping("/{placeCat}/count")
    public ResponseEntity<Long> countWish(@PathVariable int placeCat, HttpSession session) {
        try {
            Long usersId = getSessionUsersId(session);
            Long count;
            if (placeCat == 0) {  //0: 전체
                count = wishService.countWish(usersId);
            } else {  //1: 맛집, 2: 카페, 3: 관광지, 4: 숙소
                count = wishService.countWishByPlaceCat(usersId, placeCat);
            }
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    //로그인한 사용자의 '내 장소'(wishList)에서 해당 장소 삭제
    @DeleteMapping("/{wishId}")
    public ResponseEntity<Void> deleteWish(@PathVariable Long wishId, @RequestParam(required = false) Integer placeId,
                                           HttpSession session) {
        try {
            if (wishId == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }

            Long usersId = getSessionUsersId(session);

            //wishId가 0이라면, 지도에서 넘어온 요청 -> 삭제하고자 하는 wishId를 조회한 후, 삭제
            if (wishId == 0) {
                Wish wish = wishService.getWishByUsersIdAndPlaceId(usersId, placeId);
                wishService.deleteWish(wish.getWishId());
            } else {
                //wishId가 0이 아니라면 내 장소에서 넘어온 요청
                wishService.deleteWish(wishId);
            }
            return ResponseEntity.noContent().build();
        }  catch(Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}