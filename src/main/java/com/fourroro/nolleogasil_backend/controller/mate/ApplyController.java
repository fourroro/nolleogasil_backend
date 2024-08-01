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

    //session에 있는 usersId 가져오기
    private Long getSessionUsersId(HttpSession session) {
        UsersDto usersSession = (UsersDto) session.getAttribute("users");
        return usersSession.getUsersId();
    }

    //사용자가 신청버튼 클릭 시, insert apply
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

    //사용자가 수락 or 거절 버튼 클릭 시, isApply 값 변경
    @PatchMapping("/{applyId}")
    public ResponseEntity<Void> updateIsApply(@PathVariable Long applyId, @RequestBody ApplyStatus isApply) {
        try {
            ApplyDto applyDto = applyService.getApply(applyId);
            if (applyDto == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            applyDto.setIsApply(isApply);
            MateDto mateDto = MateDto.changeToDto(mateService.getMate(applyDto.getMateId()));

            if (isApply == ApplyStatus.수락) {
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

    //로그인한 사용자의 해당 mate의 isApply 조회 -> Mate.js의 신청 버튼 상태에 사용됨
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
                //사용자가 해당 mate에 신청한 적이 없음
                return ResponseEntity.noContent().build();
            }
        } catch(Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    //로그인한 사용자가 보낸 신청 목록 조회
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

    //로그인한 사용자가 받은 신청 목록 조회
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