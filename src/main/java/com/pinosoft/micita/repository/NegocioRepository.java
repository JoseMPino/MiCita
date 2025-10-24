package com.pinosoft.micita.repository;
import com.pinosoft.micita.model.Negocio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface  NegocioRepository extends JpaRepository<Negocio, Long> {
    // Método para buscar negocios por administrador
    List<Negocio> findByAdministradorId(Long administradorId);
    
    // Método para buscar un negocio por ID y administrador
    Optional<Negocio> findByIdAndAdministradorId(Long id, Long administradorId);
    
    // Método para verificar si existe un negocio con nombre para un administrador
    boolean existsByNombreAndAdministradorId(String nombre, Long administradorId);
    
    // Método para verificar si un negocio existe y pertenece a un administrador
    boolean existsByIdAndAdministradorId(Long id, Long administradorId);
}