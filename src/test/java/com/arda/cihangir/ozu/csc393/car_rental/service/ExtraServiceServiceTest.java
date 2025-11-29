package com.arda.cihangir.ozu.csc393.car_rental.service;

import com.arda.cihangir.ozu.csc393.car_rental.model.ExtraService;
import com.arda.cihangir.ozu.csc393.car_rental.repository.ExtraServiceRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class ExtraServiceServiceTest {

    @Autowired
    private ExtraServiceService extraServiceService;

    @Autowired
    private ExtraServiceRepository extraServiceRepository;

    @Test
    void testCreateAndFind() {

        ExtraService ex = new ExtraService(
                "GPS",
                "Navigation",
                100.0,
                null
        );

        extraServiceService.create(ex);

        ExtraService found = extraServiceService.getById("GPS");

        assertNotNull(found);
        assertEquals("Navigation", found.getName());
    }
}