package com.arda.cihangir.ozu.csc393.car_rental.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ExtraService {

    @Id
    private String code;

    private String name;
    private double price;

    @ManyToMany(mappedBy = "extras", fetch = FetchType.LAZY)
    private List<Reservation> reservations;
}