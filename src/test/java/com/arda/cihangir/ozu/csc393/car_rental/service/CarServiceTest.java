package com.arda.cihangir.ozu.csc393.car_rental.service;

import com.arda.cihangir.ozu.csc393.car_rental.model.Car;
import com.arda.cihangir.ozu.csc393.car_rental.model.enums.CarCategory;
import com.arda.cihangir.ozu.csc393.car_rental.model.enums.CarStatus;
import com.arda.cihangir.ozu.csc393.car_rental.model.enums.TransmissionType;
import com.arda.cihangir.ozu.csc393.car_rental.repository.CarRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class CarServiceTest {

    @Autowired
    private CarService carService;

    @Autowired
    private CarRepository carRepository;

    @Test
    void testCreateAndFind() {

        Car car = new Car(
                "C001",
                "34ABC123",
                "BMW",
                "320i",
                5,
                1000.0,
                20000.0,
                CarStatus.AVAILABLE,
                TransmissionType.AUTOMATIC,
                CarCategory.LUXURY,
                null,
                null
        );

        carService.create(car);

        Car found = carService.getById("C001");

        assertNotNull(found);
        assertEquals("BMW", found.getBrand());
        assertEquals("320i", found.getModel());
        assertEquals(5, found.getNumberOfSeats());
    }
}