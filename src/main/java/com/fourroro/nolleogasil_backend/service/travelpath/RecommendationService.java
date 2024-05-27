package com.fourroro.nolleogasil_backend.service.travelpath;

import com.fourroro.nolleogasil_backend.dto.travelpath.RecommendationDto;
import com.fourroro.nolleogasil_backend.entity.travelpath.Recommendation;
import com.fourroro.nolleogasil_backend.entity.travelpath.TravelPath;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface RecommendationService {

    public Recommendation insertRecommendation (TravelPath travelPath, RecommendationDto recommendationDto);

    public Optional<Recommendation> getRecommendation (Long recommendationId);

    public List<String> getTravelDateList (Long recommendationId);

    public List<String> getTravelInfoList (Long recommendationId);
}
