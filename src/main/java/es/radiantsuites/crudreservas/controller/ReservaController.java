package es.radiantsuites.crudreservas.controller;

import es.radiantsuites.crudreservas.dto.Cliente;
import es.radiantsuites.crudreservas.dto.ErrorResponse;
import es.radiantsuites.crudreservas.dto.ReservaConCliente;
import es.radiantsuites.crudreservas.entity.Habitacion;
import es.radiantsuites.crudreservas.entity.Reserva;
import es.radiantsuites.crudreservas.repository.HabitacionRepository;
import es.radiantsuites.crudreservas.service.ClienteService;
import es.radiantsuites.crudreservas.service.ReservaService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/reservas")
public class ReservaController {

    @Autowired
    private ReservaService reservaService;

    @Autowired
    private ClienteService clienteService;

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
    public ResponseEntity<?> crearReserva(@RequestBody ReservaRequest request, HttpServletRequest httpRequest) {
        try {
            // Validar los campos de la solicitud
            if (request.getCheckIn() == null || request.getCheckOut() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Bad Request",
                                "Las fechas checkIn y checkOut son obligatorias", httpRequest.getRequestURI()));
            }
            if (request.getCheckOut().isBefore(request.getCheckIn())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Bad Request",
                                "La fecha de checkOut debe ser posterior a la de checkIn", httpRequest.getRequestURI()));
            }
            if (request.getIdCliente() == null || request.getIdHabitacion() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Bad Request",
                                "El ID del cliente y el ID de la habitación son obligatorios", httpRequest.getRequestURI()));
            }

            // Buscar el cliente y la habitación por sus IDs
            Cliente cliente = clienteService.obtenerClientePorId(request.getIdCliente());
            if (cliente == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        new ErrorResponse(HttpStatus.NOT_FOUND.value(), "Not Found",
                                "Cliente con ID " + request.getIdCliente() + " no encontrado", httpRequest.getRequestURI()));
            }

            Habitacion habitacion = habitacionRepository.findById(request.getIdHabitacion())
                    .orElse(null);
            if (habitacion == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        new ErrorResponse(HttpStatus.NOT_FOUND.value(), "Not Found",
                                "Habitación con ID " + request.getIdHabitacion() + " no encontrada", httpRequest.getRequestURI()));
            }

            // Crear la reserva
            Reserva reserva = new Reserva();
            reserva.setCheckIn(request.getCheckIn());
            reserva.setCheckOut(request.getCheckOut());
            reserva.setIdCliente(cliente.getIdCliente());
            reserva.setHabitacion(habitacion);

            Reserva nuevaReserva = reservaService.crearReserva(reserva);
            return ResponseEntity.ok(nuevaReserva);

        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(
                    new ErrorResponse(HttpStatus.SERVICE_UNAVAILABLE.value(), "Service Unavailable",
                            "No se pudo conectar con el microservicio de clientes: " + e.getMessage(), httpRequest.getRequestURI()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal Server Error",
                            "Error al crear la reserva: " + e.getMessage(), httpRequest.getRequestURI()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerReserva(@PathVariable Integer id, HttpServletRequest httpRequest) {
        try {
            Optional<Reserva> reservaOpt = reservaService.obtenerReservaPorId(id);
            if (reservaOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        new ErrorResponse(HttpStatus.NOT_FOUND.value(), "Not Found",
                                "Reserva con ID " + id + " no encontrada", httpRequest.getRequestURI()));
            }

            Reserva reserva = reservaOpt.get();
            Cliente cliente = clienteService.obtenerClientePorId(reserva.getIdCliente());
            if (cliente == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        new ErrorResponse(HttpStatus.NOT_FOUND.value(), "Not Found",
                                "Cliente con ID " + reserva.getIdCliente() + " no encontrado", httpRequest.getRequestURI()));
            }

            ReservaConCliente combinado = new ReservaConCliente(reserva, cliente);
            return ResponseEntity.ok(combinado);

        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(
                    new ErrorResponse(HttpStatus.SERVICE_UNAVAILABLE.value(), "Service Unavailable",
                            "No se pudo conectar con el microservicio de clientes: " + e.getMessage(), httpRequest.getRequestURI()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal Server Error",
                            "Error al obtener la reserva: " + e.getMessage(), httpRequest.getRequestURI()));
        }
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<?> actualizarReserva(@PathVariable Integer id, @RequestBody ReservaRequest request, HttpServletRequest httpRequest) {
        try {
            // Validar los campos de la solicitud
            if (request.getCheckIn() == null || request.getCheckOut() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Bad Request",
                                "Las fechas checkIn y checkOut son obligatorias", httpRequest.getRequestURI()));
            }
            if (request.getCheckOut().isBefore(request.getCheckIn())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Bad Request",
                                "La fecha de checkOut debe ser posterior a la de checkIn", httpRequest.getRequestURI()));
            }
            if (request.getIdCliente() == null || request.getIdHabitacion() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Bad Request",
                                "El ID del cliente y el ID de la habitación son obligatorios", httpRequest.getRequestURI()));
            }

            // Verificar si la reserva existe
            Optional<Reserva> reservaOpt = reservaService.obtenerReservaPorId(id);
            if (reservaOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        new ErrorResponse(HttpStatus.NOT_FOUND.value(), "Not Found",
                                "Reserva con ID " + id + " no encontrada", httpRequest.getRequestURI()));
            }

            // Buscar el cliente y la habitación por sus IDs
            Cliente cliente = clienteService.obtenerClientePorId(request.getIdCliente());
            if (cliente == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        new ErrorResponse(HttpStatus.NOT_FOUND.value(), "Not Found",
                                "Cliente con ID " + request.getIdCliente() + " no encontrado", httpRequest.getRequestURI()));
            }

            Habitacion habitacion = habitacionRepository.findById(request.getIdHabitacion())
                    .orElse(null);
            if (habitacion == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        new ErrorResponse(HttpStatus.NOT_FOUND.value(), "Not Found",
                                "Habitación con ID " + request.getIdHabitacion() + " no encontrada", httpRequest.getRequestURI()));
            }

            // Crear una reserva actualizada
            Reserva reservaActualizada = new Reserva();
            reservaActualizada.setCheckIn(request.getCheckIn());
            reservaActualizada.setCheckOut(request.getCheckOut());
            reservaActualizada.setIdCliente(cliente.getIdCliente());
            reservaActualizada.setHabitacion(habitacion);

            Reserva reserva = reservaService.actualizarReserva(id, reservaActualizada);
            return ResponseEntity.ok(reserva);

        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(
                    new ErrorResponse(HttpStatus.SERVICE_UNAVAILABLE.value(), "Service Unavailable",
                            "No se pudo conectar con el microservicio de clientes: " + e.getMessage(), httpRequest.getRequestURI()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal Server Error",
                            "Error al actualizar la reserva: " + e.getMessage(), httpRequest.getRequestURI()));
        }
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminarReserva(@PathVariable Integer id, HttpServletRequest httpRequest) {
        try {
            Optional<Reserva> reservaOpt = reservaService.obtenerReservaPorId(id);
            if (reservaOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        new ErrorResponse(HttpStatus.NOT_FOUND.value(), "Not Found",
                                "Reserva con ID " + id + " no encontrada", httpRequest.getRequestURI()));
            }

            reservaService.eliminarReserva(id);
            return ResponseEntity.ok().build();

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal Server Error",
                            "Error al eliminar la reserva: " + e.getMessage(), httpRequest.getRequestURI()));
        }
    }

    @GetMapping("/listar")
    public ResponseEntity<?> listarReservasConCliente(HttpServletRequest httpRequest) {
        try {
            List<Reserva> reservas = reservaService.listarReservas();
            List<ReservaConCliente> combinadas = reservas.stream()
                    .map(reserva -> {
                        Cliente cliente = clienteService.obtenerClientePorId(reserva.getIdCliente());
                        if (cliente == null) {
                            throw new IllegalStateException("Cliente con ID " + reserva.getIdCliente() + " no encontrado");
                        }
                        return new ReservaConCliente(reserva, cliente);
                    })
                    .toList();

            return ResponseEntity.ok(combinadas);

        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ErrorResponse(HttpStatus.NOT_FOUND.value(), "Not Found",
                            e.getMessage(), httpRequest.getRequestURI()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal Server Error",
                            "Error al listar las reservas: " + e.getMessage(), httpRequest.getRequestURI()));
        }
    }

    @GetMapping("/habitaciones")
    public ResponseEntity<List<HabitacionResumen>> listarHabitacionesDisponibles() {
        List<Habitacion> habitaciones = habitacionRepository.findAll();

        // Convertimos a DTO simple
        List<HabitacionResumen> resultado = habitaciones.stream()
            .filter(h -> Boolean.TRUE.equals(h.getDisponible()) && Boolean.FALSE.equals(h.getMantenimiento()))
            .sorted((h1, h2) -> h1.getNumero().compareTo(h2.getNumero()))
            .map(h -> new HabitacionResumen(h.getIdHabitacion(), h.getNumero()))
            .toList();

        return ResponseEntity.ok(resultado);
    }

    // DTO interno para enviar solo lo necesario
    public static class HabitacionResumen {
        private Integer id;
        private Integer numero;

        public HabitacionResumen(Integer id, Integer numero) {
            this.id = id;
            this.numero = numero;
        }

        public Integer getId() {
            return id;
        }

        public Integer getNumero() {
            return numero;
        }
    }



    @GetMapping("/habitaciones-disponibles")
    public ResponseEntity<List<HabitacionResumen>> obtenerHabitacionesDisponiblesPorFechas(
            @RequestParam LocalDate checkIn,
            @RequestParam LocalDate checkOut) {

        // Obtener todas las habitaciones disponibles y sin mantenimiento
        List<Habitacion> todasDisponibles = habitacionRepository.findAll().stream()
                .filter(h -> Boolean.TRUE.equals(h.getDisponible()) && Boolean.FALSE.equals(h.getMantenimiento()))
                .toList();

        // Obtener reservas que se superpongan con el rango de fechas
        List<Reserva> reservasSolapadas = reservaService.listarReservas().stream()
                .filter(r ->
                        !(checkOut.isBefore(r.getCheckIn()) || checkIn.isAfter(r.getCheckOut()))
                ).toList();

        // IDs de habitaciones ocupadas en ese rango
        List<Integer> habitacionesOcupadas = reservasSolapadas.stream()
                .map(r -> r.getHabitacion().getIdHabitacion())
                .toList();

        // Filtrar habitaciones disponibles que no estén en la lista de ocupadas
        List<HabitacionResumen> disponibles = todasDisponibles.stream()
                .filter(h -> !habitacionesOcupadas.contains(h.getIdHabitacion()))
                .map(h -> new HabitacionResumen(h.getIdHabitacion(), h.getNumero()))
                .toList();

        return ResponseEntity.ok(disponibles);
}



}