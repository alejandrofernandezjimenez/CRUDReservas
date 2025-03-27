package es.radiantsuites.crudreservas.entity;

import es.radiantsuites.crudreservas.dto.Cliente;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "reservas", schema = "public")
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reserva")
    private Integer idReserva;

    @Column(name = "check_in")
    private LocalDate checkIn;

    @Column(name = "check_out")
    private LocalDate checkOut;

    @Column(name = "id_cliente", nullable = false)
    private Integer idCliente;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_habitacion", nullable = false)
    private Habitacion habitacion;

    public Reserva() {
    }

    public Reserva(LocalDate checkIn, LocalDate checkOut, Integer idCliente, Habitacion habitacion) {
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.idCliente = idCliente;
        this.habitacion = habitacion;
    }

    // Getters y Setters
    public Integer getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(Integer idReserva) {
        this.idReserva = idReserva;
    }

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

    public Habitacion getHabitacion() {
        return habitacion;
    }

    public void setHabitacion(Habitacion habitacion) {
        this.habitacion = habitacion;
    }

    @Override
    public String toString() {
        return "Reserva{" +
                "idReserva=" + idReserva +
                ", checkIn=" + checkIn +
                ", checkOut=" + checkOut +
                ", idCliente=" + idCliente +
                ", habitacion=" + habitacion +
                '}';
    }
}