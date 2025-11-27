package com.arda.cihangir.ozu.csc393.car_rental.controller;

import com.arda.cihangir.ozu.csc393.car_rental.model.ExtraService;
import com.arda.cihangir.ozu.csc393.car_rental.service.ExtraServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/extras")
@RequiredArgsConstructor
public class ExtraServiceController {

    private final ExtraServiceService extraServiceService;

    @GetMapping
    public List<ExtraService> getAllExtras() {
        return extraServiceService.getAll();
    }
}