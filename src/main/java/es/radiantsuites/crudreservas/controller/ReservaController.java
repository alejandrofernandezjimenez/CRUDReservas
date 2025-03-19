package es.radiantsuites.crudreservas.controller;

import es.radiantsuites.crudreservas.entity.Cliente;
import es.radiantsuites.crudreservas.entity.Habitacion;
import es.radiantsuites.crudreservas.entity.Reserva;
import es.radiantsuites.crudreservas.repository.ClienteRepository;
import es.radiantsuites.crudreservas.repository.HabitacionRepository;
import es.radiantsuites.crudreservas.service.ReservaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/reservas")
public class ReservaController {

    @Autowired
    private ReservaService reservaService;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private HabitacionRepository habitacionRepository;

    // DTO para recibir los datos de la solicitud
    public static class ReservaRequest {
        private LocalDate checkIn;
        private LocalDate checkOut;
        private Integer idCliente;
        private Integer idHabitacion;

        // Getters y Setters
        public LocalDate getCheckIn() {
            return checkIn;
        }

        public void setCheckIn(LocalDate checkIn) {
            this.checkIn = checkIn;
        }

        public LocalDate getCheckOut() {
            return checkOut;
        }

        public void setCheckOut(LocalDate checkOut) {
            this.checkOut = checkOut;
        }

        public Integer getIdCliente() {
            return idCliente;
        }

        public void setIdCliente(Integer idCliente) {
            this.idCliente = idCliente;
        }

        public Integer getIdHabitacion() {
            return idHabitacion;
        }

        public void setIdHabitacion(Integer idHabitacion) {
            this.idHabitacion = idHabitacion;
        }
    }

    @PostMapping("/crear")
    public ResponseEntity<Reserva> crearReserva(@RequestBody ReservaRequest request) {
        try {
            // Buscar el cliente y la habitación por sus IDs
            Cliente cliente = clienteRepository.findById(request.getIdCliente())
                    .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado con ID: " + request.getIdCliente()));
            Habitacion habitacion = habitacionRepository.findById(request.getIdHabitacion())
                    .orElseThrow(() -> new IllegalArgumentException("Habitación no encontrada con ID: " + request.getIdHabitacion()));
            System.out.println(cliente);
            System.out.println(habitacion);
            // Crear la reserva
            Reserva reserva = new Reserva();
            reserva.setCheckIn(request.getCheckIn());
            reserva.setCheckOut(request.getCheckOut());
            reserva.setCliente(cliente);
            reserva.setHabitacion(habitacion);
            System.out.println(reserva);

            Reserva nuevaReserva = reservaService.crearReserva(reserva);
            return ResponseEntity.ok(nuevaReserva);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reserva> obtenerReserva(@PathVariable Integer id) {
        return reservaService.obtenerReservaPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<Reserva> actualizarReserva(@PathVariable Integer id, @RequestBody ReservaRequest request) {
        try {
            // Buscar el cliente y la habitación por sus IDs
            Cliente cliente = clienteRepository.findById(request.getIdCliente())
                    .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado con ID: " + request.getIdCliente()));
            Habitacion habitacion = habitacionRepository.findById(request.getIdHabitacion())
                    .orElseThrow(() -> new IllegalArgumentException("Habitación no encontrada con ID: " + request.getIdHabitacion()));

            // Crear una reserva actualizada
            Reserva reservaActualizada = new Reserva();
            reservaActualizada.setCheckIn(request.getCheckIn());
            reservaActualizada.setCheckOut(request.getCheckOut());
            reservaActualizada.setCliente(cliente);
            reservaActualizada.setHabitacion(habitacion);

            Reserva reserva = reservaService.actualizarReserva(id, reservaActualizada);
            return ResponseEntity.ok(reserva);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminarReserva(@PathVariable Integer id) {
        try {
            reservaService.eliminarReserva(id);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/listar")
    public ResponseEntity<List<Reserva>> listarReservas() {
        List<Reserva> reservas = reservaService.listarReservas();
        return ResponseEntity.ok(reservas);
    }
}