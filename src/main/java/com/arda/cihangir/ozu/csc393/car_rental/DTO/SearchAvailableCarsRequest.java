package com.arda.cihangir.ozu.csc393.car_rental.DTO;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SearchAvailableCarsRequest {
    private String category;              // optional
    private String transmissionType;      // optional
    private Double minDailyPrice;         // optional
    private Double maxDailyPrice;         // optional
    private LocalDateTime pickUpDate;     // required
    private LocalDateTime dropOffDate;    // required
    private Integer numberOfSeats;        // optional
    private String pickupLocationCode;    // required (doküman böyle istiyor)
}