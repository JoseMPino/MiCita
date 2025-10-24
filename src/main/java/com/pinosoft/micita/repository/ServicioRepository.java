package com.pinosoft.micita.repository;

import com.pinosoft.micita.model.Servicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServicioRepository extends JpaRepository<Servicio, Long> {
	
	 List<Servicio> findByNegocioId(Long negocioId);
	 
	 
	    boolean existsByNombreAndNegocioId(String nombre, Long negocioId);
	    
	 // Método para verificar si un servicio tiene citas asociadas
	    @Query("SELECT COUNT(c) > 0 FROM Cita c WHERE c.servicio.id = :servicioId")
	    boolean tieneCitasAsociadas(@Param("servicioId") Long servicioId);
	    
	    List<Servicio> findByNegocioIdAndEstado(Long negocioId, Boolean estado);

}
