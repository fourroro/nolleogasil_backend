package com.fourroro.nolleogasil_backend.service.travelpath;

import com.fourroro.nolleogasil_backend.dto.travelpath.KeywordDto;
import com.fourroro.nolleogasil_backend.entity.travelpath.TravelPath;
import org.springframework.stereotype.Service;

@Service
public interface KeywordService {

    public void insertKeyword (TravelPath travelPath, KeywordDto keywordDto);
}
