package com.fourroro.nolleogasil_backend.repository.travelpath;

import com.fourroro.nolleogasil_backend.entity.travelpath.Recommendation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecommendationRepository extends JpaRepository<Recommendation, Long> {


    Optional<Recommendation> findById(@Param("recommendationId") Long recommendationId);

    //Orderby ASC (default)
    @Query("SELECT td.dates FROM TravelDate td WHERE td.recommendation.recommendationId = :recommendationId ORDER BY td.traveldateId ASC")
    List<String> findDatesByRecommendationRecommendationId(@Param("recommendationId") Long recommendationId);

    @Query("SELECT ti.infos FROM TravelInfo ti WHERE ti.recommendation.recommendationId = :recommendationId ORDER BY ti.travelinfoId ASC")
    List<String> findInfosByRecommendationRecommendationId(@Param("recommendationId") Long recommendationId);


}
