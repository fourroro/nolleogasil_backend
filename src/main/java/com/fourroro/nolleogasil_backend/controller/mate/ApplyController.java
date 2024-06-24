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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
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
    private final RedisTemplate<String, Object> redisTemplate;

    //session에 있는 usersId 가져오기
    private Long getSessionUsersId(HttpSession session) {
        ValueOperations<String, Object> operations = redisTemplate.opsForValue();
        UsersDto usersDto = (UsersDto) operations.get("users");

        return usersDto.getUsersId();
    }

    //apply 추가
    @PostMapping("/insertApply")
    public String insertApply(@RequestParam Long mateId, HttpSession session) {
        if (mateId == null) {
            return "failed";
        }

        Long usersId = getSessionUsersId(session);
        ApplyDto applyDto = ApplyDto.builder()
                .mateId(mateId)
                .applicantId(usersId)
                .isApply(ApplyStatus.대기)
                .build();

        try {
            if(chatRoomService.checkMateMemberNum(mateId)) {
                applyService.insertApply(applyDto);
            }
            else {
                return "failed";
            }
            return "successful";
        } catch(Exception e) {
            e.printStackTrace();
            return "failed";
        }
    }

    //isApply 변경
    @PostMapping("/updateIsApply")
    public String updateIsApply(@RequestBody ApplyDto applyRequestDto) {
        Long applyId = applyRequestDto.getApplyId();
        String status = String.valueOf(applyRequestDto.getIsApply());

        ApplyDto applyDto = applyService.getApply(applyId);
        applyDto.setIsApply(applyRequestDto.getIsApply());
        MateDto mateDto = MateDto.changeToDto(mateService.getMate(applyDto.getMateId()));

        try {
            if (status.equals("수락")) {
                //isApply 변경
                applyService.updateIsApply(applyDto);

                //MateMember 추가
                Long chatroomId = chatRoomService.getChatRoomIdByMateId(applyDto.getMateId());
                MateMemberDto mateMemberDto = mateMemberService.creatMateMemberDto(mateDto, chatroomId, applyDto.getApplicantId());
                mateMemberService.insertMateMember(mateMemberDto);

            } else {  //status.equals("거절")
                applyService.updateIsApply(applyDto);
            }

            return String.valueOf(applyDto.getIsApply());
        } catch(Exception e) {
            e.printStackTrace();
            return "failed";
        }
    }

    //apply유무 확인 후 해당 apply의 isApply 반환 -> Mate.js의 버튼상태에서 사용
    @GetMapping("/checkingApplyStatus")
    public String checkingApplyStatus(@RequestParam Long mateId, HttpSession session) {
        Long usersId = getSessionUsersId(session);
        boolean checkingApply = applyService.checkApplyColumn(mateId, usersId);
        if (checkingApply) {
            ApplyDto applyDto = applyService.getApplyByMateIdAndUsersId(mateId, usersId);
            return String.valueOf(applyDto.getIsApply());
        } else {
            return "failed";
        }
    }

    //보낸 신청 목록 조회
    @GetMapping("/getSendApply")
    public List<ApplyDto> getSendApply(HttpSession session) {
        Long usersId = getSessionUsersId(session);
        return applyService.getSendApplyList(usersId);
    }

    //받은 신청 목록 조회
    @GetMapping("/getReceivedApply")
    public List<ApplyDto> getReceivedApply(HttpSession session) {
        Long usersId = getSessionUsersId(session);
        return applyService.getReceivedApplyList(usersId);
    }

    //apply 삭제
    @PostMapping("/deleteApply")
    public String deleteApply(@RequestBody ApplyDto applyDto) {
        try {
            Long applyId = applyDto.getApplyId();

            applyService.deleteApply(applyId);
            return "successful";
        } catch(Exception e) {
            e.printStackTrace();
            return "failed";
        }
    }
}
