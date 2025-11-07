package com.arda.cihangir.ozu.csc393.car_rental.model;

import java.time.LocalDateTime;
import java.util.List;

public class Reservation {
    private String reservationNumber;
    private LocalDateTime creationDate;
    private LocalDateTime pickUpDate;
    private LocalDateTime dropOffDate;
    private LocalDateTime returnDate;
    private ReservationStatus status;
    private Location pickUpLocation;
    private Location dropOffLocation;
    private Member member;
    private Car car;
    private List<ExtraService> extras;
}