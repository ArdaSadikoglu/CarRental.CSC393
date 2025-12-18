package com.arda.cihangir.ozu.csc393.car_rental.service;

import com.arda.cihangir.ozu.csc393.car_rental.DTO.MakeReservationRequest;
import com.arda.cihangir.ozu.csc393.car_rental.DTO.MakeReservationResponse;
import com.arda.cihangir.ozu.csc393.car_rental.DTO.RentedCarDTO;
import com.arda.cihangir.ozu.csc393.car_rental.model.*;
import com.arda.cihangir.ozu.csc393.car_rental.model.enums.CarStatus;
import com.arda.cihangir.ozu.csc393.car_rental.model.enums.ReservationStatus;
import com.arda.cihangir.ozu.csc393.car_rental.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final CarRepository carRepository;
    private final MemberRepository memberRepository;
    private final LocationRepository locationRepository;
    private final ExtraServiceRepository extraServiceRepository;

    private final SecureRandom rnd = new SecureRandom();

    @Transactional
    public MakeReservationResponse makeReservation(MakeReservationRequest req) {
        validateMakeReservation(req);

        Car car = carRepository.findById(req.getCarBarcodeNumber())
                .orElseThrow(() -> new IllegalArgumentException("Car not found"));

        if (car.getStatus() != CarStatus.AVAILABLE) {
            // doküman “not acceptable / 206” diyor; controller’da status döndüreceğiz
            throw new IllegalStateException("CAR_NOT_AVAILABLE");
        }

        boolean overlap = reservationRepository.existsActiveOverlap(
                car.getBarcodeNumber(),
                req.getPickUpDateTime(),
                req.getDropOffDateTime()
        );
        if (overlap) {
            throw new IllegalStateException("CAR_NOT_AVAILABLE");
        }

        Member member = memberRepository.findById(req.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));

        Location pu = locationRepository.findById(req.getPickUpLocationCode())
                .orElseThrow(() -> new IllegalArgumentException("Pickup location not found"));

        Location doo = locationRepository.findById(req.getDropOffLocationCode())
                .orElseThrow(() -> new IllegalArgumentException("Dropoff location not found"));

        List<ExtraService> extras = (req.getExtraCodes() == null || req.getExtraCodes().isEmpty())
                ? List.of()
                : extraServiceRepository.findAllById(req.getExtraCodes());

        // 8 digit unique reservation number
        String reservationNumber = generateUnique8Digits();

        Reservation r = new Reservation();
        r.setReservationNumber(reservationNumber);
        r.setCreationDate(LocalDateTime.now());
        r.setPickUpDate(req.getPickUpDateTime());
        r.setDropOffDate(req.getDropOffDateTime());
        r.setStatus(ReservationStatus.ACTIVE);
        r.setPickUpLocation(pu);
        r.setDropOffLocation(doo);
        r.setMember(member);
        r.setCar(car);
        r.setExtras(extras);

        reservationRepository.save(r);

        double totalAmount = calculateTotalAmount(r);

        return MakeReservationResponse.builder()
                .reservationNumber(reservationNumber)
                .pickUpDateTime(r.getPickUpDate())
                .dropOffDateTime(r.getDropOffDate())
                .pickUpLocationCode(pu.getCode())
                .pickUpLocationName(pu.getName())
                .dropOffLocationCode(doo.getCode())
                .dropOffLocationName(doo.getName())
                .totalAmount(totalAmount)
                .memberId(member.getId())
                .memberName(member.getName())
                .build();
    }

    @Transactional(readOnly = true)
    public List<RentedCarDTO> getAllRentedCars() {
        List<Reservation> active = reservationRepository.findRentedAt(ReservationStatus.ACTIVE, LocalDateTime.now());
        if (active.isEmpty()) return List.of();

        return active.stream().map(r -> {
            Car c = r.getCar();
            String dropLoc = r.getDropOffLocation() != null ? r.getDropOffLocation().getName() : null;
            long days = dayCount(r.getPickUpDate(), r.getDropOffDate());

            return RentedCarDTO.builder()
                    .brand(c.getBrand())
                    .model(c.getModel())
                    .carType(c.getCategory().name())
                    .transmissionType(c.getTransmissionType().name())
                    .barcodeNumber(c.getBarcodeNumber())
                    .reservationNumber(r.getReservationNumber())
                    .memberName(r.getMember().getName())
                    .dropOffDateTime(r.getDropOffDate())
                    .dropOffLocation(dropLoc)
                    .reservationDayCount(days)
                    .build();
        }).toList();
    }

    @Transactional
    public boolean addExtraToReservation(String reservationNumber, String extraCode) {
        Reservation r = reservationRepository.findById(reservationNumber).orElse(null);
        if (r == null) return false;

        ExtraService extra = extraServiceRepository.findById(extraCode).orElse(null);
        if (extra == null) throw new IllegalArgumentException("EXTRA_NOT_FOUND");

        if (r.getExtras() != null && r.getExtras().stream().anyMatch(e -> e.getCode().equals(extraCode))) {
            return false;
        }

        if (r.getExtras() == null) r.setExtras(new java.util.ArrayList<>());
        r.getExtras().add(extra);

        reservationRepository.save(r);
        return true;
    }

    @Transactional
    public boolean returnCar(String reservationNumber) {
        Reservation r = reservationRepository.findById(reservationNumber).orElse(null);
        if (r == null) return false;

        r.setStatus(ReservationStatus.COMPLETED);
        r.setReturnDate(LocalDateTime.now());

        // dropOffLocation farklıysa car.location güncelle
        if (r.getDropOffLocation() != null && r.getCar() != null) {
            Location target = r.getDropOffLocation();
            if (r.getCar().getLocation() == null || !target.getCode().equals(r.getCar().getLocation().getCode())) {
                r.getCar().setLocation(target);
                carRepository.save(r.getCar());
            }
        }

        reservationRepository.save(r);
        return true;
    }

    @Transactional
    public boolean cancelReservation(String reservationNumber) {
        Reservation r = reservationRepository.findById(reservationNumber).orElse(null);
        if (r == null) return false;

        r.setStatus(ReservationStatus.CANCELLED);
        reservationRepository.save(r);
        return true;
    }

    @Transactional
    public boolean deleteReservation(String reservationNumber) {
        Reservation r = reservationRepository.findById(reservationNumber).orElse(null);
        if (r == null) return false;

        // Cascade.Remove yasak: ilişkileri kopar
        r.setCar(null);
        r.setMember(null);
        r.setPickUpLocation(null);
        r.setDropOffLocation(null);
        r.setExtras(List.of());
        reservationRepository.save(r);

        reservationRepository.deleteById(reservationNumber);
        return true;
    }

    public double calculateTotalAmount(Reservation r) {
        long days = dayCount(r.getPickUpDate(), r.getDropOffDate());
        double base = days * r.getCar().getDailyPrice();
        double extraSum = (r.getExtras() == null) ? 0.0 : r.getExtras().stream().mapToDouble(ExtraService::getPrice).sum();
        return base + extraSum;
    }

    private long dayCount(LocalDateTime start, LocalDateTime end) {
        long days = Duration.between(start, end).toDays();
        return Math.max(1, days); // en az 1 gün say
    }

    private void validateMakeReservation(MakeReservationRequest req) {
        if (req == null) throw new IllegalArgumentException("Request is null");
        if (isBlank(req.getCarBarcodeNumber())) throw new IllegalArgumentException("carBarcodeNumber required");
        if (req.getPickUpDateTime() == null || req.getDropOffDateTime() == null) throw new IllegalArgumentException("dates required");
        if (req.getPickUpDateTime().isAfter(req.getDropOffDateTime())) throw new IllegalArgumentException("pickUp must be <= dropOff");
        if (req.getMemberId() == null) throw new IllegalArgumentException("memberId required");
        if (isBlank(req.getPickUpLocationCode()) || isBlank(req.getDropOffLocationCode())) throw new IllegalArgumentException("location codes required");
    }

    private String generateUnique8Digits() {
        String n;
        do {
            n = String.format("%08d", rnd.nextInt(100_000_000));
        } while (reservationRepository.existsById(n));
        return n;
    }

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}