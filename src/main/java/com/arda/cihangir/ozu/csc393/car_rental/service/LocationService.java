package com.arda.cihangir.ozu.csc393.car_rental.service;

import com.arda.cihangir.ozu.csc393.car_rental.model.Location;
import com.arda.cihangir.ozu.csc393.car_rental.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;

    public List<Location> getAll() {
        return locationRepository.findAll();
    }
}