package es.radiantsuites.crudreservas.repository;

import es.radiantsuites.crudreservas.entity.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Integer> {

    // Buscar todas las reservas de un cliente específico
    List<Reserva> findByIdCliente(Integer idCliente);

    // Buscar todas las reservas de una habitación específica
    List<Reserva> findByIdHabitacion(Integer idHabitacion);

    // Buscar reservas dentro de un rango de fechas de check-in
    List<Reserva> findByCheckInBetween(LocalDate startDate, LocalDate endDate);

    // Buscar una reserva específica por id_cliente e id_habitacion
    Optional<Reserva> findByIdClienteAndIdHabitacion(Integer idCliente, Integer idHabitacion);

    // Verificar si existe una reserva para un cliente y habitación específicos
    boolean existsByIdClienteAndIdHabitacion(Integer idCliente, Integer idHabitacion);
}