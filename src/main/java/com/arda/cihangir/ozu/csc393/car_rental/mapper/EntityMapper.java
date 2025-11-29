package com.arda.cihangir.ozu.csc393.car_rental.mapper;

import com.arda.cihangir.ozu.csc393.car_rental.DTO.*;
import com.arda.cihangir.ozu.csc393.car_rental.model.*;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class EntityMapper {

    //  CAR → CarDTO
    public CarDTO toCarDTO(Car car) {
        CarDTO dto = new CarDTO();

        dto.setBarcodeNumber(car.getBarcodeNumber());
        dto.setLicensePlate(car.getLicensePlate());
        dto.setBrand(car.getBrand());
        dto.setModel(car.getModel());
        dto.setNumberOfSeats(car.getNumberOfSeats());
        dto.setDailyPrice(car.getDailyPrice());
        dto.setMileage(car.getMileage());

        // Enums → String (DTO tarafında enum tutmuyoruz)
        dto.setStatus(car.getStatus().name());
        dto.setTransmissionType(car.getTransmissionType().name());
        dto.setCategory(car.getCategory().name());

        if (car.getLocation() != null) {
            dto.setLocationCode(car.getLocation().getCode());
        }

        return dto;
    }


    //  ExtraService → ExtraServiceDTO
    public ExtraServiceDTO toExtraServiceDTO(ExtraService e) {
        ExtraServiceDTO dto = new ExtraServiceDTO();

        dto.setCode(Long.valueOf(e.getCode()));
        dto.setName(e.getName());
        dto.setPrice(e.getPrice());

        return dto;
    }


    // Member → MemberDTO
    public MemberDTO toMemberDTO(Member m) {
        MemberDTO dto = new MemberDTO();

        dto.setCode(m.getId());
        dto.setFullName(m.getName());
        dto.setEmail(m.getEmail());
        dto.setPhone(m.getPhone());


        return dto;
    }


    //  Location → LocationDTO
    public LocationDTO toLocationDTO(Location l) {
        LocationDTO dto = new LocationDTO();

        dto.setCode(l.getCode());
        dto.setName(l.getName());

        return dto;
    }


    //  Reservation → ReservationDTO
    public ReservationDTO toReservationDTO(Reservation r) {
        ReservationDTO dto = new ReservationDTO();

        dto.setReservationNumber(r.getReservationNumber());
        dto.setCreationDate(r.getCreationDate());
        dto.setPickUpDate(r.getPickUpDate());
        dto.setDropOffDate(r.getDropOffDate());
        dto.setReturnDate(r.getReturnDate());
        dto.setStatus(r.getStatus());

        // Relations: sadece ID/Code gönderiyoruz
        if (r.getPickUpLocation() != null) {
            dto.setPickUpLocationCode(r.getPickUpLocation().getCode());
        }

        if (r.getDropOffLocation() != null) {
            dto.setDropOffLocationCode(r.getDropOffLocation().getCode());
        }

        if (r.getMember() != null) {
            dto.setMemberId(r.getMember().getId());
        }

        if (r.getCar() != null) {
            dto.setCarBarcodeNumber(r.getCar().getBarcodeNumber());
        }

        if (r.getExtras() != null) {
            dto.setExtraCodes(
                    r.getExtras()
                            .stream()
                            .map(ExtraService::getCode)
                            .collect(Collectors.toList())
            );
        }

        return dto;
    }

}