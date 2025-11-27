package com.arda.cihangir.ozu.csc393.car_rental.model;

import com.arda.cihangir.ozu.csc393.car_rental.model.enums.ReservationStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Reservation {

    @Id
    private String reservationNumber;

    private LocalDateTime creationDate;
    private LocalDateTime pickUpDate;
    private LocalDateTime dropOffDate;
    private LocalDateTime returnDate;

    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    private Location pickUpLocation;

    @ManyToOne(fetch = FetchType.LAZY)
    private Location dropOffLocation;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    private Car car;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "reservation_extras",
            joinColumns = @JoinColumn(name = "reservation_id"),
            inverseJoinColumns = @JoinColumn(name = "extra_code")
    )
    private List<ExtraService> extras;
}