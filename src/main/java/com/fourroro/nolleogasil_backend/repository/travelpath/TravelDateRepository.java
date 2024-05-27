package com.fourroro.nolleogasil_backend.repository.travelpath;

import com.fourroro.nolleogasil_backend.entity.travelpath.TravelDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TravelDateRepository extends JpaRepository<TravelDate, Long> {


}
