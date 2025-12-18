package com.arda.cihangir.ozu.csc393.car_rental.controller;

import com.arda.cihangir.ozu.csc393.car_rental.DTO.CarDTO;
import com.arda.cihangir.ozu.csc393.car_rental.DTO.SearchAvailableCarsRequest;
import com.arda.cihangir.ozu.csc393.car_rental.repository.ReservationRepository;
import com.arda.cihangir.ozu.csc393.car_rental.service.CarService;
import com.arda.cihangir.ozu.csc393.car_rental.repository.CarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cars")
@RequiredArgsConstructor
public class CarController {

    private final CarService carService;
    private final CarRepository carRepository;
    private final ReservationRepository reservationRepository;

    // 1) Search available cars
    @PostMapping("/search-available")
    public ResponseEntity<?> search(@RequestBody SearchAvailableCarsRequest req) {
        List<CarDTO> list = carService.searchAvailableCars(req);
        if (list.isEmpty()) return ResponseEntity.status(404).build();
        return ResponseEntity.ok(list);
    }

    // 7) Delete a car (used in reservation ise 406)
    @DeleteMapping("/{barcodeNumber}")
    public ResponseEntity<?> delete(@PathVariable String barcodeNumber) {
        var car = carRepository.findById(barcodeNumber).orElse(null);
        if (car == null) return ResponseEntity.status(404).build();

        boolean used = (car.getReservations() != null && !car.getReservations().isEmpty());
        if (used) return ResponseEntity.status(406).body(false);

        carRepository.deleteById(barcodeNumber);
        return ResponseEntity.ok(true);
    }
}