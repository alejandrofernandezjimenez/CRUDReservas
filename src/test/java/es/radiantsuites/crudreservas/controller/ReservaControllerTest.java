package es.radiantsuites.crudreservas.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.radiantsuites.crudreservas.dto.Cliente;
import es.radiantsuites.crudreservas.entity.Habitacion;
import es.radiantsuites.crudreservas.entity.Reserva;
import es.radiantsuites.crudreservas.repository.HabitacionRepository;
import es.radiantsuites.crudreservas.service.ClienteService;
import es.radiantsuites.crudreservas.service.ReservaService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReservaController.class)
class ReservaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean private ReservaService reservaService;
    @MockBean private ClienteService clienteService;
    @MockBean private HabitacionRepository habitacionRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCrearReserva_OK() throws Exception {
        ReservaController.ReservaRequest request = new ReservaController.ReservaRequest();
        request.setCheckIn(LocalDate.of(2025, 4, 10));
        request.setCheckOut(LocalDate.of(2025, 4, 12));
        request.setIdCliente(1);
        request.setIdHabitacion(101);

        Cliente cliente = new Cliente(1, "David", "correo@demo.com");
        Habitacion habitacion = new Habitacion();
        habitacion.setIdHabitacion(101);
        habitacion.setNumero(10);
        habitacion.setPlanta(1);

        Reserva reserva = new Reserva(request.getCheckIn(), request.getCheckOut(), 1, habitacion);
        reserva.setIdReserva(1);

        Mockito.when(clienteService.obtenerClientePorId(1)).thenReturn(cliente);
        Mockito.when(habitacionRepository.findById(101)).thenReturn(Optional.of(habitacion));
        Mockito.when(reservaService.crearReserva(Mockito.any())).thenReturn(reserva);

        mockMvc.perform(post("/api/reservas/crear")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idReserva").value(1))
                .andExpect(jsonPath("$.idCliente").value(1));
    }
}