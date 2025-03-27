package es.radiantsuites.crudreservas.dto;

import es.radiantsuites.crudreservas.entity.Reserva;

public class ReservaConCliente {

    private Reserva reserva;
    private Cliente cliente;

    public ReservaConCliente() {}

    public ReservaConCliente(Reserva reserva, Cliente cliente) {
        this.reserva = reserva;
        this.cliente = cliente;
    }

    public Reserva getReserva() {
        return reserva;
    }

    public void setReserva(Reserva reserva) {
        this.reserva = reserva;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
}
