package com.arda.cihangir.ozu.csc393.car_rental.service;

import com.arda.cihangir.ozu.csc393.car_rental.model.Reservation;
import com.arda.cihangir.ozu.csc393.car_rental.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;

    public List<Reservation> getAll() {
        return reservationRepository.findAll();
    }

    public Reservation create(Reservation r) {
        return reservationRepository.save(r);
    }
    public Reservation getById(String reservationNumber) {
        return reservationRepository.findById(reservationNumber).orElse(null);
    }
}