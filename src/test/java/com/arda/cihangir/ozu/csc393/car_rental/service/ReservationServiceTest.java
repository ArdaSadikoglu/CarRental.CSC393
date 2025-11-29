package com.arda.cihangir.ozu.csc393.car_rental.service;

import com.arda.cihangir.ozu.csc393.car_rental.model.*;
import com.arda.cihangir.ozu.csc393.car_rental.model.enums.*;
import com.arda.cihangir.ozu.csc393.car_rental.repository.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class ReservationServiceTest {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CarRepository carRepository;

    @Test
    void testCreateAndFind() {

        Location loc = new Location("IST", "Istanbul", null, null);
        locationRepository.save(loc);

        Member m = new Member(
                null,
                "Arda",
                "Istanbul",
                "a@mail.com",
                "532",
                "DL1",
                null
        );
        memberRepository.save(m);

        Car car = new Car(
                "B001",
                "34PLT",
                "Renault",
                "Clio",
                5,
                400.0,
                10000.0,
                CarStatus.AVAILABLE,
                TransmissionType.AUTOMATIC,
                CarCategory.SUV,
                loc,
                null
        );
        carRepository.save(car);

        Reservation r = new Reservation(
                "R001",
                LocalDateTime.now(),
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(2),
                null,
                ReservationStatus.ACTIVE,
                loc,
                loc,
                m,
                car,
                null
        );

        reservationService.create(r);

        Reservation found = reservationService.getById("R001");

        assertNotNull(found);
        assertEquals("R001", found.getReservationNumber());
        assertEquals("Arda", found.getMember().getName());
    }
}