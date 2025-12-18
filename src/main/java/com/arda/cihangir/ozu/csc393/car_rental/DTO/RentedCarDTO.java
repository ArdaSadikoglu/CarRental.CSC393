package com.arda.cihangir.ozu.csc393.car_rental.DTO;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class RentedCarDTO {
    private String brand;
    private String model;
    private String carType; // category
    private String transmissionType;
    private String barcodeNumber;

    private String reservationNumber;
    private String memberName;

    private LocalDateTime dropOffDateTime;
    private String dropOffLocation;

    private long reservationDayCount;
}