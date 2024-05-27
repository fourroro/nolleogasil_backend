package com.fourroro.nolleogasil_backend.service.users;

import com.fourroro.nolleogasil_backend.dto.users.WishDto;
import com.fourroro.nolleogasil_backend.entity.place.Place;
import com.fourroro.nolleogasil_backend.entity.users.Users;
import com.fourroro.nolleogasil_backend.entity.users.Wish;
import com.fourroro.nolleogasil_backend.repository.users.WishRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WishServiceImpl implements WishService {

    private final WishRepository wishRepository;
    private final UsersService usersService;

    @Transactional
    @Override
    public void insertWish(WishDto wishDto) {
        Users users = usersService.findByUsersId(wishDto.getUsersId());
        Place place = Place.changeToEntity(wishDto.getPlace());

        Wish wish = Wish.changeToEntity(users, place);
        wishRepository.save(wish);
    }

    @Override
    public List<Wish> getWishList(Long usersId, int placeCat) {
        if (placeCat == 0) {
            return wishRepository.findByUsersUsersId(usersId);
        } else {
            return wishRepository.findByUsersUsersIdAndPlacePlaceCat(usersId, placeCat);
        }
    }

    @Override
    public List<Wish> getSortedWishList(Long usersId, int placeCat, String sortBy) {
        if (placeCat == 0) { //전체 장소 정렬
            if (sortBy.equals("최신순")) {
                return wishRepository.findByUsersUsersIdOrderByWishIdDesc(usersId);
            } else {
                return wishRepository.findByUsersUsersIdOrderByWishIdAsc(usersId);
            }
        } else { //장소별 정렬
            if (sortBy.equals("최신순")) {
                return wishRepository.findByUsersUsersIdAndPlacePlaceCatOrderByWishIdDesc(usersId, placeCat);
            } else {
                return wishRepository.findByUsersUsersIdAndPlacePlaceCatOrderByWishIdAsc(usersId, placeCat);
            }
        }
    }

    @Override
    public Wish getWishByUsersIdAndPlaceId(Long usersId, Integer placeId) {
        return wishRepository.findByUsersUsersIdAndPlacePlaceId(usersId, placeId);
    }

    @Override
    public Boolean checkWishColumn(Long usersId, Integer placeId) {
        return wishRepository.existsByUsersUsersIdAndPlacePlaceId(usersId, placeId);
    }

    @Override
    public Long countWish(Long usersId) {
        return wishRepository.countByUsersUsersId(usersId);
    }

    @Override
    public Long countWishByPlaceCat(Long usersId, int placeCat) {
//        return wishRepository.countByUsersIdAndPlaceCat(usersId, placeCat);
        return wishRepository.countByUsersUsersIdAndPlacePlaceCat(usersId, placeCat);
    }

    @Transactional
    @Override
    public void deleteWish(Long wishId) {
        wishRepository.deleteById(wishId);
    }

}
