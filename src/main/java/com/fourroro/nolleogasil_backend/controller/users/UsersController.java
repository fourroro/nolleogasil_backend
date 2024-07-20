package com.fourroro.nolleogasil_backend.controller.users;

import com.fourroro.nolleogasil_backend.dto.users.KakaoDto;
import com.fourroro.nolleogasil_backend.dto.users.UsersDto;
import com.fourroro.nolleogasil_backend.entity.users.Users;
import com.fourroro.nolleogasil_backend.service.users.KakaoService;
import com.fourroro.nolleogasil_backend.service.users.UsersServiceImpl;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@EnableRedisHttpSession
@RequestMapping("/api/user")
public class UsersController {
    private final UsersServiceImpl usersService;
    private final KakaoService kakaoService;

    private final RedisTemplate<String, Object> redisTemplate;
    private ValueOperations<String, Object> operations;
    //redisTemplate이 초기화된 후에 ValueOperations를 초기화
    @PostConstruct
    private void init() {
        this.operations = redisTemplate.opsForValue();
    }

    //회원가입 및 로그인
    //세션 확인하기
    @PostMapping("/profile")
    public ResponseEntity<Long> setUserProfile(@RequestBody KakaoDto kakaoRequest, HttpSession session){
        try{
            //카카오로부터 받은 사용자 정보 中 phone_number
            String kakaoUsersPhone = kakaoRequest.getPhone();
            String kakaoUsersEmail = kakaoRequest.getEmail();
            //기존 회원 여부 확인
            boolean isDuplicate = usersService.validateDuplicateUsers(kakaoRequest.toDto());

            if(isDuplicate) { //기존 회원인 경우
                Users existingUsers = usersService.findByEmail(kakaoUsersEmail);
                String existingPhone = existingUsers.getPhone();
                //전화번호 변경되었으면
                if(!existingPhone.equals(kakaoUsersPhone)){
                    System.out.println("phone number changed...");
                    usersService.validateDuplicatePhoneAndUpdate(kakaoUsersEmail, kakaoUsersPhone);
                }else{
                    System.out.println("phone number not changed...");
                }
                UsersDto usersDto = UsersDto.changeToDto(existingUsers);

                //세션에 사용자 정보 저장
                session.setAttribute("usersId", usersDto.getUsersId());
                UsersDto sessionUsersDto = (UsersDto) session.getAttribute("usersId");
                System.out.println("!!!!!!!!!로그인!!!!!!!!!!!");
//                operations.set("users", usersDto);

                //프론트엔드로 기존 회원임을 전달
                return ResponseEntity.badRequest().body(usersDto.getUsersId());

            }else { //신규 회원인 경우
                usersService.insertUsers(kakaoRequest.toDto());
                Users users = usersService.findByEmail(kakaoRequest.getEmail());
                UsersDto usersDto = UsersDto.changeToDto(users);

                //세션에 사용자 정보 저장
//                operations.set("users", usersDto);
                session.setAttribute("users", usersDto);

                //프론트엔드로 신규 회원임을 전달
                return ResponseEntity.ok(usersDto.getUsersId());
            }
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(-1L);
        }
    }

    @GetMapping("/callback")
    public KakaoDto getKakaoAccount(@RequestParam("code") String code){
        log.debug("code = {}", code);
        return kakaoService.getInfo(code).getKakaoDto();
    }

    @GetMapping("/info")
    public ResponseEntity<UsersDto> getUserInfo(@RequestParam String email){
        Users users = usersService.findUsersByEmail(email);
        UsersDto userInfo = UsersDto.changeToDto(users);

        return ResponseEntity.ok(userInfo);
    }

    //회원정보 수정
    @GetMapping("/update/{usersId}")
    public ResponseEntity<UsersDto> updateForm(@PathVariable Long usersId){

        try {
            Users users = usersService.findUsers(usersId);
            UsersDto usersDto = UsersDto.changeToDto(users);
            if (usersDto != null) { //회원 정보 존재 시
                return ResponseEntity.ok(usersDto);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/update/{usersId}")
    public ResponseEntity<String> updateUsers(@PathVariable Long usersId, @RequestBody Map<String, String> requestBody, HttpServletRequest request){
        try{
            String nickname = requestBody.get("nickname");

            usersService.updateUsers(nickname, usersId);
            // 세션에 저장된 사용자 정보 업데이트
            if(operations.get("users") != null) {
                UsersDto userInfo = (UsersDto) operations.get("users");

                if(userInfo != null) {
                    userInfo.setNickname(nickname);

                    //세션에서 users의 value 삭제
                    redisTemplate.delete("users");
                    //세션에 새로 저장
                    operations.set("users", userInfo);
                }
            }else{
                System.out.println("session is empty!");
            }

            return ResponseEntity.ok("User info update successfully!");
        }catch(Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    //mate의 master정보 가져올 때 사용, mate의 member정보 가져올 때 사용
    @GetMapping("/getUsersInfo")
    public UsersDto getUsersInfo(@RequestParam Long usersId) {
        Users users = usersService.findByUsersId(usersId);
        return UsersDto.changeToDto(users);
    }

    //로그아웃
    @RequestMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        //세션에서 사용자 정보 제거
        UsersDto sessionUsersDto = (UsersDto) session.getAttribute("spring:session:sessions" + session.getId());

        if (sessionUsersDto != null) {
            //세션에서 users의 value 삭
            session.invalidate();
            System.out.println("!!!!!!!!!로그아웃!!!!!!!!!!!");
            System.out.println(sessionUsersDto.getName());

            return ResponseEntity.ok("Logout successful");
        } else{
            System.out.println("!!!!!!!!!!!!!!!!!!!!세션 없음");
            return ResponseEntity.ok("No active redis session found");
        }
    }

    //회원탈퇴
    @DeleteMapping("/delete/{usersId}")
    public ResponseEntity<String> deleteUsers(@PathVariable Long usersId){
        usersService.deleteUsers(usersId);
        //세션 무효화
        redisTemplate.delete("users");
        return ResponseEntity.ok("User delete successfully");
    }
}