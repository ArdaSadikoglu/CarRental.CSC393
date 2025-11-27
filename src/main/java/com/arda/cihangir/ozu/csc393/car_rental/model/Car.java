package com.arda.cihangir.ozu.csc393.car_rental.model;

import com.arda.cihangir.ozu.csc393.car_rental.model.enums.CarCategory;
import com.arda.cihangir.ozu.csc393.car_rental.model.enums.CarStatus;
import com.arda.cihangir.ozu.csc393.car_rental.model.enums.TransmissionType;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Car {

    @Id
    private String barcodeNumber;

    private String licensePlate;
    private String brand;
    private String model;
    private int numberOfSeats;
    private double dailyPrice;
    private double mileage;

    @Enumerated(EnumType.STRING)
    private CarStatus status;

    @Enumerated(EnumType.STRING)
    private TransmissionType transmissionType;

    @Enumerated(EnumType.STRING)
    private CarCategory category;

    @ManyToOne(fetch = FetchType.LAZY)
    private Location location;

    @OneToMany(mappedBy = "car", fetch = FetchType.LAZY)
    private List<Reservation> reservations;
}