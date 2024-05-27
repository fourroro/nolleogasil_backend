package com.fourroro.nolleogasil_backend.service.travelpath;

import com.fourroro.nolleogasil_backend.dto.travelpath.TravelInfoDto;
import com.fourroro.nolleogasil_backend.entity.travelpath.Recommendation;
import com.fourroro.nolleogasil_backend.entity.travelpath.TravelInfo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TravelInfoService {
    public void insertTravelInfo (Recommendation recommendation, TravelInfoDto travelInfoDto);

    public List<TravelInfo> getTravelInfoById(Long recommendationId);

    public void updateTravelInfo(List<TravelInfoDto> travelInfoDtos, Recommendation recommendation);
}
