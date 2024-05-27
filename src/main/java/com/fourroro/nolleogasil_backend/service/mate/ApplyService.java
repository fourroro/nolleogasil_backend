package com.fourroro.nolleogasil_backend.service.mate;

import com.fourroro.nolleogasil_backend.dto.mate.ApplyDto;

import java.util.List;

public interface ApplyService {

    //apply 추가
    public void insertApply(ApplyDto applyDto);

    //isApply 변경
    public void updateIsApply(ApplyDto applyDto);

    //보낸 신청 목록 조회
    public List<ApplyDto> getSendApplyList(Long applicantId);

    //받은 신청 목록 조회
    public List<ApplyDto> getReceivedApplyList(Long usersId);

    //1개의 신청 조회(applyId 이용)
    public ApplyDto getApply(Long applyId);

    //1개의 신청 조회(mateId, applicantId 이용)
    public ApplyDto getApplyByMateIdAndUsersId(Long mateId, Long usersId);

    //apply 유무 확인
    public boolean checkApplyColumn(Long mateId, Long usersId);

    //데이터 삭제
    public void deleteApply(Long applyId);
}
