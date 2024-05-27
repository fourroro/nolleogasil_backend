package com.fourroro.nolleogasil_backend.service.travelpath;

import com.fourroro.nolleogasil_backend.dto.travelpath.TravelPathDto;
import com.fourroro.nolleogasil_backend.entity.travelpath.TravelPath;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TravelPathService {

    // travelPath 추가
    public TravelPath insertTravelPath (TravelPathDto travelPathDto);

    // travelPath 내림차순 조회 (keyword 조인)
    public List<TravelPath> getTravelPathListDesc(@Param("usersId") Long usersId);

    // travelPath 오름차순 조회 (keyword 조인)
    public List<TravelPath> getTravelPathListAsc(@Param("usersId") Long usersId);

    // travelPath 지역순 조회 (keyword 조인)
    public List<TravelPath> getTravelPathListByArrival(@Param("usersId") Long usersId);

    // travelpathId로 TravelPath, Keyword, Recommendation 조회
    public TravelPath getTravelPathById(@Param("travelpathId") Long travelPathId);

    //user가 저장한 TravelPath의 총 개수
    public Long countTravelPath(Long usersId);

    // travelPath 삭제
    public void deleteTravelPathById(@Param("travelpathId") Long travelPathId);
}
