package com.arda.cihangir.ozu.csc393.car_rental.DTO;

import lombok.Data;

@Data
public class MemberDTO {
    private Long code;
    private String fullName;
    private String email;
    private String phone;
}