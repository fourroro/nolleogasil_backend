package com.fourroro.nolleogasil_backend.service.mate;

import com.fourroro.nolleogasil_backend.dto.mate.MateDto;
import com.fourroro.nolleogasil_backend.dto.mate.MateFormDto;
import com.fourroro.nolleogasil_backend.dto.place.PlaceDto;
import com.fourroro.nolleogasil_backend.entity.mate.Mate;
import com.fourroro.nolleogasil_backend.entity.place.Place;
import com.fourroro.nolleogasil_backend.entity.users.Users;
import com.fourroro.nolleogasil_backend.global.exception.GlobalException;
import com.fourroro.nolleogasil_backend.global.exception.ResultCode;
import com.fourroro.nolleogasil_backend.repository.mate.MateRepository;
import com.fourroro.nolleogasil_backend.service.place.PlaceService;
import com.fourroro.nolleogasil_backend.service.users.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class MateServiceImpl implements MateService {

    private final MateRepository mateRepository;
    private final PlaceService placeService;
    private final UsersService usersService;

    @Transactional
    @Override
    public MateDto insertMate(MateFormDto requestMateDto, PlaceDto placeDto, Long usersId) {
        System.out.println(usersId);
        //메이트 작성자 조회
        Users users = usersService.findByUsersId(usersId);
        //플레이스 저장
        Place place = placeService.insertPlace(placeDto);
        Mate mate = Mate.toEntity(requestMateDto,users,place);
        //메이트 공고글 작성
        Mate savedMate = mateRepository.save(mate);
        MateDto mateDto = MateDto.changeToDto(savedMate);
        return mateDto;
    }

    @Override
    public List<MateDto> getMateList(Integer placeId, int placeCat) {
        List<Mate> mateList;
        if (placeId == 0) {  //전체 장소에 관한 공고 조회
            if (placeCat == 0) {  //전체 목록 조회
                mateList = mateRepository.findByDisplay(1);
            } else {  //식당(1) or 카페(2)에 관한 공고만 조회
                mateList = mateRepository.findByDisplayAndPlaceCat(1, placeCat);
            }
        } else {  //해당 장소에 관한 공고만 조회
            mateList = mateRepository.findByDisplayAndPlaceId(1, placeId);
        }
        return compareDateTime(mateList);
    }

    @Override
    public List<MateDto> getMateListByUsersId(Long usersId) {
        List<Mate> mateList = mateRepository.findByUsersUsersIdOrderByMateIdDesc(usersId);

        List<MateDto> mateDtoList = new ArrayList<>();
        for (Mate mate : mateList) {
            MateDto mateDto = MateDto.changeToDto(mate);
            mateDtoList.add(mateDto);
        }
        return mateDtoList;
    }

    @Override
    public List<MateDto> getMateListOrderByDistance(Integer placeId, int placeCat, double currentLat, double currentLng) {
        List<MateDto> mateDtoList = getMateList(placeId, placeCat);

        //람다식(sort, comparator 사용)을 이용해 mateDtoList를 거리순으로 정렬
        mateDtoList.sort((mate1, mate2) -> {
            double distance1 = placeService.calculateDistance(currentLat, currentLng,
                    mate1.getPlace().getPlaceLat(), mate1.getPlace().getPlaceLng());

            double distance2 = placeService.calculateDistance(currentLat, currentLng,
                    mate2.getPlace().getPlaceLat(), mate2.getPlace().getPlaceLng());

            mate1.getPlace().setDistance(distance1);
            mate2.getPlace().setDistance(distance2);
            return Double.compare(distance1, distance2);
        });

        return mateDtoList;
    }

    @Override
    public Mate getMate(Long mateId) {
        return mateRepository.findById(mateId)
                .orElseThrow(() -> new GlobalException(ResultCode.NOT_FOUND_MATE));
    }

    @Override
    public MateDto getMateBychatroom(Long chatroomId) {
        Mate mate = mateRepository.findByChatRoomChatroomId(chatroomId);
        MateDto mateDto = MateDto.changeToDto(mate);
        return mateDto;
    }

    @Override
    public PlaceDto getPlaceDto(Long chatRoomId) {
        Mate mate = mateRepository.findByChatRoomChatroomId(chatRoomId);
        PlaceDto placeDto = PlaceDto.changeToDto(mate.getPlace());

        return placeDto;
    }

    @Override
    public Long countMate(Integer placeId) {
        if (placeId == 0) {
            return mateRepository.countByDisplay(1);
        } else {
            return mateRepository.countByDisplayAndPlacePlaceId(1, placeId);
        }
    }

    @Transactional
    @Override
    public void deleteMate(Long mateId) {
        mateRepository.deleteById(mateId);
    }

    //각 mate의 eatDate, eatTime과 현재날짜와 시간을 비교해 display 변경
    public List<MateDto> compareDateTime (List<Mate> mateList) {
        LocalDateTime now = LocalDateTime.now();
        List<MateDto> mateDtoList = new ArrayList<>();

        for (Mate mate : mateList) {
            LocalTime mateTime = LocalTime.parse(mate.getEatTime(), DateTimeFormatter.ofPattern("ah:mm",  Locale.ENGLISH));
            LocalDateTime mateDateTime = mate.getEatDate().atTime(mateTime);

            //now가 mateDateTime보다 이후라면 display를 0으로 변경
            if (now.isAfter(mateDateTime)) {
                updateDisplayStatus(mate);
            } else {
                MateDto mateDto = MateDto.changeToDto(mate);
                mateDtoList.add(mateDto);
            }
        }
        return mateDtoList;
    }

    //display 0으로 변경
    public void updateDisplayStatus(Mate m) {
        MateDto mateDto = MateDto.changeToDto(m);
        mateDto.setDisplay(0);  //mate 공고 글 비활성화

        Users users = usersService.findByUsersId(mateDto.getUsersId());
        Mate mate = Mate.changeToEntity(mateDto, users);
        mateRepository.save(mate);
    }
}
