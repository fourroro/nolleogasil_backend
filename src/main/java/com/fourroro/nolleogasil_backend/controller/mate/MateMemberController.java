/**
 * 맛집메이트의 멤버 관리를 위한 Controller입니다.
 * @author 박초은
 * @since 2024-01-10
 */
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

    /**
     * session에 저장된 UsersDto의 사용자 ID를 가져오는 함수
     *
     * @param session //현재 사용자의 세션 객체
     * @return 현재 세션에 저장된 사용자 ID
     */
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

    /**
     * 로그인한 사용자가 참여했던 맛집메이트 목록 조회
     * (본인이 개설한 맛집메이트도 포함)
     *
     * @param session 현재 사용자의 세션 객체
     * @return 조회된 맛집메이트 목록을 포함한 HTTP 상태 코드가 200인 ResponseEntity 객체,
     *         서버 오류 발생 시 HTTP 상태 코드가 500인 ResponseEntity 객체
     */
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

    /**
     * 맛집메이트의 멤버 목록 조회
     *
     * @param mateId 멤버 목록을 조회할 맛집메이트 ID
     * @return 조회된 멤버 목록을 포함한 HTTP 상태 코드가 200인 ResponseEntity 객체,
     *         HTTP 상태 코드가 400(잘못된 요청 시), 500(서버 오류 발생 시)인 ResponseEntity 객체
     */
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

    /**
     * 맛집메이트의 멤버 목록 조회 (단, 로그인한 사용자는 제외)
     *      -> 본인을 제외한 다른 멤버들에게 온도를 부여할 때 사용
     *
     * @param mateId 멤버 목록을 조회할 맛집메이트 ID
     * @param session 현재 사용자의 세션 객체
     * @return 조회된 멤버 목록을 포함한 HTTP 상태 코드가 200인 ResponseEntity 객체,
     *         HTTP 상태 코드가 400(잘못된 요청 시), 500(서버 오류 발생 시)인 ResponseEntity 객체
     */
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

    /**
     * 맛집메이트에 참여한 인원 수 조회
     *
     * @param mateId 조회할 맛집메이트 ID
     * @return 조회된 인원수를 포함한 HTTP 상태 코드가 200인 ResponseEntity 객체,
     *         HTTP 상태 코드가 400(잘못된 요청 시), 500(서버 오류 발생 시)인 ResponseEntity 객체
     */
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

    /**
     * 로그인한 사용자의 멤버 정보 조회
     * 로그인한 사용자가 참여한 맛집메이트의 다른 멤버에게 온도를 부여했는지 여부를 저장하는 isGiven값을 조회하기 위해 사용
     *
     * @param mateId 조회할 맛집메이트 ID
     * @param session 현재 사용자의 세션 객체
     * @return 조회된 멤버 정보를 포함한 HTTP 상태 코드가 200인 ResponseEntity 객체,
     *         조회된 멤버 정보가 없다면 HTTP 상태 코드가 404인 ResponseEntity 객체,
     *         HTTP 상태 코드가 400(잘못된 요청 시), 500(서버 오류 발생 시)인 ResponseEntity 객체
     */
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

    /**
     * 로그인한 사용자 본인을 제외한 맛집메이트 멤버들에게 메이트 온도(mateTemp) 부여
     *
     * @param mateId 해당 맛집메이트 ID
     * @param memberMateTempMap 멤버별 부여할 온도값이 저장된 Map객체 (key: 멤버 ID, value: 부여할 온도 값)
     * @param session 현재 사용자의 세션 객체
     * @return HTTP 상태 코드가 204(부여 성공 시), 400(잘못된 요청 시), 500(서버 오류 발생 시)인 ResponseEntity 객체
     */
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

    /**
     * 맛집메이트 멤버 삭제
     * 해당 맛집메이트 게시자만 가능
     *
     * @param matememberId 삭제할 멤버 ID
     * @return HTTP 상태 코드가 204(삭제 성공 시), 400(잘못된 요청 시), 500(서버 오류 발생 시)인 ResponseEntity 객체
     */
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