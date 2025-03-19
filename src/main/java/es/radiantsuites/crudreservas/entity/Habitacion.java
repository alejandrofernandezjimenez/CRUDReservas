package es.radiantsuites.crudreservas.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "habitaciones", schema = "public")
public class Habitacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_habitacion")
    private Integer idHabitacion;

    @Column(name = "numero")
    private Integer numero;

    @Column(name = "planta")
    private Integer planta;

    @Column(name = "telefono")
    private String telefono;

    @Column(name = "categoria")
    private String categoria;

    @Column(name = "disponible")
    private Boolean disponible;

    @Column(name = "mantenimiento")
    private Boolean mantenimiento;

    public Habitacion() {
    }

    public Habitacion(Integer numero, Integer planta, String telefono, String categoria, Boolean disponible, Boolean mantenimiento) {
        this.numero = numero;
        this.planta = planta;
        this.telefono = telefono;
        this.categoria = categoria;
        this.disponible = disponible;
        this.mantenimiento = mantenimiento;
    }

    // Getters y Setters
    public Integer getIdHabitacion() {
        return idHabitacion;
    }

    public void setIdHabitacion(Integer idHabitacion) {
        this.idHabitacion = idHabitacion;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public Integer getPlanta() {
        return planta;
    }

    public void setPlanta(Integer planta) {
        this.planta = planta;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public Boolean getDisponible() {
        return disponible;
    }

    public void setDisponible(Boolean disponible) {
        this.disponible = disponible;
    }

    public Boolean getMantenimiento() {
        return mantenimiento;
    }

    public void setMantenimiento(Boolean mantenimiento) {
        this.mantenimiento = mantenimiento;
    }

    @Override
    public String toString() {
        return "Habitacion{" +
                "idHabitacion=" + idHabitacion +
                ", numero=" + numero +
                ", planta=" + planta +
                ", telefono='" + telefono + '\'' +
                ", categoria='" + categoria + '\'' +
                ", disponible=" + disponible +
                ", mantenimiento=" + mantenimiento +
                '}';
    }
}