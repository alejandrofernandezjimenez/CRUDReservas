package es.radiantsuites.crudreservas.entity;

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

    @Column(name = "id_cliente")
    private Integer idCliente;

    @Column(name = "id_habitacion")
    private Integer idHabitacion;

    public Reserva() {
    }

    public Reserva(LocalDate checkIn, LocalDate checkOut, Integer idCliente, Integer idHabitacion) {
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.idCliente = idCliente;
        this.idHabitacion = idHabitacion;
    }

    public int getIdReserva() {
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

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }

    public int getIdHabitacion() {
        return idHabitacion;
    }

    public void setIdHabitacion(Integer idHabitacion) {
        this.idHabitacion = idHabitacion;
    }
}