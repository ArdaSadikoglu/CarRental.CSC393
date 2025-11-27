package com.arda.cihangir.ozu.csc393.car_rental.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String address;
    private String email;
    private String phone;
    private String drivingLicenseNumber;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private List<Reservation> reservations;
}