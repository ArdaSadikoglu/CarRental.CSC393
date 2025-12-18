package com.arda.cihangir.ozu.csc393.car_rental.DTO;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class MakeReservationRequest {
    private String carBarcodeNumber;
    private LocalDateTime pickUpDateTime;
    private LocalDateTime dropOffDateTime;
    private Long memberId;
    private String pickUpLocationCode;
    private String dropOffLocationCode;
    private List<String> extraCodes; // optional
}