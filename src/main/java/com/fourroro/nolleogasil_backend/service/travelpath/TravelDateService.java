package com.fourroro.nolleogasil_backend.service.travelpath;

import com.fourroro.nolleogasil_backend.dto.travelpath.TravelDateDto;
import com.fourroro.nolleogasil_backend.entity.travelpath.Recommendation;
import org.springframework.stereotype.Service;

@Service
public interface TravelDateService {
    public void insertTravelDate (Recommendation recommendation, TravelDateDto travelDateDto);
}
