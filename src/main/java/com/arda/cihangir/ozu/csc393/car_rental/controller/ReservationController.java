package com.arda.cihangir.ozu.csc393.car_rental.controller;

import com.arda.cihangir.ozu.csc393.car_rental.DTO.MakeReservationRequest;
import com.arda.cihangir.ozu.csc393.car_rental.DTO.MakeReservationResponse;
import com.arda.cihangir.ozu.csc393.car_rental.DTO.RentedCarDTO;
import com.arda.cihangir.ozu.csc393.car_rental.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    // 2) Make a reservation
    @PostMapping("/make")
    public ResponseEntity<?> make(@RequestBody MakeReservationRequest req) {
        try {
            MakeReservationResponse resp = reservationService.makeReservation(req);
            return ResponseEntity.ok(resp);
        } catch (IllegalStateException e) {
            if ("CAR_NOT_AVAILABLE".equals(e.getMessage())) {
                return ResponseEntity.status(206).body("Selected car is not available");
            }
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    // 3) Get all rented cars
    @GetMapping("/rented")
    public ResponseEntity<?> rented() {
        List<RentedCarDTO> list = reservationService.getAllRentedCars();
        if (list.isEmpty()) return ResponseEntity.status(404).build();
        return ResponseEntity.ok(list);
    }

    // 4) Add extra to reservation
    @PostMapping("/{reservationNumber}/extras/{extraCode}")
    public ResponseEntity<?> addExtra(@PathVariable String reservationNumber, @PathVariable String extraCode) {
        try {
            boolean ok = reservationService.addExtraToReservation(reservationNumber, extraCode);
            return ok ? ResponseEntity.ok(true) : ResponseEntity.ok(false);
        } catch (IllegalArgumentException e) {
            if ("EXTRA_NOT_FOUND".equals(e.getMessage())) return ResponseEntity.status(404).body("Extra not found");
            return ResponseEntity.internalServerError().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    // 5) Return car
    @PostMapping("/{reservationNumber}/return")
    public ResponseEntity<?> returnCar(@PathVariable String reservationNumber) {
        try {
            boolean ok = reservationService.returnCar(reservationNumber);
            if (!ok) return ResponseEntity.status(404).build();
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    // 6) Cancel reservation
    @PostMapping("/{reservationNumber}/cancel")
    public ResponseEntity<?> cancel(@PathVariable String reservationNumber) {
        try {
            boolean ok = reservationService.cancelReservation(reservationNumber);
            if (!ok) return ResponseEntity.status(404).build();
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    // 8) Delete reservation
    @DeleteMapping("/{reservationNumber}")
    public ResponseEntity<?> delete(@PathVariable String reservationNumber) {
        try {
            boolean ok = reservationService.deleteReservation(reservationNumber);
            if (!ok) return ResponseEntity.status(404).build();
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}