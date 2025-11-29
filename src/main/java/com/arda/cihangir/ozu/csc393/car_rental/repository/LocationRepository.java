package com.arda.cihangir.ozu.csc393.car_rental.repository;

import com.arda.cihangir.ozu.csc393.car_rental.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends JpaRepository<Location, String> {
}