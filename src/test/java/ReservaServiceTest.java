

import es.radiantsuites.crudreservas.entity.Cliente;
import es.radiantsuites.crudreservas.entity.Habitacion;
import es.radiantsuites.crudreservas.entity.Reserva;
import es.radiantsuites.crudreservas.repository.ReservaRepository;
import es.radiantsuites.crudreservas.service.ReservaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservaServiceTest {

    @Mock
    private ReservaRepository reservaRepository;

    @InjectMocks
    private ReservaService reservaService;

    private Cliente cliente;
    private Habitacion habitacion;
    private Reserva reserva;
    private LocalDate checkIn;
    private LocalDate checkOut;

    @BeforeEach
    void setUp() {
        // Configurar datos de prueba
        cliente = new Cliente();
        cliente.setIdCliente(1);
        cliente.setNombre("Juan Diogo");

        habitacion = new Habitacion();
        habitacion.setIdHabitacion(101);
        habitacion.setNumero(101);

        checkIn = LocalDate.of(2025, 3, 20);
        checkOut = LocalDate.of(2025, 3, 22);

        reserva = new Reserva();
        reserva.setIdReserva(1);
        reserva.setCheckIn(checkIn);
        reserva.setCheckOut(checkOut);
        reserva.setCliente(cliente);
        reserva.setHabitacion(habitacion);
    }

    @Test
    void crearReserva_success() {
        // Arrange: Configurar el comportamiento del mock
        when(reservaRepository.existsByHabitacionAndCheckInLessThanEqualAndCheckOutGreaterThanEqual(
                habitacion, checkIn, checkOut)).thenReturn(false); // Habitación disponible
        when(reservaRepository.save(any(Reserva.class))).thenReturn(reserva);

        // Act: Llamar al método a probar
        Reserva nuevaReserva = reservaService.crearReserva(reserva);

        // Assert: Verificar el resultado
        assertNotNull(nuevaReserva);
        assertEquals(1, nuevaReserva.getIdReserva());
        assertEquals(checkIn, nuevaReserva.getCheckIn());
        assertEquals(checkOut, nuevaReserva.getCheckOut());
        assertEquals(cliente, nuevaReserva.getCliente());
        assertEquals(habitacion, nuevaReserva.getHabitacion());

        // Verificar interacciones con el mock
        verify(reservaRepository).existsByHabitacionAndCheckInLessThanEqualAndCheckOutGreaterThanEqual(
                habitacion, checkIn, checkOut);
        verify(reservaRepository).save(reserva);
    }

    @Test
    void crearReserva_roomNotAvailable_throwsException() {
        // Arrange: Simular que la habitación está ocupada
        when(reservaRepository.existsByHabitacionAndCheckInLessThanEqualAndCheckOutGreaterThanEqual(
                habitacion, checkIn, checkOut)).thenReturn(true);

        // Act & Assert: Verificar que se lanza la excepción
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            reservaService.crearReserva(reserva);
        });

        assertEquals("La habitación no está disponible en las fechas solicitadas", exception.getMessage());

        // Verificar interacciones
        verify(reservaRepository).existsByHabitacionAndCheckInLessThanEqualAndCheckOutGreaterThanEqual(
                habitacion, checkIn, checkOut);
        verify(reservaRepository, never()).save(any(Reserva.class));
    }

    @Test
    void crearReserva_invalidDates_throwsException() {
        // Arrange: Configurar fechas inválidas (checkOut antes de checkIn)
        reserva.setCheckIn(LocalDate.of(2025, 3, 22));
        reserva.setCheckOut(LocalDate.of(2025, 3, 20));

        // Act & Assert: Verificar que se lanza la excepción
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            reservaService.crearReserva(reserva);
        });

        assertEquals("La fecha de check-out debe ser posterior a la fecha de check-in", exception.getMessage());

        // Verificar que no se interactúa con el repositorio
        verify(reservaRepository, never()).existsByHabitacionAndCheckInLessThanEqualAndCheckOutGreaterThanEqual(
                any(), any(), any());
        verify(reservaRepository, never()).save(any());
    }

    @Test
    void crearReserva_nullClienteOrHabitacion_throwsException() {
        // Arrange: Configurar reserva con cliente nulo
        reserva.setCliente(null);

        // Act & Assert: Verificar que se lanza la excepción
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            reservaService.crearReserva(reserva);
        });

        assertEquals("Cliente y habitación son obligatorios", exception.getMessage());

        // Verificar que no se interactúa con el repositorio
        verify(reservaRepository, never()).existsByHabitacionAndCheckInLessThanEqualAndCheckOutGreaterThanEqual(
                any(), any(), any());
        verify(reservaRepository, never()).save(any());
    }

    @Test
    void obtenerReservaPorId_success() {
        // Arrange
        when(reservaRepository.findById(1)).thenReturn(Optional.of(reserva));

        // Act
        Optional<Reserva> result = reservaService.obtenerReservaPorId(1);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(reserva, result.get());

        // Verificar interacciones
        verify(reservaRepository).findById(1);
    }

    @Test
    void obtenerReservaPorId_notFound() {
        // Arrange
        when(reservaRepository.findById(1)).thenReturn(Optional.empty());

        // Act
        Optional<Reserva> result = reservaService.obtenerReservaPorId(1);

        // Assert
        assertFalse(result.isPresent());

        // Verificar interacciones
        verify(reservaRepository).findById(1);
    }

    @Test
    void actualizarReserva_success() {
        // Arrange: Configurar una reserva existente
        Reserva updatedReserva = new Reserva();
        updatedReserva.setCheckIn(LocalDate.of(2025, 3, 25));
        updatedReserva.setCheckOut(LocalDate.of(2025, 3, 27));
        updatedReserva.setCliente(cliente);
        updatedReserva.setHabitacion(habitacion);

        when(reservaRepository.findById(1)).thenReturn(Optional.of(reserva));
        when(reservaRepository.save(any(Reserva.class))).thenReturn(reserva);

        // Act
        Reserva result = reservaService.actualizarReserva(1, updatedReserva);

        // Assert
        assertNotNull(result);
        assertEquals(LocalDate.of(2025, 3, 25), result.getCheckIn());
        assertEquals(LocalDate.of(2025, 3, 27), result.getCheckOut());
        assertEquals(cliente, result.getCliente());
        assertEquals(habitacion, result.getHabitacion());

        // Verificar interacciones
        verify(reservaRepository).findById(1);
        verify(reservaRepository).save(reserva);
    }

    @Test
    void actualizarReserva_invalidDates_throwsException() {
        // Arrange: Configurar una reserva existente con fechas inválidas
        Reserva updatedReserva = new Reserva();
        updatedReserva.setCheckIn(LocalDate.of(2025, 3, 27));
        updatedReserva.setCheckOut(LocalDate.of(2025, 3, 25));
        updatedReserva.setCliente(cliente);
        updatedReserva.setHabitacion(habitacion);

        when(reservaRepository.findById(1)).thenReturn(Optional.of(reserva));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            reservaService.actualizarReserva(1, updatedReserva);
        });

        assertEquals("La fecha de check-out debe ser posterior a la fecha de check-in", exception.getMessage());

        // Verificar interacciones
        verify(reservaRepository).findById(1);
        verify(reservaRepository, never()).save(any());
    }

    @Test
    void actualizarReserva_notFound_throwsException() {
        // Arrange
        when(reservaRepository.findById(1)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            reservaService.actualizarReserva(1, reserva);
        });

        assertEquals("Reserva no encontrada con ID: 1", exception.getMessage());

        // Verificar interacciones
        verify(reservaRepository).findById(1);
        verify(reservaRepository, never()).save(any());
    }

    @Test
    void eliminarReserva_success() {
        // Arrange
        when(reservaRepository.existsById(1)).thenReturn(true);

        // Act
        reservaService.eliminarReserva(1);

        // Assert: Verificar interacciones
        verify(reservaRepository).existsById(1);
        verify(reservaRepository).deleteById(1);
    }

    @Test
    void eliminarReserva_notFound_throwsException() {
        // Arrange
        when(reservaRepository.existsById(1)).thenReturn(false);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            reservaService.eliminarReserva(1);
        });

        assertEquals("Reserva no encontrada con ID: 1", exception.getMessage());

        // Verificar interacciones
        verify(reservaRepository).existsById(1);
        verify(reservaRepository, never()).deleteById(any());
    }

    @Test
    void listarReservas_success() {
        // Arrange
        List<Reserva> reservas = Arrays.asList(reserva);
        when(reservaRepository.findAll()).thenReturn(reservas);

        // Act
        List<Reserva> result = reservaService.listarReservas();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(reserva, result.get(0));

        // Verificar interacciones
        verify(reservaRepository).findAll();
    }
}