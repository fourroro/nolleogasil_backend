/**
 * 맛집메이트의 신청 관리를 위한 Controller입니다.
 * @author 박초은
 * @since 2024-01-05
 */
package com.fourroro.nolleogasil_backend.controller.mate;

import com.fourroro.nolleogasil_backend.dto.mate.ApplyDto;
import com.fourroro.nolleogasil_backend.dto.mate.ApplyStatus;
import com.fourroro.nolleogasil_backend.dto.mate.MateDto;
import com.fourroro.nolleogasil_backend.dto.mate.MateMemberDto;
import com.fourroro.nolleogasil_backend.dto.users.UsersDto;
import com.fourroro.nolleogasil_backend.service.chat.ChatRoomService;
import com.fourroro.nolleogasil_backend.service.mate.ApplyService;
import com.fourroro.nolleogasil_backend.service.mate.MateMemberService;
import com.fourroro.nolleogasil_backend.service.mate.MateService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/apply")
public class ApplyController {

    private final ApplyService applyService;
    private final MateService mateService;
    private final MateMemberService mateMemberService;
    private final ChatRoomService chatRoomService;

    /**
     * session에 저장된 UsersDto의 사용자 ID를 가져오는 함수
     *
     * @param session //현재 사용자의 세션 객체
     * @return 현재 세션에 저장된 사용자 ID
     */
    //session에 있는 usersId 가져오기
    private Long getSessionUsersId(HttpSession session) {
        UsersDto usersSession = (UsersDto) session.getAttribute("users");
        System.out.println("!!!!!!!");
        System.out.println(usersSession.getName());
        return usersSession.getUsersId();
    }

    /**
     * 맛집메이트의 신청 버튼 클릭 시, 로그인한 사용자의 신청 정보 저장
     *
     * @param mateId //해당 맛집메이트 ID
     * @param session //현재 사용자의 세션 객체
     * @return HTTP 상태 코드가 201(신청 저장 성공 시), 400(잘못된 요청 시), 500(서버 오류 발생 시)인 ResponseEntity 객체
     */
    @PostMapping("/{mateId}")
    public ResponseEntity<Void> createApply(@PathVariable Long mateId, HttpSession session) {
        try {
            if (mateId == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }

            Long usersId = getSessionUsersId(session);
            ApplyDto applyDto = ApplyDto.builder()
                    .mateId(mateId)
                    .applicantId(usersId)
                    .isApply(ApplyStatus.대기)
                    .build();

            applyService.insertApply(applyDto);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch(Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 받은 신청 목록에서 수락이나 거절 클릭 시, 해당 신청의 신청 상태(isApply 값) 변경
     *
     * @param applyId //해당 신청 ID
     * @param isApply //클릭한 신청 상태(수락 or 거절)
     * @return HTTP 상태 코드가 200(성공 시), 404(해당 신청을 찾지 못할 시), 500(서버 오류 발생 시)인 ResponseEntity 객체
     */
    @PatchMapping("/{applyId}")
    public ResponseEntity<Void> updateIsApply(@PathVariable Long applyId, @RequestParam String status) {
        try {
            ApplyDto applyDto = applyService.getApply(applyId);
            if (applyDto == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            //String타입의 status값을 ApplyStatus타입으로 변경
            ApplyStatus isApply = ApplyStatus.valueOf(status);
            applyDto.setIsApply(isApply);
            MateDto mateDto = MateDto.changeToDto(mateService.getMate(applyDto.getMateId()));

            if (status.equals("수락")) {
                //isApply 변경
                applyService.updateIsApply(applyDto);

                //MateMember에 추가
                Long chatroomId = chatRoomService.getChatRoomIdByMateId(applyDto.getMateId());
                MateMemberDto mateMemberDto = mateMemberService.creatMateMemberDto(mateDto, chatroomId, applyDto.getApplicantId());
                mateMemberService.insertMateMember(mateMemberDto);

            } else {  //status.equals("거절")
                //isApply 변경
                applyService.updateIsApply(applyDto);
            }
            return ResponseEntity.ok().build();
        } catch(Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 로그인한 사용자의 신청 상태 조회
     * Mate.js에서 사용 -> 신청 상태에 따라 신청 버튼 내 text 다르게 출력
     *
     * @param mateId 신청 상태를 조회할 맛집메이트 ID
     * @param session 현재 사용자의 세션 객체
     * @return 조회된 신청 상태를 포함한 HTTP 상태 코드가 200인 ResponseEntity 객체,
     *         조회된 신청 상태가 없을 시 HTTP 상태 코드가 204인 ResponseEntity 객체,
     *         서버 오류 발생 시 HTTP 상태 코드가 500인 ResponseEntity 객체
     */
    @GetMapping("/{mateId}/status")
    public ResponseEntity<ApplyStatus> checkApplyStatus(@PathVariable Long mateId, HttpSession session) {
        try {
            Long usersId = getSessionUsersId(session);

            //apply 유무 확인
            boolean checkingApply = applyService.checkApplyColumn(mateId, usersId);

            if (checkingApply) {
                ApplyDto applyDto = applyService.getApplyByMateIdAndUsersId(mateId, usersId);
                return ResponseEntity.ok(applyDto.getIsApply());
            } else {
                //사용자가 해당 mate에 신청한 적이 없다면
                return ResponseEntity.noContent().build();
            }
        } catch(Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 로그인한 사용자의 보낸 신청 목록 조회
     *
     * @param session 현재 사용자의 세션 객체
     * @return 조회된 보낸 신청 목록을 포함한 HTTP 상태 코드가 200인 ResponseEntity 객체,
     *         서버 오류 발생 시 HTTP 상태 코드가 500인 ResponseEntity 객체
     */
    @GetMapping("/send")
    public ResponseEntity<List<ApplyDto>> getSendApplyList(HttpSession session) {
        try {
            Long usersId = getSessionUsersId(session);
            List<ApplyDto> sendApplyList = applyService.getSendApplyList(usersId);
            return ResponseEntity.ok(sendApplyList);
        } catch(Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 로그인한 사용자의 받은 신청 목록 조회
     *
     * @param session 현재 사용자의 세션 객체
     * @return 조회된 받은 신청 목록을 포함한 HTTP 상태 코드가 200인 ResponseEntity 객체,
     *         서버 오류 발생 시 HTTP 상태 코드가 500인 ResponseEntity 객체
     */
    @GetMapping("/receive")
    public ResponseEntity<List<ApplyDto>> getReceiveApplyList(HttpSession session) {
        try {
            Long usersId = getSessionUsersId(session);
            List<ApplyDto> receiveApplyList = applyService.getReceiveApplyList(usersId);
            return ResponseEntity.ok(receiveApplyList);
        } catch(Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 신청 취소나 삭제 버튼 클릭 시, 신청 정보 삭제
     *
     * @param applyId 삭제할 신청 ID
     * @return HTTP 상태 코드가 204(삭제 성공 시), 400(잘못된 요청 시), 500(서버 오류 발생 시)인 ResponseEntity 객체
     */
    //해당 신청 삭제(및 취소)
    @DeleteMapping("/{applyId}")
    public ResponseEntity<Void> deleteApply(@PathVariable Long applyId) {
        try {
            if (applyId == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }

            applyService.deleteApply(applyId);
            return ResponseEntity.noContent().build();
        } catch(Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}