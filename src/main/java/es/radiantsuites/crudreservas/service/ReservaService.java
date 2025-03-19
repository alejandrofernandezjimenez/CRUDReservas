package es.radiantsuites.crudreservas.service;

import es.radiantsuites.crudreservas.entity.Reserva;
import es.radiantsuites.crudreservas.repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;

    // Crear una nueva reserva
    public Reserva crearReserva(Reserva reserva) {
        // Validación: check-out debe ser posterior a check-in
        if (reserva.getCheckOut() != null && reserva.getCheckIn() != null && reserva.getCheckOut().isBefore(reserva.getCheckIn())) {
            throw new IllegalArgumentException("La fecha de check-out debe ser posterior a la fecha de check-in");
        }
        // Verificar si ya existe una reserva para el mismo cliente y habitación
        if (reservaRepository.existsByIdClienteAndIdHabitacion(reserva.getIdCliente(), reserva.getIdHabitacion())) {
            throw new IllegalStateException("Ya existe una reserva para este cliente y habitación");
        }
        return reservaRepository.save(reserva);
    }

    // Obtener una reserva por ID
    public Optional<Reserva> obtenerReservaPorId(Integer id) {
        return reservaRepository.findById(id);
    }

    // Actualizar una reserva existente
    public Reserva actualizarReserva(Integer id, Reserva reservaActualizada) {
        return reservaRepository.findById(id).map(reserva -> {
            reserva.setCheckIn(reservaActualizada.getCheckIn());
            reserva.setCheckOut(reservaActualizada.getCheckOut());
            reserva.setIdCliente(reservaActualizada.getIdCliente());
            reserva.setIdHabitacion(reservaActualizada.getIdHabitacion());
            // Validación de fechas
            if (reserva.getCheckOut() != null && reserva.getCheckIn() != null && reserva.getCheckOut().isBefore(reserva.getCheckIn())) {
                throw new IllegalArgumentException("La fecha de check-out debe ser posterior a la fecha de check-in");
            }
            return reservaRepository.save(reserva);
        }).orElseThrow(() -> new IllegalArgumentException("Reserva no encontrada con ID: " + id));
    }

    // Eliminar una reserva
    public void eliminarReserva(Integer id) {
        if (!reservaRepository.existsById(id)) {
            throw new IllegalArgumentException("Reserva no encontrada con ID: " + id);
        }
        reservaRepository.deleteById(id);
    }

    // Listar todas las reservas
    public List<Reserva> listarReservas() {
        return reservaRepository.findAll();
    }
}