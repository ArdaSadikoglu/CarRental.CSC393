package com.arda.cihangir.ozu.csc393.car_rental.service;

import com.arda.cihangir.ozu.csc393.car_rental.model.Reservation;
import com.arda.cihangir.ozu.csc393.car_rental.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;

    public List<Reservation> getAll() {
        return reservationRepository.findAll();
    }

    public Reservation create(Reservation r) {
        return reservationRepository.save(r);
    }

    public Reservation getById(String reservationNumber) {
        return reservationRepository.findById(reservationNumber).orElse(null);
    }

    /**
     * Controller'ın "makeReservation" ihtiyacı için:
     * - basic validation
     * - duplicate id kontrolü
     * - (TODO) availability/date-overlap kontrolü
     */
    @Transactional
    public Reservation makeReservation(Reservation reservation) {
        if (reservation == null) {
            throw new IllegalArgumentException("Reservation cannot be null");
        }

        validateDatesIfPossible(reservation);

        String id = extractReservationNumber(reservation);
        if (id != null && !id.isBlank() && reservationRepository.existsById(id)) {
            throw new IllegalStateException("Reservation already exists with id: " + id);
        }

        // TODO: burada availability / date overlap kontrolünü ekle
        // Örn: carId + start/end üzerinden DB'de çakışan ACTIVE rezervasyon var mı?

        setStatusIfPossible(reservation, "ACTIVE");
        setCancelledIfPossible(reservation, false);

        return reservationRepository.save(reservation);
    }

    /**
     * Controller'ın "cancelReservation" ihtiyacı için:
     * - reservation'ı bul
     * - mümkünse status=CANCELLED veya cancelled=true yapıp save et
     * - modelde böyle alan yoksa son çare delete (yine de controller servis çağırmış olur)
     */
    @Transactional
    public Reservation cancelReservation(String reservationNumber) {
        if (reservationNumber == null || reservationNumber.isBlank()) {
            throw new IllegalArgumentException("reservationNumber cannot be null/blank");
        }

        Reservation reservation = reservationRepository.findById(reservationNumber)
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found: " + reservationNumber));

        boolean changed = false;
        changed |= setStatusIfPossible(reservation, "CANCELLED");
        changed |= setCancelledIfPossible(reservation, true);

        if (changed) {
            return reservationRepository.save(reservation);
        }

        // Modelde status/cancelled yoksa fallback:
        reservationRepository.deleteById(reservationNumber);
        return reservation;
    }

    // ---------------- helpers (model alan adların farklı olsa bile compile etsin diye reflection) ----------------

    private String extractReservationNumber(Reservation r) {
        // getter dene
        for (String getterName : new String[]{"getReservationNumber", "getReservationId", "getId"}) {
            Object v = invokeNoArg(r, getterName);
            if (v instanceof String s && !s.isBlank()) return s;
        }
        // field dene
        for (String fieldName : new String[]{"reservationNumber", "reservationId", "id"}) {
            Object v = readField(r, fieldName);
            if (v instanceof String s && !s.isBlank()) return s;
        }
        return null;
    }

    private void validateDatesIfPossible(Reservation r) {
        Object start = null;
        Object end = null;

        for (String getter : new String[]{"getStartDate", "getStart", "getPickupDate"}) {
            Object v = invokeNoArg(r, getter);
            if (v != null) { start = v; break; }
        }
        for (String getter : new String[]{"getEndDate", "getEnd", "getDropoffDate"}) {
            Object v = invokeNoArg(r, getter);
            if (v != null) { end = v; break; }
        }

        if (start != null && end != null && start instanceof Comparable && end instanceof Comparable) {
            @SuppressWarnings("unchecked")
            Comparable<Object> s = (Comparable<Object>) start;
            if (s.compareTo(end) > 0) {
                throw new IllegalArgumentException("Start date must be <= end date");
            }
        }
    }

    private boolean setStatusIfPossible(Reservation r, String statusName) {
        // setter dene
        for (String setterName : new String[]{"setStatus", "setReservationStatus"}) {
            if (invokeSingleArgSetter(r, setterName, statusName)) return true;
        }
        // field dene
        for (String fieldName : new String[]{"status", "reservationStatus"}) {
            if (writeFieldEnumOrString(r, fieldName, statusName)) return true;
        }
        return false;
    }

    private boolean setCancelledIfPossible(Reservation r, boolean cancelled) {
        // setter dene
        for (String setterName : new String[]{"setCancelled", "setCanceled", "setIsCancelled"}) {
            if (invokeSingleArgSetter(r, setterName, cancelled)) return true;
        }
        // field dene
        for (String fieldName : new String[]{"cancelled", "canceled", "isCancelled"}) {
            Object ok = writeFieldBoolean(r, fieldName, cancelled);
            if (ok != null) return true;
        }
        return false;
    }

    private Object invokeNoArg(Object target, String methodName) {
        try {
            Method m = target.getClass().getMethod(methodName);
            m.setAccessible(true);
            return m.invoke(target);
        } catch (Exception ignored) {
            return null;
        }
    }

    private boolean invokeSingleArgSetter(Object target, String methodName, Object value) {
        try {
            Method[] methods = target.getClass().getMethods();
            for (Method m : methods) {
                if (!m.getName().equals(methodName) || m.getParameterCount() != 1) continue;
                Class<?> paramType = m.getParameterTypes()[0];
                Object arg = coerceValue(value, paramType);
                if (arg == null && paramType.isPrimitive()) continue;
                m.setAccessible(true);
                m.invoke(target, arg);
                return true;
            }
            return false;
        } catch (Exception ignored) {
            return false;
        }
    }

    private Object readField(Object target, String fieldName) {
        try {
            Field f = target.getClass().getDeclaredField(fieldName);
            f.setAccessible(true);
            return f.get(target);
        } catch (Exception ignored) {
            return null;
        }
    }

    private boolean writeFieldEnumOrString(Object target, String fieldName, String value) {
        try {
            Field f = target.getClass().getDeclaredField(fieldName);
            f.setAccessible(true);
            Object coerced = coerceValue(value, f.getType());
            if (coerced == null && f.getType().isPrimitive()) return false;
            f.set(target, coerced);
            return true;
        } catch (Exception ignored) {
            return false;
        }
    }

    private Object writeFieldBoolean(Object target, String fieldName, boolean value) {
        try {
            Field f = target.getClass().getDeclaredField(fieldName);
            f.setAccessible(true);
            Object coerced = coerceValue(value, f.getType());
            if (coerced == null && f.getType().isPrimitive()) return null;
            f.set(target, coerced);
            return coerced;
        } catch (Exception ignored) {
            return null;
        }
    }

    private Object coerceValue(Object value, Class<?> targetType) {
        if (value == null) return null;

        if (targetType.isInstance(value)) return value;

        if (targetType == boolean.class || targetType == Boolean.class) {
            if (value instanceof Boolean b) return b;
            if (value instanceof String s) return Boolean.parseBoolean(s);
        }

        if (targetType == String.class) return String.valueOf(value);

        if (targetType.isEnum() && value instanceof String s) {
            @SuppressWarnings({"rawtypes", "unchecked"})
            Class<? extends Enum> enumType = (Class<? extends Enum>) targetType;
            try {
                return Enum.valueOf(enumType, s);
            } catch (Exception ignored) {

                return null;
            }
        }

        return null;
    }
}