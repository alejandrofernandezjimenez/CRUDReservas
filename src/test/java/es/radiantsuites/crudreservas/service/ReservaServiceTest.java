package es.radiantsuites.crudreservas.service;

import es.radiantsuites.crudreservas.controller.ReservaController;
import es.radiantsuites.crudreservas.entity.Habitacion;
import es.radiantsuites.crudreservas.entity.Reserva;
import es.radiantsuites.crudreservas.repository.ReservaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@WebMvcTest(ReservaService.class)
class ReservaServiceTest {

    @Mock
    private ReservaRepository reservaRepository;

    @InjectMocks
    private ReservaService reservaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCrearReserva_Valida() {
        Habitacion habitacion = new Habitacion();
        habitacion.setIdHabitacion(1);

        Reserva reserva = new Reserva(LocalDate.of(2025, 4, 10), LocalDate.of(2025, 4, 12), 1, habitacion);

        when(reservaRepository.existsByHabitacionAndCheckInLessThanEqualAndCheckOutGreaterThanEqual(
                any(), any(), any())).thenReturn(false);
        when(reservaRepository.save(ArgumentMatchers.any(Reserva.class))).thenReturn(reserva);

        Reserva creada = reservaService.crearReserva(reserva);
        assertNotNull(creada);
        assertEquals(1, creada.getIdCliente());
    }

    @Test
    void testCrearReserva_HabitacionOcupada() {
        Habitacion habitacion = new Habitacion();
        habitacion.setIdHabitacion(1);

        Reserva reserva = new Reserva(LocalDate.of(2025, 4, 10), LocalDate.of(2025, 4, 12), 1, habitacion);

        when(reservaRepository.existsByHabitacionAndCheckInLessThanEqualAndCheckOutGreaterThanEqual(
                any(), any(), any())).thenReturn(true);

        IllegalStateException ex = assertThrows(IllegalStateException.class, () ->
                reservaService.crearReserva(reserva));
        assertEquals("La habitación no está disponible en las fechas solicitadas", ex.getMessage());
    }
}