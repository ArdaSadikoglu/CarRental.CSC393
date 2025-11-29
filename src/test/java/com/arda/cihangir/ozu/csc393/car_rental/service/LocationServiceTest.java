package com.arda.cihangir.ozu.csc393.car_rental.service;

import com.arda.cihangir.ozu.csc393.car_rental.model.Location;
import com.arda.cihangir.ozu.csc393.car_rental.repository.LocationRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class LocationServiceTest {

    @Autowired
    private LocationService locationService;

    @Autowired
    private LocationRepository locationRepository;

    @Test
    void testCreateAndFind() {

        Location loc = new Location("IST", "Istanbul", null, null);

        locationService.create(loc);

        Location found = locationService.getById("IST");

        assertNotNull(found);
        assertEquals("Istanbul", found.getName());
    }
}
