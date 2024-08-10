package com.fourroro.nolleogasil_backend.service.travelpath;

import com.fourroro.nolleogasil_backend.dto.travelpath.TravelInfoDto;
import com.fourroro.nolleogasil_backend.entity.travelpath.Recommendation;
import com.fourroro.nolleogasil_backend.entity.travelpath.TravelInfo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TravelInfoService {

    /** travelInfo 추가 */
    public void insertTravelInfo (Recommendation recommendation, TravelInfoDto travelInfoDto);

    /** travelInfo 조회 */
    public List<TravelInfo> getTravelInfoById(Long recommendationId);

    /** travelInfo 갱신 */
    public void updateTravelInfo(List<TravelInfoDto> travelInfoDtos, Recommendation recommendation);
}
