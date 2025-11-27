package com.arda.cihangir.ozu.csc393.car_rental.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Location {

    @Id
    private String code;

    private String name;

    @OneToMany(mappedBy = "pickUpLocation", fetch = FetchType.LAZY)
    private List<Reservation> pickUps;

    @OneToMany(mappedBy = "dropOffLocation", fetch = FetchType.LAZY)
    private List<Reservation> dropOffs;
}