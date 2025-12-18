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

        CarCategory category = (req.getCategory() == null || req.getCategory().isBlank())
                ? null : CarCategory.valueOf(req.getCategory());

        TransmissionType tt = (req.getTransmissionType() == null || req.getTransmissionType().isBlank())
                ? null : TransmissionType.valueOf(req.getTransmissionType());

        return carRepository.searchAvailable(
                CarStatus.AVAILABLE,
                category,
                tt,
                req.getMinDailyPrice(),
                req.getMaxDailyPrice(),
                req.getPickUpDate(),
                req.getDropOffDate(),
                req.getNumberOfSeats(),
                req.getPickupLocationCode()
        ).stream().map(mapper::toCarDTO).toList();
    }
}