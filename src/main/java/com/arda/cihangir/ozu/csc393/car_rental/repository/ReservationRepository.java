package com.arda.cihangir.ozu.csc393.car_rental.repository;

import com.arda.cihangir.ozu.csc393.car_rental.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, String> {
}