package com.fourroro.nolleogasil_backend.service.mate;

import com.fourroro.nolleogasil_backend.dto.chat.ChatRoomAndPlaceDto;
import com.fourroro.nolleogasil_backend.dto.mate.MateDto;
import com.fourroro.nolleogasil_backend.dto.mate.MateMemberDto;
import com.fourroro.nolleogasil_backend.dto.users.UsersDto;
import com.fourroro.nolleogasil_backend.entity.mate.MateMember;

import java.util.List;

public interface MateMemberService {

    //mateMember인지 확인
    public boolean checkFirstEnterRoom(Long chatRoomId, Long usersId);
  
    //해당 유저의 챗방목록 및 메이트 목록
    public List<MateMember> getMateMembersByUsersId(Long usersId);

    //MateMemberDto 생성
    public MateMemberDto creatMateMemberDto(MateDto mateDto, Long chatroomId, Long usersId);

    //mateMeber 추가
    public void insertMateMember(MateMemberDto mateMemberDto);

    List<ChatRoomAndPlaceDto> getChatRoomListByMate(Long usersId);
    List<ChatRoomAndPlaceDto> getJoinedRoomsBySorted(Long usersId,String sortedBy);

    //해당 mate의 mateMember 수
    public Long countMateMember(Long mateId);

    //해당 mate의 mateMember 목록 조회
    public List<MateMemberDto> getMateMemberList(Long mateId);

    //해당 mate의 mateMember 목록 조회(단, 로그인 한 사용자는 제외)
    public List<MateMemberDto> getMateMemberListWithoutMe(Long mateId, Long usersId);

    //1명의 mateMember 조회
    public MateMember getMateMember(Long matememberId);

    //1명의 mateMember 조회(mateId, usersId 이용)
    public MateMember getMateMemberByUsersIdAndMateId(Long usersId, Long mateId);

    //mate 이력 조회
    public List<MateMemberDto> getMateHistory(Long usersId);

    //동일 mate의 다른 member들에게 mateTemp부여
    public void setMemberMateTemp(UsersDto member, float mateTemp, Long usersId, Long mateId);

    //mateMember 삭제
    public void deleteMateMember(Long matememberId);

}