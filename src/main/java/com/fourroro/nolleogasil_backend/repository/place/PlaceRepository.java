package com.fourroro.nolleogasil_backend.repository.place;

import com.fourroro.nolleogasil_backend.entity.place.Place;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceRepository extends JpaRepository<Place, Integer> {
    //save()(=persist())
    //existsById()
}
