package com.fourroro.nolleogasil_backend.service.users;

import com.fourroro.nolleogasil_backend.dto.users.UsersDto;
import com.fourroro.nolleogasil_backend.entity.users.Users;
import com.fourroro.nolleogasil_backend.global.exception.GlobalException;
import com.fourroro.nolleogasil_backend.global.exception.ResultCode;
import com.fourroro.nolleogasil_backend.repository.users.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.fourroro.nolleogasil_backend.dto.users.UsersDto.changeToDto;

@Service
@Transactional
@RequiredArgsConstructor
public class UsersServiceImpl implements UsersService {
    private final UsersRepository usersRepository;

    @Override
    @Transactional
    public void insertUsers(UsersDto usersDto) {
        //성별 한글로 변환
        if(usersDto.getGender().equals("female")){
            usersDto.setGender("여성");
        }else if(usersDto.getGender().equals("male")){
            usersDto.setGender("남성");
        }else{
            usersDto.setGender("성별 미상");
        }

        Users users = Users.changeToEntity(usersDto);
        usersRepository.save(users);
    }

    @Override
    public Users findByUsersId(Long usersId) {
        Users users = usersRepository.findById(usersId)
        .orElseThrow(()->new GlobalException(ResultCode.NOT_FOUND_USER));

        return users;
    }

    @Override
    public Users findUsers(Long usersId) {
        Optional<Users> optionalUsers = usersRepository.findByUsersId(usersId);

        if (optionalUsers.isPresent()) {
            Users users = optionalUsers.get();
            UsersDto usersDto = changeToDto(users);

            return users;
        }else{
            return null;
        }

    }


    @Override
    public List<Users> findAll() {
        return usersRepository.findAll();
    }

    public String findNickname(String email){
        return usersRepository.findNickname(email);
    }


    public Users findByEmail(String email){
        return usersRepository.findByEmail(email);
    }

    @Override
    public Users findByPhone(String phone) {
        return usersRepository.findByPhone(phone);
    }

    public boolean validateDuplicateUsers(UsersDto usersDto) throws IllegalAccessException {
        Users users = Users.changeToEntity(usersDto);
        Users isExistUser = usersRepository.findByEmail(users.getEmail());
        return isExistUser!=null;
    }

    public boolean validateDuplicatePhoneAndUpdate(String email, String phone) {
        // 이메일로 기존 사용자 조회
        Users existingUser = usersRepository.findByEmail(email);
        UsersDto usersDto = changeToDto(existingUser);
        if (usersDto != null) {
            // 기존 사용자가 존재하면 폰 넘버 비교 및 업데이트
            String existingPhone = usersDto.getPhone();
            if (!existingPhone.equals(phone)) {
                // 전화번호가 변경된 경우 업데이트
                Long usersId = usersDto.getUsersId();
                usersDto.setPhone(phone);
                usersRepository.updatePhone(phone, usersId);
                return true; // 업데이트 성공
            }
        }
        return false; // 업데이트 실패 또는 사용자가 존재하지 않음
    }

    @Override
    @Transactional
    public void updateUsers(String nickname, Long usersId) {
        //회원정보 수정
        usersRepository.updateUsers(nickname, usersId);
    }

    @Override
    @Transactional
    public void deleteUsers(Long usersId) {
        usersRepository.deleteById(usersId);
    }


    @Override
    public Users findUsersByEmail(String email) {
        return usersRepository.findUsersByEmail(email);
    }

    @Transactional
    @Override
    public void setMateTemp(Long usersId, Float mateTemp) {
        usersRepository.setMateTemp(usersId, mateTemp);
    }

}
