package com.fourroro.nolleogasil_backend.service.travelpath;

import com.fourroro.nolleogasil_backend.dto.travelpath.TravelInfoDto;
import com.fourroro.nolleogasil_backend.entity.travelpath.Recommendation;
import com.fourroro.nolleogasil_backend.entity.travelpath.TravelInfo;
import com.fourroro.nolleogasil_backend.repository.travelpath.TravelInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TravelInfoServiceImpl implements TravelInfoService{

    private final TravelInfoRepository travelInfoRepository;

    @Override
    @Transactional
    public void insertTravelInfo (Recommendation recommendation, TravelInfoDto travelInfoDto) {

        TravelInfo travelInfo = TravelInfo.changeToEntity(travelInfoDto, recommendation);

        try {
            travelInfoRepository.save(travelInfo);
        }catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    @Transactional
    public List<TravelInfo> getTravelInfoById(Long recommendationId) {

        List<TravelInfo> travelInfos = new ArrayList<>();
        try {
            travelInfos = travelInfoRepository.findByRecommendationRecommendationId(recommendationId);
        }catch(Exception e) {
            e.printStackTrace();
        }
        return travelInfos;
    }

    @Override
    @Transactional
    public void updateTravelInfo(List<TravelInfoDto> travelInfoDtos,  Recommendation recommendation){
        List<TravelInfo> travelInfos = new ArrayList<>();
        try {
            for (int i = 0; i < travelInfoDtos.size(); i++) {
                travelInfos.add(TravelInfo.changeToEntity(travelInfoDtos.get(i), recommendation));
            }
            travelInfoRepository.saveAllAndFlush(travelInfos);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
