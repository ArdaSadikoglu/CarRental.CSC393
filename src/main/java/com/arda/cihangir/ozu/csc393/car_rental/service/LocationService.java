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
    public Location create(Location loc) {
        return locationRepository.save(loc);
    }

    public Location getById(String code) {
        return locationRepository.findById(code).orElse(null);
    }
}