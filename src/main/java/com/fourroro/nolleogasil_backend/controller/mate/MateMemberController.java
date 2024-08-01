package com.fourroro.nolleogasil_backend.controller.mate;

import com.fourroro.nolleogasil_backend.dto.mate.MateMemberDto;
import com.fourroro.nolleogasil_backend.dto.users.UsersDto;
import com.fourroro.nolleogasil_backend.entity.mate.MateMember;
import com.fourroro.nolleogasil_backend.service.mate.MateMemberService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mateMember")
public class MateMemberController {

    private final MateMemberService mateMemberService;

    //session에 있는 usersId 가져오기
    private Long getSessionUsersId(HttpSession session) {
        UsersDto usersSession = (UsersDto) session.getAttribute("users");
        return usersSession.getUsersId();
    }

    //첫 입장인지......
    @GetMapping("/checkedMember")
    public ResponseEntity<String> checkFirstEnterRoom(@RequestParam String chatroomId, HttpSession session) {

        Long chatRoomId = Long.parseLong(chatroomId);
        Long userId = getSessionUsersId(session);


        boolean isFirst = mateMemberService.checkFirstEnterRoom(chatRoomId,userId);

        if(isFirst) {
            return ResponseEntity.ok("firstEnter");
        } else {
            return ResponseEntity.ok("noFirstEnter");
        }
    }

    //로그인한 사용자가 참여했던 mate 이력 조회(본인이 개설한 mate 포함)
    @GetMapping("/history")
    public ResponseEntity<List<MateMemberDto>> getMateHistoryList(HttpSession session) {
        try {
            Long userId = getSessionUsersId(session);
            List<MateMemberDto> mateHistoryList = mateMemberService.getMateHistoryList(userId);
            return ResponseEntity.ok(mateHistoryList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    //해당 mate의 mateMember 조회
    @GetMapping("/{mateId}")
    public  ResponseEntity<List<MateMemberDto>> getMateMemberList(@PathVariable Long mateId) {
        try {
            if (mateId == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }

            List<MateMemberDto> mateMemberList = mateMemberService.getMateMemberList(mateId);
            return ResponseEntity.ok(mateMemberList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    //해당 mate의 mateMember 조회(단, 로그인한 사용자는 제외) -> 온도 부여할 때 사용
    @GetMapping("/{mateId}/excluding-me")
    public ResponseEntity<List<MateMemberDto>> getMateMemberListExcludingMe(@PathVariable Long mateId, HttpSession session) {
        try {
            if (mateId == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }

            Long usersId = getSessionUsersId(session);
            List<MateMemberDto> mateMemberList = mateMemberService.getMateMemberListExcludingMe(mateId, usersId);
            return ResponseEntity.ok(mateMemberList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    //해당 mate의 mateMember 수
    @GetMapping("/{mateId}/count")
    public ResponseEntity<Long> countMateMember(@PathVariable Long mateId) {
        try {
            if (mateId == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }

            Long count = mateMemberService.countMateMember(mateId);
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    //로그인한 사용자의 mateMember 정보 조회(usersId, mateId 이용) -> isGiven(온도부여 여부) 조회할 때 사용
    @GetMapping("/{mateId}/my-info")
    public ResponseEntity<MateMemberDto> getMateMemberByUsersIdAndMateId(@PathVariable Long mateId, HttpSession session) {
        try {
            if (mateId == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }

            Long usersId = getSessionUsersId(session);
            MateMember mateMember = mateMemberService.getMateMemberByUsersIdAndMateId(usersId, mateId);

            if (mateMember == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            return ResponseEntity.ok(MateMemberDto.changeToDto(mateMember));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    //동일 mate의 mateMember들에게 mateTemp부여(본인 제외)
    @PatchMapping("/{mateId}/temp")
    public ResponseEntity<Void> setMemberMateTemp(@PathVariable Long mateId, @RequestBody Map<Long, Float> memberMateTempMap,
                                                  HttpSession session) {
        try {
            if (mateId == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }

            Long usersId = getSessionUsersId(session);
            for (Map.Entry<Long, Float> entry : memberMateTempMap.entrySet()) {
                Long memberId = entry.getKey();
                Float mateTemp = entry.getValue();

                MateMember mateMember = mateMemberService.getMateMember(memberId);
                UsersDto member = UsersDto.changeToDto(mateMember.getUsers());
                mateMemberService.setMemberMateTemp(member, mateTemp, usersId, mateId);
            }
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    //해당 mateMemeber 삭제
    @DeleteMapping("/{matememberId}")
    public ResponseEntity<Void> deleteMateMember(@PathVariable Long matememberId) {
        try {
            if (matememberId == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }

            mateMemberService.deleteMateMember(matememberId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}