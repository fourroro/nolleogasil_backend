package com.fourroro.nolleogasil_backend.repository.travelpath;

import com.fourroro.nolleogasil_backend.entity.travelpath.TravelInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TravelInfoRepository extends JpaRepository<TravelInfo, Long> {

    @Query("SELECT ti FROM TravelInfo ti WHERE ti.recommendation.recommendationId = :recommendationId ORDER BY ti.travelinfoId ASC")
    List<TravelInfo> findByRecommendationRecommendationId(@Param("recommendationId") Long recommendationId);

}
