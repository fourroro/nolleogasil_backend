package com.fourroro.nolleogasil_backend.service.mate;

import com.fourroro.nolleogasil_backend.dto.chat.ChatRoomAndPlaceDto;
import com.fourroro.nolleogasil_backend.dto.mate.MateDto;
import com.fourroro.nolleogasil_backend.dto.mate.MateMemberDto;
import com.fourroro.nolleogasil_backend.dto.place.PlaceDto;
import com.fourroro.nolleogasil_backend.dto.users.UsersDto;
import com.fourroro.nolleogasil_backend.entity.chat.ChatRoom;
import com.fourroro.nolleogasil_backend.entity.mate.Mate;
import com.fourroro.nolleogasil_backend.entity.mate.MateMember;
import com.fourroro.nolleogasil_backend.entity.place.Place;
import com.fourroro.nolleogasil_backend.entity.users.Users;
import com.fourroro.nolleogasil_backend.global.exception.GlobalException;
import com.fourroro.nolleogasil_backend.global.exception.ResultCode;
import com.fourroro.nolleogasil_backend.repository.mate.MateMemberRepository;
import com.fourroro.nolleogasil_backend.service.chat.ChatRoomService;
import com.fourroro.nolleogasil_backend.service.users.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MateMemberServiceImpl implements MateMemberService {

    private final MateMemberRepository mateMemberRepository;
    private final UsersService usersService;
    private final ChatRoomService chatRoomService;
    private final MateService mateService;

    @Override
    public boolean checkFirstEnterRoom(Long chatRoomId, Long usersId) {
        MateMember mateMember = mateMemberRepository.findByChatRoomChatroomIdAndUsersUsersId(chatRoomId,usersId);
        System.out.println(mateMember.getMatememberId());
        System.out.println(mateMember.getIsFirst());
        if(mateMember.getIsFirst() == 1) {
            //첫입장
            mateMember.setIsFirst(0);
            mateMemberRepository.save(mateMember);
            return true;
        } else {
            //첫입장x
            return false;
        }
    }

    @Override
    public List<MateMember> getMateMembersByUsersId(Long usersId) {
        List<MateMember> mateMembers = mateMemberRepository.findAllByUsersUsersId(usersId);
        return mateMembers;
    }

    @Override
    public MateMemberDto creatMateMemberDto(MateDto mateDto, Long chatroomId, Long usersId) {
        return MateMemberDto.builder()
                .mate(mateDto)
                .usersId(usersId)
                .chatroomID(chatroomId)
                .isGiven(1)
                .build();
    }

    @Transactional
    @Override
    public void insertMateMember(MateMemberDto mateMemberDto) {
        Users users = usersService.findByUsersId(mateMemberDto.getUsersId());
        Mate mate = Mate.changeToEntity(mateMemberDto.getMate(), users);
        ChatRoom chatRoom = chatRoomService.getChatRoom(mateMemberDto.getChatroomID());

        MateMember mateMember = MateMember.changeToEntity(mateMemberDto, users, mate, chatRoom);
        MateMember savedMateMember = mateMemberRepository.save(mateMember);
        Mate savedMate = mateService.getMate(mate.getMateId());

        chatRoom.getMateMembers().add(savedMateMember);
        savedMate.getMateMembers().add(savedMateMember);
    }

    public List<ChatRoomAndPlaceDto> getChatRoomAndPlaceDto(List<MateMember> mateMembers) {
        List<ChatRoomAndPlaceDto> chatRoomAndPlaceDtos = new ArrayList<>();

        for(MateMember mateMember : mateMembers) {
            ChatRoom chatRoom = mateMember.getChatRoom();
            if(chatRoom.getUsers().getUsersId() != mateMember.getUsers().getUsersId()) {
                Mate mate = mateMember.getMate();
                MateDto mateDto = MateDto.changeToDto(mate);
                Place place = mate.getPlace();
                PlaceDto placeDto = PlaceDto.changeToDto(place);
                int memberCnt = chatRoom.getMateMembers().size();
                ChatRoomAndPlaceDto chatRoomAndPlaceDto = ChatRoomAndPlaceDto.ChangeToDto(chatRoom, placeDto, mateDto,memberCnt);
                chatRoomAndPlaceDtos.add(chatRoomAndPlaceDto);
            }
        }

            return chatRoomAndPlaceDtos;
        }

    @Override
    public List<ChatRoomAndPlaceDto> getChatRoomListByMate(Long usersId) {
        List<MateMember> mateMembers = this.getMateMembersByUsersId(usersId);
        List<ChatRoomAndPlaceDto> chatRoomAndPlaceDtos = this.getChatRoomAndPlaceDto(mateMembers);
        return chatRoomAndPlaceDtos;
    }

    @Override
    public List<ChatRoomAndPlaceDto> getJoinedRoomsBySorted(Long usersId,String sortedBy) {
        List<MateMember> mateMembers;
        if(sortedBy.equals("최신순")) {
             mateMembers = mateMemberRepository.findAllByUsers_UsersIdOrderByMate_EatDateDesc(usersId);
        } else {
             mateMembers = mateMemberRepository.findAllByUsers_UsersIdOrderByMate_EatDate(usersId);
        }
        List<ChatRoomAndPlaceDto> chatRoomAndPlaceDtos = this.getChatRoomAndPlaceDto(mateMembers);

        return chatRoomAndPlaceDtos;
    }

    @Override
    public Long countMateMember(Long mateId) {
        return mateMemberRepository.countByMateMateId(mateId);
    }

    @Override
    public List<MateMemberDto> getMateMemberList(Long mateId) {
        List<MateMember> mateMemberList = mateMemberRepository.findByMateMateIdOrderByMatememberId(mateId);
        return changeToDtoList(mateMemberList);
    }

    @Override
    public List<MateMemberDto> getMateMemberListWithoutMe(Long mateId, Long usersId) {
        List<MateMember> mateMemberList = mateMemberRepository.findByMateMemberWithoutMe(mateId, usersId);
        return changeToDtoList(mateMemberList);
    }

    @Override
    public MateMember getMateMember(Long matememberId) {
        return mateMemberRepository.findById(matememberId)
                .orElseThrow(() -> new GlobalException(ResultCode.NOT_FOUND_MATEMEMBER));
    }

    @Override
    public MateMember getMateMemberByUsersIdAndMateId(Long usersId, Long mateId) {
        return mateMemberRepository.findByUsersUsersIdAndMateMateId(usersId, mateId)
                .orElseThrow(() -> new GlobalException(ResultCode.NOT_FOUND_MATEMEMBER));
    }

    @Override
    public List<MateMemberDto> getMateHistory(Long usersId) {
        List<MateMember> mateHistory = mateMemberRepository.findMateHistoryByUsersId(usersId);
        return changeToDtoList(mateHistory);
    }

    @Transactional
    @Override
    public void setMemberMateTemp(UsersDto member, float mateTemp, Long usersId, Long mateId) {
        float memberMateTemp = member.getMateTemp();

        //36.5도 이상이라면 평균값 + 0.1도씩 상승(최고 100도)
        float mateTempValue = (memberMateTemp + mateTemp) / 2 + 0.1f;
        float newMateTemp = (float) (Math.round(mateTempValue * 10.0) / 10.0);   //소수점 한자리까지 반올림
        if (newMateTemp > 100) {  //최고 온도: 100도
            newMateTemp = 100;
        }
        usersService.setMateTemp(member.getUsersId(), newMateTemp);

        //온도 부여한 member(로그인한 사용자)의 isGiven 0으로 변경
        MateMember users = this.getMateMemberByUsersIdAndMateId(usersId, mateId);
        users.setIsGiven(0);
        mateMemberRepository.save(users);
    }

    @Transactional
    @Override
    public void deleteMateMember(Long matememberId) {
        mateMemberRepository.deleteById(matememberId);
    }

    //entityList -> dtoList
    public List<MateMemberDto> changeToDtoList(List<MateMember> mateMemberList) {
        List<MateMemberDto> mateMemberDtoList = new ArrayList<>();

        for (MateMember member : mateMemberList) {
            MateMemberDto mateMemberDto = MateMemberDto.changeToDto(member);
            mateMemberDtoList.add(mateMemberDto);
        }
        return mateMemberDtoList;
    }
}
