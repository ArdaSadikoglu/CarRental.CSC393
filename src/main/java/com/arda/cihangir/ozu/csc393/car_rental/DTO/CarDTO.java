package com.arda.cihangir.ozu.csc393.car_rental.DTO;

import lombok.Data;

@Data
public class CarDTO {
    private String barcodeNumber;
    private String licensePlate;
    private String brand;
    private String model;
    private int numberOfSeats;
    private double dailyPrice;
    private double mileage;

    private String status;
    private String transmissionType;
    private String category;

    private String locationCode;
}