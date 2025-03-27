package es.radiantsuites.crudreservas.repository;

import es.radiantsuites.crudreservas.dto.Cliente;
import es.radiantsuites.crudreservas.entity.Habitacion;
import es.radiantsuites.crudreservas.entity.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Integer> {

    // Verificar si una habitación está reservada en un rango de fechas
    boolean existsByHabitacionAndCheckInLessThanEqualAndCheckOutGreaterThanEqual(Habitacion habitacion, LocalDate checkOut, LocalDate checkIn);
}