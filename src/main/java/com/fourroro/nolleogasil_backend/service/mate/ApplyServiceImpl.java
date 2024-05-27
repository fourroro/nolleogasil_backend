package com.fourroro.nolleogasil_backend.service.mate;

import com.fourroro.nolleogasil_backend.dto.mate.ApplyDto;
import com.fourroro.nolleogasil_backend.entity.mate.Apply;
import com.fourroro.nolleogasil_backend.entity.mate.Mate;
import com.fourroro.nolleogasil_backend.entity.users.Users;
import com.fourroro.nolleogasil_backend.global.exception.GlobalException;
import com.fourroro.nolleogasil_backend.global.exception.ResultCode;
import com.fourroro.nolleogasil_backend.repository.mate.ApplyRepository;
import com.fourroro.nolleogasil_backend.service.users.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplyServiceImpl implements ApplyService {

    private final ApplyRepository applyRepository;
    private final MateService mateService;
    private final UsersService usersService;

    @Transactional
    @Override
    public void insertApply(ApplyDto applyDto) {

        Mate mate = mateService.getMate(applyDto.getMateId());
        Users applicant = usersService.findByUsersId(applyDto.getApplicantId());
        Apply apply = Apply.changeToEntity(applyDto, mate, applicant);
        applyRepository.save(apply);
    }

    @Transactional
    @Override
    public void updateIsApply(ApplyDto applyDto) {
        applyRepository.updateIsApply(applyDto.getApplyId(), applyDto.getIsApply());
    }

    @Override
    public List<ApplyDto> getSendApplyList(Long applicantId) {
        List<Apply> sendApplyList = applyRepository.findByApplicantId(applicantId);
        return changeToDtoList(sendApplyList);
    }

    @Override
    public List<ApplyDto> getReceivedApplyList(Long usersId) {
        List<Apply> receivedApplyList = applyRepository.findByMasterId(usersId);
        return changeToDtoList(receivedApplyList);
    }

    @Override
    public ApplyDto getApply(Long applyId) {
        Apply apply = applyRepository.findById(applyId)
                .orElseThrow(() -> new GlobalException(ResultCode.NOT_FOUND_APPLY));
        return ApplyDto.changeToDto(apply);
    }

    @Override
    public ApplyDto getApplyByMateIdAndUsersId(Long mateId, Long usersId) {
        Apply apply = applyRepository.findByMateMateIdAndUsersUsersId(mateId, usersId)
                .orElseThrow(() -> new GlobalException(ResultCode.NOT_FOUND_APPLY));

        return ApplyDto.changeToDto(apply);
    }

    @Override
    public boolean checkApplyColumn(Long mateId, Long usersId) {
        return applyRepository.existsByMateMateIdAndUsersUsersId(mateId, usersId);
    }

    @Transactional
    @Override
    public void deleteApply(Long applyId) {
        applyRepository.deleteById(applyId);
    }

    //entityList -> dtoList
    public List<ApplyDto> changeToDtoList(List<Apply> applyList) {
        if (applyList.isEmpty()) {
            return Collections.emptyList();
        }

        List<ApplyDto> applyDtoList = new ArrayList<>();
        for (Apply value : applyList) {
            ApplyDto applyDto = ApplyDto.changeToDto(value);
            applyDtoList.add(applyDto);
        }
        return applyDtoList;
    }

}
