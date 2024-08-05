/*
 * 이 클래스는 장소를 DB에 저장 및 DB에서 조회하기 위한 Repository입니다.
 * @author 박초은
 * @since 2024-01-05
 */
package com.fourroro.nolleogasil_backend.repository.place;

import com.fourroro.nolleogasil_backend.entity.place.Place;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceRepository extends JpaRepository<Place, Integer> {
    //save()(=persist())
    //existsById()
}
