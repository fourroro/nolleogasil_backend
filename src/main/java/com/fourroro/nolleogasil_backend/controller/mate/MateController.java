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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        UsersDto usersSession = (UsersDto) session.getAttribute("users");
        return usersSession.getUsersId();
    }

    @PostMapping("/mateForm")
    public ResponseEntity<ChatRoomAndPlaceDto> creatMateForm(@RequestBody RequestMateDto requestMateDto,
                                                             @RequestParam(name = "category") String category, HttpSession session) {

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

    //전체 mate 공고 글 조회
    @GetMapping
    public ResponseEntity<List<MateDto>> getMateList(@RequestParam Integer placeId,
                                                     @RequestParam Integer placeCat,
                                                     @RequestParam Double currentLat,
                                                     @RequestParam Double currentLng,
                                                     @RequestParam(defaultValue = "날짜순") String sorted) {
        try {
            List<MateDto> mateList;
            if (sorted.equals("날짜순")) {
                mateList = mateService.getMateList(placeId, placeCat);
            } else {
                mateList = mateService.getMateListOrderByDistance(placeId, placeCat, currentLat, currentLng);
            }
            return ResponseEntity.ok(mateList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    //1개의 mate 공고 글 조회
    @GetMapping("/{mateId}")
    public ResponseEntity<MateDto> getMate(@PathVariable Long mateId) {
        try {
            if (mateId == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }

            Mate mate = mateService.getMate(mateId);
            if (mate == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            return ResponseEntity.ok(MateDto.changeToDto(mate));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    //로그인한 사용자가 개설한 mate 공고 글 조회
    @GetMapping("/my-create")
    public ResponseEntity<List<MateDto>> getMateListByUsersId(HttpSession session) {
        try {
            Long usersId = getSessionUsersId(session);
            List<MateDto> mateList = mateService.getMateListByUsersId(usersId);
            return ResponseEntity.ok(mateList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    //해당 mate 삭제 -> frontend에서 사용안함
    @DeleteMapping("/{mateId}")
    public ResponseEntity<Void> deleteMate(@PathVariable Long mateId) {
        try {
            if (mateId == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }

            mateService.deleteMate(mateId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}