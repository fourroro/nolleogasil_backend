package com.fourroro.nolleogasil_backend.controller.mate;

import com.fourroro.nolleogasil_backend.dto.mate.MateMemberDto;
import com.fourroro.nolleogasil_backend.dto.users.UsersDto;
import com.fourroro.nolleogasil_backend.entity.mate.MateMember;
import com.fourroro.nolleogasil_backend.service.mate.MateMemberService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mateMember")
public class MateMemberController {

    private final MateMemberService mateMemberService;
    private final RedisTemplate<String, Object> redisTemplate;

    //session에 있는 usersId 가져오기
    private Long getSessionUsersId() {
        ValueOperations<String, Object> operations = redisTemplate.opsForValue();
        UsersDto usersDto = (UsersDto) operations.get("users");

        return usersDto.getUsersId();
    }

    //첫 입장인지......
    @GetMapping("/{chatroomId}")
    public ResponseEntity<String> checkFirstEnterRoom(@PathVariable String chatroomId) {

        Long chatRoomId = Long.parseLong(chatroomId);
        Long userId = getSessionUsersId();


        boolean isFirst = mateMemberService.checkFirstEnterRoom(chatRoomId,userId);

        if(isFirst) {
            return ResponseEntity.ok("firstEnter");
        } else {
            return ResponseEntity.ok("noFirstEnter");
        }
    }

    //사용자의 mate이력 조회
    @GetMapping("/getMateHistory")
    public List<MateMemberDto> getMateHistory() {
        Long usersId = getSessionUsersId();
        return mateMemberService.getMateHistory(usersId);
    }

    //해당 mate의 mateMember 목록 조회
    @GetMapping("/getMateMemberList")
    public List<MateMemberDto> getMateMemberList(@RequestParam Long mateId) {
        return mateMemberService.getMateMemberList(mateId);
    }

    //해당 mate의 mateMember 목록 조회(단, 로그인한 사용자는 제외) -> 온도 부여할 때 사용
    @GetMapping("/getMateMemberListWithoutMe")
    public List<MateMemberDto> getMateMemberListWithoutMe(@RequestParam Long mateId) {
        Long usersId = getSessionUsersId();
        return mateMemberService.getMateMemberListWithoutMe(mateId, usersId);
    }

    //mateMember 수 조회
    @GetMapping("/countMateMember")
    public Long countMateMember(@RequestParam Long mateId) {
        return mateMemberService.countMateMember(mateId);
    }

    //1명의 mateMember 정보 조회(usersId, mateId 이용) -> 해당 member의 isGiven 조회할 때 사용
    @GetMapping("/getMateMemberByUsersIdAndMateId")
    public MateMemberDto getMateMemberByUsersIdAndMateId(@RequestParam Long mateId) {
        Long usersId = getSessionUsersId();
        MateMember mateMember = mateMemberService.getMateMemberByUsersIdAndMateId(usersId, mateId);
        return MateMemberDto.changeToDto(mateMember);
    }

    //본인 제외, 다른 member들에게 mateTemp 부여
    @PostMapping("/setMemberMateTemp")
    public String setMemberMateTemp(@RequestBody Map<Long, Float> memberMateTempMap, @RequestParam Long mateId) {
        try {
            Long usersId = getSessionUsersId();

            for (Map.Entry<Long, Float> entry : memberMateTempMap.entrySet()) {
                Long memberId = entry.getKey();
                Float mateTemp = entry.getValue();

                MateMember mateMember = mateMemberService.getMateMember(memberId);
                UsersDto member = UsersDto.changeToDto(mateMember.getUsers());
                mateMemberService.setMemberMateTemp(member, mateTemp, usersId, mateId);
            }
            return "successful";
        } catch (Exception e) {
            e.printStackTrace();
            return "failed";
        }
    }

    //해당 memeber 삭제
    @PostMapping("/deleteMateMember")
    public String deleteMateMember(@RequestParam Long matememberId) {
        try {
            if (matememberId == null) {
                return "failed";
            }

            mateMemberService.deleteMateMember(matememberId);
            return "successful";
        } catch (Exception e) {
            e.printStackTrace();
            return "failed";
        }
    }
}
