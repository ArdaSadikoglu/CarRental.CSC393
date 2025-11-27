package com.arda.cihangir.ozu.csc393.car_rental.controller;

import com.arda.cihangir.ozu.csc393.car_rental.model.Location;


import com.arda.cihangir.ozu.csc393.car_rental.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/locations")
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;

    @GetMapping
    public List<Location> getAllLocations() {
        return locationService.getAll();
    }
}