package com.arda.cihangir.ozu.csc393.car_rental.DTO;

import com.arda.cihangir.ozu.csc393.car_rental.model.enums.ReservationStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ReservationDTO {
    private String reservationNumber;

    private LocalDateTime creationDate;
    private LocalDateTime pickUpDate;
    private LocalDateTime dropOffDate;
    private LocalDateTime returnDate;

    private ReservationStatus status;

    private String pickUpLocationCode;
    private String dropOffLocationCode;

    private Long memberId;
    private String carBarcodeNumber;

    private List<String> extraCodes;
}