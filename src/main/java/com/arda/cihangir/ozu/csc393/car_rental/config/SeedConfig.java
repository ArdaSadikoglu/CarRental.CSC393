package com.arda.cihangir.ozu.csc393.car_rental.config;

import com.arda.cihangir.ozu.csc393.car_rental.model.*;
import com.arda.cihangir.ozu.csc393.car_rental.model.enums.*;
import com.arda.cihangir.ozu.csc393.car_rental.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SeedConfig {

    private final CarRepository carRepository;
    private final MemberRepository memberRepository;
    private final LocationRepository locationRepository;
    private final ExtraServiceRepository extraServiceRepository;
    private final ReservationRepository reservationRepository;

    @Bean
    CommandLineRunner seed() {
        return args -> seedIfEmpty();
    }

    @Transactional
    public void seedIfEmpty() {
        if (locationRepository.count() == 0) {
            locationRepository.saveAll(List.of(
                    new Location("1", "İstanbul Airport", null, null),
                    new Location("2", "İstanbul Sabiha Gökçen Airport", null, null),
                    new Location("3", "İstanbul Kadıköy", null, null),
                    new Location("4", "İzmir City Center", null, null)
            ));
        }

        if (extraServiceRepository.count() == 0) {
            extraServiceRepository.saveAll(List.of(
                    new ExtraService("GPS", "GPS", 150.0, null),
                    new ExtraService("CHILD_SEAT", "Child Seat", 200.0, null),
                    new ExtraService("WIFI", "Wifi Router", 250.0, null)
            ));
        }

        if (memberRepository.count() == 0) {
            memberRepository.saveAll(List.of(
                    new Member(null, "Arda", "Adres 1", "arda@mail.com", "5551112233", "DL-123", null),
                    new Member(null, "Test User", "Adres 2", "test@mail.com", "5559998877", "DL-999", null)
            ));
        }

        if (carRepository.count() == 0) {
            Location l1 = locationRepository.findById("1").orElseThrow();
            Location l3 = locationRepository.findById("3").orElseThrow();

            carRepository.saveAll(List.of(
                    new Car("C-0001","34ABC34","BMW","320i",5,2500,12000,
                            CarStatus.AVAILABLE, TransmissionType.AUTOMATIC, CarCategory.LUXURY,
                            l1, null),
                    new Car("C-0002","06XYZ06","Toyota","Corolla",5,1200,45000,
                            CarStatus.AVAILABLE, TransmissionType.MANUAL, CarCategory.COMPACT,
                            l3, null)
            ));
        }

        // Opsiyonel örnek ACTIVE rezervasyon (rented-cars listesi için)
        if (reservationRepository.count() == 0) {
            Car car = carRepository.findById("C-0002").orElseThrow();
            Member member = memberRepository.findAll().get(0);
            Location pu = locationRepository.findById("3").orElseThrow();
            Location doo = locationRepository.findById("1").orElseThrow();

            Reservation r = new Reservation();
            r.setReservationNumber("12345678");
            r.setCreationDate(LocalDateTime.now());
            r.setPickUpDate(LocalDateTime.now().minusDays(1));
            r.setDropOffDate(LocalDateTime.now().plusDays(2));
            r.setStatus(ReservationStatus.ACTIVE);
            r.setPickUpLocation(pu);
            r.setDropOffLocation(doo);
            r.setMember(member);
            r.setCar(car);
            r.setExtras(List.of(extraServiceRepository.findById("GPS").orElseThrow()));

            reservationRepository.save(r);
        }
    }
}