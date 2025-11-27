package com.arda.cihangir.ozu.csc393.car_rental.controller;

import com.arda.cihangir.ozu.csc393.car_rental.model.Reservation;
import com.arda.cihangir.ozu.csc393.car_rental.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @GetMapping
    public List<Reservation> getAllReservations() {
        return reservationService.getAll();
    }

    @PostMapping
    public Reservation createReservation(@RequestBody Reservation r) {
        return reservationService.create(r);
    }
}