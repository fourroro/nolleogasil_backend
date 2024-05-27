package com.fourroro.nolleogasil_backend.service.users;

import com.fourroro.nolleogasil_backend.dto.users.WishDto;
import com.fourroro.nolleogasil_backend.entity.users.Wish;

import java.util.List;

public interface WishService {
    //wish 추가
    public void insertWish(WishDto wishDto);

    //wish목록 조회
    public List<Wish> getWishList(Long usersId, int placeCat);

    //wishList 정렬
    public List<Wish> getSortedWishList(Long usersId, int placeCat, String sortBy);

    //wishId찾기
    public Wish getWishByUsersIdAndPlaceId(Long usersId, Integer placeId);

    //wish column 유무 확인
    public Boolean checkWishColumn(Long usersId, Integer placeId);

    //저장한 총 wish개수 조회
    public Long countWish(Long usersId);

    //placeCate에 따른 저장한 wish개수 조회
    public Long countWishByPlaceCat(Long usersId, int placeCat);

    //wish 삭제
    public void deleteWish(Long wishId);
}
