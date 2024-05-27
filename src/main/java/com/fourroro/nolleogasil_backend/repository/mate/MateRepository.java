package com.fourroro.nolleogasil_backend.repository.mate;

import com.fourroro.nolleogasil_backend.entity.mate.Mate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MateRepository extends JpaRepository<Mate, Long> {
    //save()(=persist())

    //모든 Mate 공고 글 조회(활성화된 글만) -> 마감기한이 가까운 순으로
    @Query("select m from Mate m JOIN m.place p " +
            "where m.display = :display order by m.eatDate ASC, m.eatTime ASC")
    public List<Mate> findByDisplay(@Param("display")int display);

    //해당 place에 관한 Mate 공고 글 조회(활성화된 글만) -> 마감기한이 가까운 순으로
    @Query("select m from Mate m JOIN m.place p " +
            "where m.display = :display AND p.placeId = :placeId order by m.eatDate ASC, m.eatTime ASC")
    public List<Mate> findByDisplayAndPlaceId(@Param("display")int display, @Param("placeId")Integer placeId);

    //해당 placeCat에 관한 Mate 골고 글 조회(활성화된 글만) -> 마감기한이 가까운 순으로
    @Query("select m from Mate m JOIN m.place p " +
            "where m.display = :display AND p.placeCat = :placeCat order by m.eatDate ASC, m.eatTime ASC")
    public List<Mate> findByDisplayAndPlaceCat(@Param("display")int display, @Param("placeCat")Integer placeCat);

    //로그인한 사용자가 개설한 Mate 공고 글 조회(작성한 순 -> 최신순)
    public List<Mate> findByUsersUsersIdOrderByMateIdDesc(Long usersId);

    //mate 개수(활성화 된 글만 count)
    public Long countByDisplay(int display);

    //해당 place에 관한 mate 개수(활성화 된 글만 count)
    public Long countByDisplayAndPlacePlaceId(int display, Integer placeId);

    public Mate findByChatRoomChatroomId(Long chatRoomId);

    //display 변경 -> mate 공고 글 비활성화
    //save()사용
}