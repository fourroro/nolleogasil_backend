package com.fourroro.nolleogasil_backend.service.mate;

import com.fourroro.nolleogasil_backend.dto.mate.MateDto;
import com.fourroro.nolleogasil_backend.dto.mate.MateFormDto;
import com.fourroro.nolleogasil_backend.dto.place.PlaceDto;
import com.fourroro.nolleogasil_backend.entity.mate.Mate;

import java.util.List;

public interface MateService {

    //mate공고 추가
    public MateDto insertMate(MateFormDto requestMateDto, PlaceDto placeDto, Long usersId);

    //mate 공고 글 조회(날짜순)
    public List<MateDto> getMateList(Integer placeId, int placeCat);

    //로그인한 사용자가 개설한 mate 공고 글 조회
    public List<MateDto> getMateListByUsersId(Long usersId);

    //mate 공고 글 조회(거리순)
    public List<MateDto> getMateListOrderByDistance(Integer placeId, int placeCat, double currentLat, double currentLng);

    //하나의 mate 공고 글 조회
    public Mate getMate(Long mateId);

    public MateDto getMateBychatroom(Long chatroomId);

    public PlaceDto getPlaceDto(Long chatRoomId);

    //mate 공고 글 개수
    public Long countMate(Integer placeId);

    //Mate공고 글 삭제
    public void deleteMate(Long mateId);


}