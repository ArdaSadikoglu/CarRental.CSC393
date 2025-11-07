package com.arda.cihangir.ozu.csc393.car_rental.model;

import java.util.List;

public class Member {
    private Long id;
    private String name;
    private String address;
    private String email;
    private String phone;
    private String drivingLicenseNumber;
    private List<Reservation> reservations;
}