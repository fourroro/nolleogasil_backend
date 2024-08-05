/*
 * 이 클래스는 맛집메이트의 신청을 DB에 저장 및 DB에서 조회, 삭제하기 위한 Repository입니다.
 * @author 박초은
 * @since 2024-01-05
 */
package com.fourroro.nolleogasil_backend.repository.mate;

import com.fourroro.nolleogasil_backend.dto.mate.ApplyStatus;
import com.fourroro.nolleogasil_backend.entity.mate.Apply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ApplyRepository extends JpaRepository<Apply, Long> {
    //save()(=persist())
    //findById()
    //deleteById()

    //보낸 신청 목록 조회
    @Query("select a from Apply a where a.users.usersId = :applicantId order by a.applyId DESC")
    public List<Apply> findByApplicantId(@Param("applicantId")Long applicantId);

    //받은 신청 목록 조회
    @Query("select a from Apply a where a.mate.users.usersId = :usersId order by a.applyId DESC")
    public List<Apply> findByMasterId(@Param("usersId")Long usersId);

    //apply 유무 확인
    public boolean existsByMateMateIdAndUsersUsersId(Long mateId, Long applicantId);

    //mateId, usersId(applicantId)로 apply 조회
    public Optional<Apply> findByMateMateIdAndUsersUsersId(Long mateId, Long applicantId);

    //isApply값 변경
    @Modifying
    @Query("update Apply a set a.isApply = :isApply where a.applyId = :applyId")
    public void updateIsApply(@Param("applyId")Long applyId, @Param("isApply")ApplyStatus isApply);

}