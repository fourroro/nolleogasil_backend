package com.fourroro.nolleogasil_backend.service.users;

import com.fourroro.nolleogasil_backend.dto.users.UsersDto;
import com.fourroro.nolleogasil_backend.entity.users.Users;

import java.util.List;

public interface UsersService {

    public void insertUsers(UsersDto users);
    //public Users findByusersId(Long usersId);

    public Users findByUsersId(Long usersId);

    public Users findUsers(Long usersId);

    public List<Users> findAll();

    public String findNickname(String email);


    public Users findByEmail(String email);

    public Users findByPhone(String phone);
    
    //회원저장

    //회원정보 수정
    public void updateUsers(String nickname, Long usersId);

    public void deleteUsers(Long usersId);

    Users findUsersByEmail(String email);

    public void setMateTemp(Long usersId, Float mateTemp);
}
