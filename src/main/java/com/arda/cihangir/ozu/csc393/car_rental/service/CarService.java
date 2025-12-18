package com.arda.cihangir.ozu.csc393.car_rental.service;

import com.arda.cihangir.ozu.csc393.car_rental.DTO.CarDTO;
import com.arda.cihangir.ozu.csc393.car_rental.DTO.SearchAvailableCarsRequest;
import com.arda.cihangir.ozu.csc393.car_rental.mapper.EntityMapper;
import com.arda.cihangir.ozu.csc393.car_rental.model.enums.*;
import com.arda.cihangir.ozu.csc393.car_rental.repository.CarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CarService {

    private final CarRepository carRepository;
    private final EntityMapper mapper;

    public List<CarDTO> searchAvailableCars(SearchAvailableCarsRequest req) {

        if (req.getPickUpDate() == null || req.getDropOffDate() == null) {
            throw new IllegalArgumentException("pickUpDate & dropOffDate required");
        }

        if (req.getPickupLocationCode() == null || req.getPickupLocationCode().isBlank()) {
            throw new IllegalArgumentException("pickupLocationCode required");
        }

        CarCategory category = parseEnum(req.getCategory(), CarCategory.class);
        TransmissionType transmissionType = parseEnum(req.getTransmissionType(), TransmissionType.class);

        return carRepository.searchAvailable(
                CarStatus.AVAILABLE,
                category,
                transmissionType,
                req.getMinDailyPrice(),
                req.getMaxDailyPrice(),
                req.getPickUpDate(),
                req.getDropOffDate(),
                req.getNumberOfSeats(),
                req.getPickupLocationCode()
        ).stream().map(mapper::toCarDTO).toList();
    }

    private <T extends Enum<T>> T parseEnum(String value, Class<T> enumClass) {
        if (value == null || value.isBlank() || value.equalsIgnoreCase("string")) {
            return null;
        }
        try {
            return Enum.valueOf(enumClass, value.toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException(
                    "Invalid value for " + enumClass.getSimpleName() + ": " + value
            );
        }
    }
}