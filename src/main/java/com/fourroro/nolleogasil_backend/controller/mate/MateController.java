package com.fourroro.nolleogasil_backend.controller.mate;

import com.fourroro.nolleogasil_backend.dto.chat.ChatRoomAndPlaceDto;
import com.fourroro.nolleogasil_backend.dto.chat.ChatRoomDto;
import com.fourroro.nolleogasil_backend.dto.mate.MateDto;
import com.fourroro.nolleogasil_backend.dto.mate.MateMemberDto;
import com.fourroro.nolleogasil_backend.dto.mate.RequestMateDto;
import com.fourroro.nolleogasil_backend.dto.users.UsersDto;
import com.fourroro.nolleogasil_backend.entity.mate.Mate;
import com.fourroro.nolleogasil_backend.service.chat.ChatRoomService;
import com.fourroro.nolleogasil_backend.service.mate.MateMemberService;
import com.fourroro.nolleogasil_backend.service.mate.MateService;
import com.fourroro.nolleogasil_backend.service.place.PlaceService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mate")
public class MateController {

    private final MateService mateService;
    private final PlaceService placeService;
    private final MateMemberService mateMemberService;
    private final ChatRoomService chatRoomService;

    //session에 있는 usersId 가져오기
    private Long getSessionUsersId(HttpSession session) {
        UsersDto usersDto = (UsersDto) session.getAttribute("users");
        return usersDto.getUsersId();
    }

    @PostMapping("/mateForm")
    public ResponseEntity<ChatRoomAndPlaceDto> creatMateForm(@RequestBody RequestMateDto requestMateDto,
                                                             @RequestParam(name = "category") String category,
                                                             HttpSession session) {

        ChatRoomDto chatRoomDto = null;
        ChatRoomAndPlaceDto chatRoomAndPlaceDto = null;
        try {
            // 장소 카테고리 변경 후 mateDto에 저장

            int placeCat = requestMateDto.getPlaceDto().getPlaceCat();
            if (placeCat == 0) {
                placeCat = placeService.changeToPlaceCat(category);
                requestMateDto.getPlaceDto().setPlaceCat(placeCat);

            }

            // 사용자 정보 얻어오기
            Long userId = getSessionUsersId(session);
            //메이트 공고글 생성
            MateDto mateDto = mateService.insertMate(requestMateDto.getMateFormDto(),requestMateDto.getPlaceDto(),userId);
            //메이트 공고글에 해당하는 챗룸 생성
            chatRoomDto = chatRoomService.createRoom(mateDto,userId);
            //해당 사용자 메이트 멤버로 등록.
            //memberDto 반환하는 로직 MateMemberService에 메서드로 만듬!
            MateMemberDto mateMemberDto = mateMemberService.creatMateMemberDto(mateDto, chatRoomDto.getChatroomId(), userId);
            mateMemberService.insertMateMember(mateMemberDto);
            chatRoomAndPlaceDto = ChatRoomAndPlaceDto.createDto(chatRoomDto,requestMateDto.getPlaceDto(),mateDto);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
        return new ResponseEntity<>(chatRoomAndPlaceDto, HttpStatus.OK);
    }

    //mate 공고 글 조회
    @GetMapping("/mateList")
    public List<MateDto> getMateList(Integer placeId, int placeCat, double currentLat, double currentLng, String sorted) {
        if (sorted.equals("날짜순")) {
            return mateService.getMateList(placeId, placeCat);
        } else {  //거리순
            return mateService.getMateListOrderByDistance(placeId, placeCat, currentLat, currentLng);
        }
    }

    //로그인한 사용자가 개설한 mate 공고 글 조회
    @GetMapping("/mateListByUsersId")
    public List<MateDto> getMateListByUsersId(HttpSession session) {
        Long usersId = getSessionUsersId(session);
        return mateService.getMateListByUsersId(usersId);
    }

    //하나의 mate 공고 글 조회
    @GetMapping("/")
    public MateDto getMate(@RequestParam Long mateId) {
        if (mateId == null) {
            return (MateDto)Collections.emptyList();
        }
        Mate mate = mateService.getMate(mateId);
        return MateDto.changeToDto(mate);
    }

    //mate 공고 글 개수 -> 아직 사용 안함
    @GetMapping("/countMate")
    public Long countMate(Integer placeId) {
        if (placeId == null) {
            return mateService.countMate(0);
        } else {
            return mateService.countMate(placeId);
        }
    }

    //mate 삭제
    @DeleteMapping("/")
    public String deleteMate(Long mateId) {
        try {
            mateService.deleteMate(mateId);
            return "successful";
        }  catch(Exception e) {
            e.printStackTrace();
            return "failed";
        }
    }
}