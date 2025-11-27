package com.arda.cihangir.ozu.csc393.car_rental.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import com.arda.cihangir.ozu.csc393.car_rental.model.Car;
import com.arda.cihangir.ozu.csc393.car_rental.service.CarService;

@RestController
@RequestMapping("/api/cars")
@RequiredArgsConstructor
public class CarController {

    private final CarService carService;

    @GetMapping
    public List<Car> getAllCars() {
        return carService.getAll();
    }

    @GetMapping("/{id}")
    public Car getById(@PathVariable String id) {
        return carService.getById(id);
    }

    @PostMapping
    public Car createCar(@RequestBody Car car) {
        return carService.create(car);
    }
}