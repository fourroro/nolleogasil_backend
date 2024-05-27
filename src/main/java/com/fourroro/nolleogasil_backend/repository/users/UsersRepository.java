package com.fourroro.nolleogasil_backend.repository.users;

import com.fourroro.nolleogasil_backend.entity.users.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users, Long> {
    @Modifying
    @Query(value = "update Users u " +
            "set u.nickname = :nickname " +
            "where u.usersId = :usersId")
    public void updateUsers(@Param("nickname")String nickname, @Param("usersId")Long usersId);

    @Modifying
    @Query(value = "update Users u " +
            "set u.phone = :phone " +
            "where u.usersId = :usersId")
    public void updatePhone(@Param("phone")String phone, @Param("usersId")Long usersId);

    public Users findByNickname(String nickname);

    @Query(value = "SELECT u FROM Users u WHERE u.usersId = :usersId")
    public Optional<Users> findByUsersId(@Param("usersId") Long usersId);


    public Users findByEmail(String email);

    @Query(value = "SELECT u.nickname FROM Users u WHERE u.email = :email")
    public String findNickname(@Param("email")String email);


    @Query(value = "SELECT u.nickname FROM Users u WHERE u.usersId = :usersId")
    public String findNicknameByUsersId(@Param("usersId") Long usersId);

    public Users findUsersByEmail(String email);

    public Users findByPhone(String phone);

    //동일 mate의 다른 member들에게 mateTemp부여
    @Modifying
    @Query("update Users u set u.matetemp = :mateTemp where u.usersId = :usersId")
    public void setMateTemp(@Param("usersId")Long usersId, @Param("mateTemp")float mateTemp);
}