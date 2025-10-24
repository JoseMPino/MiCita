package com.pinosoft.micita.repository;

import com.pinosoft.micita.model.TrabajadorServicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrabajadorServicioRepository extends JpaRepository<TrabajadorServicio, Long> {
	
	  // Encontrar servicios por trabajador
    List<TrabajadorServicio> findByTrabajadorId(Long trabajadorId);
    
    // Encontrar trabajadores por servicio
    List<TrabajadorServicio> findByServicioId(Long servicioId);
    
    // Verificar si existe la relación trabajador-servicio
    boolean existsByTrabajadorIdAndServicioId(Long trabajadorId, Long servicioId);
    
    // Encontrar relación específica
    Optional<TrabajadorServicio> findByTrabajadorIdAndServicioId(Long trabajadorId, Long servicioId);
    
    // Encontrar servicios con confirmación automática por trabajador
    List<TrabajadorServicio> findByTrabajadorIdAndConfirmacionAutomaticaTrue(Long trabajadorId);

}
