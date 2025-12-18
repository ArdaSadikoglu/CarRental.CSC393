package com.arda.cihangir.ozu.csc393.car_rental.DTO;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class MakeReservationResponse {
    private String reservationNumber;
    private LocalDateTime pickUpDateTime;
    private LocalDateTime dropOffDateTime;

    private String pickUpLocationCode;
    private String pickUpLocationName;

    private String dropOffLocationCode;
    private String dropOffLocationName;

    private double totalAmount; // DB’ye yazma, response’ta hesapla

    private Long memberId;
    private String memberName;
}