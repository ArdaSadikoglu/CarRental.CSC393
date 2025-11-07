package com.arda.cihangir.ozu.csc393.car_rental.model;

import java.util.List;

public class Car {
    private String barcodeNumber;
    private String licensePlate;
    private String brand;
    private String model;
    private int numberOfSeats;
    private double dailyPrice;
    private double mileage;
    private CarStatus status;
    private TransmissionType transmissionType;
    private CarCategory category;
    private Location location;
    private List<Reservation> reservations;
}