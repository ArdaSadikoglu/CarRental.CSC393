package com.arda.cihangir.ozu.csc393.car_rental.service;

import jakarta.persistence.Id;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

import com.arda.cihangir.ozu.csc393.car_rental.model.Car;
import com.arda.cihangir.ozu.csc393.car_rental.repository.CarRepository;

@Service
@RequiredArgsConstructor
public class CarService {

    private final CarRepository carRepository;

    public List<Car> getAll() {
        return carRepository.findAll();
    }

    public Car getById(String id) {
        return carRepository.findById(id).orElse(null);
    }

    public Car create(Car car) {
        return carRepository.save(car);
    }
}