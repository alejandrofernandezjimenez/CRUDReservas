package es.radiantsuites.crudreservas.repository;

import es.radiantsuites.crudreservas.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {

    // Buscar un cliente por DNI
    Optional<Cliente> findByDni(String dni);

    // Buscar un cliente por correo
    Optional<Cliente> findByCorreo(String correo);

    // Verificar si existe un cliente con un DNI específico
    boolean existsByDni(String dni);

    // Verificar si existe un cliente con un correo específico
    boolean existsByCorreo(String correo);
}