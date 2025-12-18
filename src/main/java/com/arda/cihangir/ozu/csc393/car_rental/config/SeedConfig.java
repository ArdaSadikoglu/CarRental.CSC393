package com.arda.cihangir.ozu.csc393.car_rental.config;

import com.arda.cihangir.ozu.csc393.car_rental.model.*;
import com.arda.cihangir.ozu.csc393.car_rental.model.enums.*;
import com.arda.cihangir.ozu.csc393.car_rental.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SeedConfig {

    private final CarRepository carRepository;
    private final MemberRepository memberRepository;
    private final LocationRepository locationRepository;
    private final ExtraServiceRepository extraServiceRepository;
    private final ReservationRepository reservationRepository;

    @Bean
    CommandLineRunner seed() {
        return args -> seedIfEmpty();
    }

    @Transactional
    public void seedIfEmpty() {

        // 1) Locations
        if (locationRepository.count() == 0) {
            Location l1 = new Location();
            Location l2 = new Location();

            trySet(l1, "setName", "OZU Campus");
            trySet(l1, "setCity", "Istanbul");

            trySet(l2, "setName", "Sabiha Gokcen");
            trySet(l2, "setCity", "Istanbul");

            locationRepository.saveAll(List.of(l1, l2));
        }

        // 2) Members
        if (memberRepository.count() == 0) {
            Member m1 = new Member();
            Member m2 = new Member();

            trySet(m1, "setName", "Arda");
            trySet(m1, "setSurname", "Sadikoglu");
            trySet(m1, "setEmail", "arda@mail.com");

            trySet(m2, "setName", "Test");
            trySet(m2, "setSurname", "User");
            trySet(m2, "setEmail", "test@mail.com");

            memberRepository.saveAll(List.of(m1, m2));
        }

        // 3) Extra Services
        if (extraServiceRepository.count() == 0) {
            ExtraService e1 = new ExtraService();
            ExtraService e2 = new ExtraService();

            trySet(e1, "setName", "GPS");
            trySet(e1, "setPrice", 150.0);

            trySet(e2, "setName", "Child Seat");
            trySet(e2, "setPrice", 200.0);

            extraServiceRepository.saveAll(List.of(e1, e2));
        }

        // 4) Cars
        if (carRepository.count() == 0) {
            Car c1 = new Car();
            Car c2 = new Car();

            // Bu setter isimleri sende farklıysa sorun değil; trySet sessizce geçiyor.
            trySet(c1, "setPlate", "34ABC34");
            trySet(c1, "setBrand", "BMW");
            trySet(c1, "setModel", "320i");
            trySetEnum(c1, "setCategory", CarCategory.class, "SEDAN");
            trySetEnum(c1, "setTransmissionType", TransmissionType.class, "AUTOMATIC");
            trySetEnum(c1, "setStatus", CarStatus.class, "AVAILABLE");

            trySet(c2, "setPlate", "06XYZ06");
            trySet(c2, "setBrand", "Toyota");
            trySet(c2, "setModel", "Corolla");
            trySetEnum(c2, "setCategory", CarCategory.class, "HATCHBACK");
            trySetEnum(c2, "setTransmissionType", TransmissionType.class, "MANUAL");
            trySetEnum(c2, "setStatus", CarStatus.class, "AVAILABLE");

            carRepository.saveAll(List.of(c1, c2));
        }

        // 5) Reservations (opsiyonel ama iyi görünür)
        if (reservationRepository.count() == 0) {
            List<Car> cars = carRepository.findAll();
            List<Member> members = memberRepository.findAll();
            List<Location> locations = locationRepository.findAll();

            if (!cars.isEmpty() && !members.isEmpty() && !locations.isEmpty()) {
                Reservation r1 = new Reservation();

                // reservationNumber / id
                trySet(r1, "setReservationNumber", "R-0001");
                trySet(r1, "setId", "R-0001"); // sende id ismi böyleyse

                // ilişkiler (sende alan adları farklı olabilir)
                trySet(r1, "setCar", cars.get(0));
                trySet(r1, "setMember", members.get(0));
                trySet(r1, "setPickupLocation", locations.get(0));
                trySet(r1, "setDropoffLocation", locations.get(1));

                // tarih alanları (LocalDate ise)
                trySet(r1, "setStartDate", LocalDate.now().plusDays(1));
                trySet(r1, "setEndDate", LocalDate.now().plusDays(3));

                // status
                trySetEnum(r1, "setStatus", ReservationStatus.class, "ACTIVE");

                reservationRepository.save(r1);
            }
        }
    }

    private void trySet(Object target, String methodName, Object arg) {
        try {
            for (Method m : target.getClass().getMethods()) {
                if (m.getName().equals(methodName) && m.getParameterCount() == 1) {
                    Class<?> p = m.getParameterTypes()[0];
                    Object coerced = coerce(arg, p);
                    if (coerced == null && p.isPrimitive()) return;
                    m.invoke(target, coerced);
                    return;
                }
            }
        } catch (Exception ignored) { }
    }

    private <E extends Enum<E>> void trySetEnum(Object target, String methodName, Class<E> enumType, String value) {
        try {
            E enumVal = Enum.valueOf(enumType, value);
            trySet(target, methodName, enumVal);
        } catch (Exception ignored) { }
    }

    private Object coerce(Object value, Class<?> targetType) {
        if (value == null) return null;
        if (targetType.isInstance(value)) return value;

        if (targetType == String.class) return String.valueOf(value);

        if ((targetType == double.class || targetType == Double.class) && value instanceof Number n) return n.doubleValue();
        if ((targetType == int.class || targetType == Integer.class) && value instanceof Number n) return n.intValue();
        if ((targetType == long.class || targetType == Long.class) && value instanceof Number n) return n.longValue();
        if ((targetType == boolean.class || targetType == Boolean.class) && value instanceof Boolean b) return b;

        return value; // çoğu entity referansı zaten uyumlu gelir
    }
}