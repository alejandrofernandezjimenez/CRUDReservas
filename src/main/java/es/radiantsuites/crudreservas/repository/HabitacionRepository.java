package es.radiantsuites.crudreservas.repository;

import es.radiantsuites.crudreservas.entity.Habitacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HabitacionRepository extends JpaRepository<Habitacion, Integer> {

    // Buscar una habitación por número y planta
    Optional<Habitacion> findByNumeroAndPlanta(Integer numero, Integer planta);

    // Buscar habitaciones disponibles
    List<Habitacion> findByDisponibleTrue();

    // Buscar habitaciones en mantenimiento
    List<Habitacion> findByMantenimientoTrue();

    // Buscar habitaciones por categoría
    List<Habitacion> findByCategoria(String categoria);

    // Verificar si existe una habitación con un número y planta específicos
    boolean existsByNumeroAndPlanta(Integer numero, Integer planta);
}