package com.fourroro.nolleogasil_backend.service.travelpath;

import com.fourroro.nolleogasil_backend.dto.travelpath.TravelDateDto;
import com.fourroro.nolleogasil_backend.entity.travelpath.Recommendation;
import com.fourroro.nolleogasil_backend.entity.travelpath.TravelDate;
import com.fourroro.nolleogasil_backend.repository.travelpath.TravelDateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TravelDateServiceImpl implements TravelDateService{

    private final TravelDateRepository travelDateRepository;

    /** travelDate 추가 */
    @Override
    @Transactional
    public void insertTravelDate (Recommendation recommendation, TravelDateDto travelDateDto){

        TravelDate travelDate = TravelDate.changeToEntity(travelDateDto, recommendation);

        try {
            travelDateRepository.save(travelDate);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
