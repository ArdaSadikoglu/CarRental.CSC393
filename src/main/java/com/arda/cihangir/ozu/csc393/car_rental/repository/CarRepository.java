package com.arda.cihangir.ozu.csc393.car_rental.repository;

import com.arda.cihangir.ozu.csc393.car_rental.model.Car;
import com.arda.cihangir.ozu.csc393.car_rental.model.enums.CarCategory;
import com.arda.cihangir.ozu.csc393.car_rental.model.enums.CarStatus;
import com.arda.cihangir.ozu.csc393.car_rental.model.enums.TransmissionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface CarRepository extends JpaRepository<Car, String> {

    @Query("""
      select c from Car c
      where c.status = :available
        and (:category is null or c.category = :category)
        and (:transmission is null or c.transmissionType = :transmission)
        and (:minPrice is null or c.dailyPrice >= :minPrice)
        and (:maxPrice is null or c.dailyPrice <= :maxPrice)
        and (:seats is null or c.numberOfSeats = :seats)
        and (:pickupLocationCode is null or c.location.code = :pickupLocationCode)
        and not exists (
            select 1 from Reservation r
            where r.car = c
              and r.status = com.arda.cihangir.ozu.csc393.car_rental.model.enums.ReservationStatus.ACTIVE
              and (r.pickUpDate < :dropOff and r.dropOffDate > :pickUp)
        )
    """)
    List<Car> searchAvailable(
            @Param("available") CarStatus available,
            @Param("category") CarCategory category,
            @Param("transmission") TransmissionType transmission,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice,
            @Param("pickUp") LocalDateTime pickUp,
            @Param("dropOff") LocalDateTime dropOff,
            @Param("seats") Integer seats,
            @Param("pickupLocationCode") String pickupLocationCode
    );
}