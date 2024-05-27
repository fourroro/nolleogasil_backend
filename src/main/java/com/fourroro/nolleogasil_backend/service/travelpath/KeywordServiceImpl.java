package com.fourroro.nolleogasil_backend.service.travelpath;

import com.fourroro.nolleogasil_backend.dto.travelpath.KeywordDto;
import com.fourroro.nolleogasil_backend.entity.travelpath.Keyword;
import com.fourroro.nolleogasil_backend.entity.travelpath.TravelPath;
import com.fourroro.nolleogasil_backend.repository.travelpath.KeywordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class KeywordServiceImpl implements KeywordService{

    private final KeywordRepository keywordRepository;

    @Override
    @Transactional
    public void insertKeyword (TravelPath travelPath, KeywordDto keywordDto){

        Keyword keyword = Keyword.changeToEntity(keywordDto, travelPath);

        try {
            keywordRepository.save(keyword);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
