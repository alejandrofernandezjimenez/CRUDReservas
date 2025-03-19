package es.radiantsuites.crudreservas.repository;

import es.radiantsuites.crudreservas.entity.Cliente;
import es.radiantsuites.crudreservas.entity.Habitacion;
import es.radiantsuites.crudreservas.entity.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Integer> {

    // Buscar todas las reservas de un cliente específico
    List<Reserva> findByCliente(Cliente cliente);

    // Buscar todas las reservas de una habitación específica
    List<Reserva> findByHabitacion(Habitacion habitacion);

    // Buscar reservas dentro de un rango de fechas de check-in
    List<Reserva> findByCheckInBetween(LocalDate startDate, LocalDate endDate);

    // Buscar una reserva específica por cliente y habitación
    Optional<Reserva> findByClienteAndHabitacion(Cliente cliente, Habitacion habitacion);

    // Verificar si existe una reserva para un cliente y habitación específicos
    boolean existsByClienteAndHabitacion(Cliente cliente, Habitacion habitacion);
}