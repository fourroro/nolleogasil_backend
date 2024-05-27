package com.fourroro.nolleogasil_backend.repository.users;

import com.fourroro.nolleogasil_backend.entity.users.Wish;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WishRepository extends JpaRepository<Wish, Long> {
    //save()(=persist())
    //delete()

    //-----wishList조회-----
    //wish목록 조회
    public List<Wish> findByUsersUsersId(Long usersId);

    //wish목록 정렬(오름차순)
    public List<Wish> findByUsersUsersIdOrderByWishIdAsc(Long usersId);

    //wish목록 정렬(내림차순)
    public List<Wish> findByUsersUsersIdOrderByWishIdDesc(Long usersId);

    //placeCat에 따른 wish 조회
    public List<Wish> findByUsersUsersIdAndPlacePlaceCat(Long usersId, int placeCat);

    //placeCat에 따른 wish목록 정렬(오름차순)
    public List<Wish> findByUsersUsersIdAndPlacePlaceCatOrderByWishIdAsc(Long usersId, int placeCat);

    //placeCat에 따른 wish목록 정렬(내림차순)
    public List<Wish> findByUsersUsersIdAndPlacePlaceCatOrderByWishIdDesc(Long usersId, int placeCat);
    //---------------------

    //wish 1개 조회
    public Wish findByUsersUsersIdAndPlacePlaceId(Long usersId, Integer placeId);

    //wish column 유무 확인
    public Boolean existsByUsersUsersIdAndPlacePlaceId(Long usersId, Integer placeId);

    //저장한 총 wish 개수
    public Long countByUsersUsersId(Long usersId);

    //placeCat에 따른 저장한 wish 개수
    public Long countByUsersUsersIdAndPlacePlaceCat(Long usersId, int placeCat);
}
