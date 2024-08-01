package com.fourroro.nolleogasil_backend.dto.mate;

import com.fourroro.nolleogasil_backend.entity.mate.Apply;
import lombok.*;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ApplyDto {

    private Long applyId;           //PK
    private Long mateId;            //해당 mate_id
    private Long applicantId;       //신청자 users_id
    private ApplyStatus isApply;    //신청 상태("대기", "수락", "거절" 中 1)

    public void setIsApply(ApplyStatus isApply) {
        this.isApply = isApply;
    }

    //entity -> dto
    public static ApplyDto changeToDto(Apply entity) {
        return ApplyDto.builder()
                .applyId(entity.getApplyId())
                .mateId(entity.getMate().getMateId())
                .applicantId(entity.getUsers().getUsersId())
                .isApply(entity.getIsApply())
                .build();
    }

}
