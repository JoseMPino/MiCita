package com.pinosoft.micita.repository;

import com.pinosoft.micita.model.Reprogramacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReprogramacionRepository extends JpaRepository<Reprogramacion, Long> {
	// Encontrar reprogramaciones por cita
    List<Reprogramacion> findByIdCita(Long idCita);
    
    // Encontrar reprogramaciones por estado
    List<Reprogramacion> findByEstado(Reprogramacion.EstadoReprogramacion estado);
    
    // Encontrar reprogramaciones pendientes (método específico con nombre query)
    @Query("SELECT r FROM Reprogramacion r WHERE r.estado = 'PENDIENTE'")
    List<Reprogramacion> findPendientes();
    
    // Encontrar reprogramaciones aprobadas
    @Query("SELECT r FROM Reprogramacion r WHERE r.estado = 'APROBADA'")
    List<Reprogramacion> findAprobadas();
    
    // Encontrar reprogramaciones rechazadas
    @Query("SELECT r FROM Reprogramacion r WHERE r.estado = 'RECHAZADA'")
    List<Reprogramacion> findRechazadas();
    
    // Encontrar la última reprogramación de una cita
    Optional<Reprogramacion> findFirstByIdCitaOrderByCreateAtDesc(Long idCita);
    
    // Verificar si existe reprogramación para una cita con estado específico
    boolean existsByIdCitaAndEstado(Long idCita, Reprogramacion.EstadoReprogramacion estado);
    
    // Verificar específicamente si existe reprogramación pendiente para una cita
    @Query("SELECT COUNT(r) > 0 FROM Reprogramacion r WHERE r.idCita = :idCita AND r.estado = 'PENDIENTE'")
    boolean existsPendienteByIdCita(@Param("idCita") Long idCita);
    
    // Encontrar reprogramaciones por cita ordenadas por fecha de creación
    List<Reprogramacion> findByIdCitaOrderByCreateAtDesc(Long idCita);
    
    // Contar reprogramaciones pendientes por trabajador
    @Query("SELECT COUNT(r) FROM Reprogramacion r JOIN Cita c ON r.idCita = c.idCita " +
           "WHERE c.idTrabajador = :trabajadorId AND r.estado = 'PENDIENTE'")
    long countReprogramacionesPendientesPorTrabajador(@Param("trabajadorId") Long trabajadorId);
    
    // Encontrar reprogramaciones por cita y estado
    List<Reprogramacion> findByIdCitaAndEstado(Long idCita, Reprogramacion.EstadoReprogramacion estado);
    
    // Encontrar reprogramaciones pendientes por cita
    @Query("SELECT r FROM Reprogramacion r WHERE r.idCita = :idCita AND r.estado = 'PENDIENTE'")
    List<Reprogramacion> findPendientesByIdCita(@Param("idCita") Long idCita);

}
