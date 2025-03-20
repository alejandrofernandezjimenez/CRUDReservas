package es.radiantsuites.crudreservas.service;

import es.radiantsuites.crudreservas.entity.Cliente;
import es.radiantsuites.crudreservas.entity.Habitacion;
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

    public Reserva crearReserva(Reserva reserva) {
        if (reserva.getCheckOut() != null && reserva.getCheckIn() != null && reserva.getCheckOut().isBefore(reserva.getCheckIn())) {
            throw new IllegalArgumentException("La fecha de check-out debe ser posterior a la fecha de check-in");
        }
        Cliente cliente = reserva.getCliente();
        Habitacion habitacion = reserva.getHabitacion();
        if (cliente == null || habitacion == null) {
            throw new IllegalArgumentException("Cliente y habitación son obligatorios");
        }

        // Verificar si la habitación está disponible en las fechas solicitadas
        if (reserva.getCheckIn() != null && reserva.getCheckOut() != null) {
            boolean isRoomBooked = reservaRepository.existsByHabitacionAndCheckInLessThanEqualAndCheckOutGreaterThanEqual(
                    habitacion, reserva.getCheckOut(), reserva.getCheckIn());
            if (isRoomBooked) {
                throw new IllegalStateException("La habitación no está disponible en las fechas solicitadas");
            }
        }
        return reservaRepository.save(reserva);
    }

    public Optional<Reserva> obtenerReservaPorId(Integer id) {
        return reservaRepository.findById(id);
    }

    public Reserva actualizarReserva(Integer id, Reserva reservaActualizada) {
        return reservaRepository.findById(id).map(reserva -> {
            reserva.setCheckIn(reservaActualizada.getCheckIn());
            reserva.setCheckOut(reservaActualizada.getCheckOut());
            reserva.setCliente(reservaActualizada.getCliente());
            reserva.setHabitacion(reservaActualizada.getHabitacion());
            if (reserva.getCheckOut() != null && reserva.getCheckIn() != null && reserva.getCheckOut().isBefore(reserva.getCheckIn())) {
                throw new IllegalArgumentException("La fecha de check-out debe ser posterior a la fecha de check-in");
            }

            // TODO: Agregar verificación de disponibilidad aquí también (lo haremos después si quieres)

            return reservaRepository.save(reserva);
        }).orElseThrow(() -> new IllegalArgumentException("Reserva no encontrada con ID: " + id));
    }

    public void eliminarReserva(Integer id) {
        if (!reservaRepository.existsById(id)) {
            throw new IllegalArgumentException("Reserva no encontrada con ID: " + id);
        }
        reservaRepository.deleteById(id);
    }

    public List<Reserva> listarReservas() {
        return reservaRepository.findAll();
    }
}