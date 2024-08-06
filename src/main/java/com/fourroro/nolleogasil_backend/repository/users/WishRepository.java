/**
 * 이 클래스는 위시(내 장소)를 DB에 저장 및 DB에서 조회, 삭제하기 위한 Repository입니다.
 * @author 박초은
 * @since 2024-01-05
 */
package com.fourroro.nolleogasil_backend.repository.users;

import com.fourroro.nolleogasil_backend.entity.users.Wish;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WishRepository extends JpaRepository<Wish, Long> {
    //save()(=persist())
    //deleteById()

    //-----wishList조회-----
    //wish 목록 조회
    public List<Wish> findByUsersUsersId(Long usersId);

    //해당 placeCat의 wish 목록 조회
    public List<Wish> findByUsersUsersIdAndPlacePlaceCat(Long usersId, int placeCat);

    //wish 목록 조회(오름차순)
    public List<Wish> findByUsersUsersIdOrderByWishIdAsc(Long usersId);

    //wish 목록 조회(내림차순)
    public List<Wish> findByUsersUsersIdOrderByWishIdDesc(Long usersId);

    //해당 placeCat의 wish 목록 정렬(오름차순)
    public List<Wish> findByUsersUsersIdAndPlacePlaceCatOrderByWishIdAsc(Long usersId, int placeCat);

    //해당 placeCat의 wish 목록 정렬(내림차순)
    public List<Wish> findByUsersUsersIdAndPlacePlaceCatOrderByWishIdDesc(Long usersId, int placeCat);
    //---------------------

    //wish 1개 조회
    public Wish findByUsersUsersIdAndPlacePlaceId(Long usersId, Integer placeId);

    //wish 유무 확인
    public Boolean existsByUsersUsersIdAndPlacePlaceId(Long usersId, Integer placeId);

    //저장된 총 wish 개수 조회
    public Long countByUsersUsersId(Long usersId);

    //저장된 해당 placeCate의 wish 개수 조회
    public Long countByUsersUsersIdAndPlacePlaceCat(Long usersId, int placeCat);

}