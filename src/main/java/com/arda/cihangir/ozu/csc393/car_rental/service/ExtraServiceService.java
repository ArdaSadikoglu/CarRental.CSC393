package com.arda.cihangir.ozu.csc393.car_rental.service;

import com.arda.cihangir.ozu.csc393.car_rental.model.ExtraService;
import com.arda.cihangir.ozu.csc393.car_rental.repository.ExtraServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExtraServiceService {

    private final ExtraServiceRepository extraServiceRepository;

    // Zaten varsa dokunma, yoksa ekle:
    public ExtraService create(ExtraService extra) {   // ✅
        return extraServiceRepository.save(extra);
    }

    public ExtraService getById(String code) {         // ✅
        return extraServiceRepository.findById(code).orElse(null);
    }

    public List<ExtraService> getAll() {
        return extraServiceRepository.findAll();
    }
}