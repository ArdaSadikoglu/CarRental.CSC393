package com.arda.cihangir.ozu.csc393.car_rental.repository;

import com.arda.cihangir.ozu.csc393.car_rental.model.Reservation;
import com.arda.cihangir.ozu.csc393.car_rental.model.enums.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, String> {

    @Query("""
        select (count(r) > 0) from Reservation r
        where r.car.barcodeNumber = :carBarcode
          and r.status = com.arda.cihangir.ozu.csc393.car_rental.model.enums.ReservationStatus.ACTIVE
          and (r.pickUpDate < :dropOff and r.dropOffDate > :pickUp)
    """)
    boolean existsActiveOverlap(@Param("carBarcode") String carBarcode,
                                @Param("pickUp") LocalDateTime pickUp,
                                @Param("dropOff") LocalDateTime dropOff);

    @Query("""
        select r from Reservation r
        where r.status = :status
          and :now between r.pickUpDate and r.dropOffDate
    """)
    List<Reservation> findRentedAt(@Param("status") ReservationStatus status,
                                   @Param("now") LocalDateTime now);
}