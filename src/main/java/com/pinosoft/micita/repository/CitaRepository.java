package com.pinosoft.micita.repository;
import com.pinosoft.micita.model.Cita;
import com.pinosoft.micita.dto.CitaFiltroDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CitaRepository extends JpaRepository<Cita, Long>{
	 // Encontrar citas por trabajador
    List<Cita> findByIdTrabajador(Long idTrabajador);
    
    // Encontrar citas por cliente
    List<Cita> findByIdCliente(Long idCliente);
    
    // Encontrar citas por servicio
    List<Cita> findByServicio_Id(Long idServicio);
    
    // Encontrar citas por estado
    List<Cita> findByEstado(Cita.EstadoCita estado);
    
    // Encontrar citas por trabajador y fecha
    List<Cita> findByIdTrabajadorAndFecha(Long idTrabajador, LocalDate fecha);
    
    // Encontrar citas por trabajador y estado
    List<Cita> findByIdTrabajadorAndEstado(Long idTrabajador, Cita.EstadoCita estado);
    
    // Encontrar citas por fecha
    List<Cita> findByFecha(LocalDate fecha);
    
    // Encontrar citas por rango de fechas
    List<Cita> findByFechaBetween(LocalDate fechaInicio, LocalDate fechaFin);
    
    // Encontrar citas por trabajador y rango de fechas
    List<Cita> findByIdTrabajadorAndFechaBetween(Long idTrabajador, LocalDate fechaInicio, LocalDate fechaFin);
    
    // Verificar si existe conflicto de horario para un trabajador
    @Query("SELECT COUNT(c) > 0 FROM Cita c WHERE " +
           "c.idTrabajador = :trabajadorId AND c.fecha = :fecha AND " +
           "c.estado NOT IN ('CANCELADA') AND " +
           "(:horaInicio < c.horaFin AND :horaFin > c.horaInicio)")
    boolean existsConflictoHorario(@Param("trabajadorId") Long trabajadorId,
                                  @Param("fecha") LocalDate fecha,
                                  @Param("horaInicio") LocalTime horaInicio,
                                  @Param("horaFin") LocalTime horaFin);
    
    // Verificar conflicto excluyendo una cita específica (para actualizaciones)
    @Query("SELECT COUNT(c) > 0 FROM Cita c WHERE " +
           "c.idTrabajador = :trabajadorId AND c.fecha = :fecha AND " +
           "c.idCita != :excludeCitaId AND " +
           "c.estado NOT IN ('CANCELADA') AND " +
           "(:horaInicio < c.horaFin AND :horaFin > c.horaInicio)")
    boolean existsConflictoHorarioExcluyendoCita(@Param("trabajadorId") Long trabajadorId,
                                                @Param("fecha") LocalDate fecha,
                                                @Param("horaInicio") LocalTime horaInicio,
                                                @Param("horaFin") LocalTime horaFin,
                                                @Param("excludeCitaId") Long excludeCitaId);
    
    // Contar citas por estado y trabajador
    long countByIdTrabajadorAndEstado(Long idTrabajador, Cita.EstadoCita estado);
    
    // Encontrar citas pendientes de confirmación automática
    @Query("SELECT c FROM Cita c WHERE c.estado = 'PENDIENTE' AND EXISTS " +
           "(SELECT ts FROM TrabajadorServicio ts WHERE " +
           "ts.trabajadorId = c.idTrabajador AND ts.servicioId = c.servicio.id AND " +
           "ts.confirmacionAutomatica = true)")
    List<Cita> findCitasPendientesConfirmacionAutomatica();
    
    // Método para búsqueda avanzada con filtros
    @Query("SELECT c FROM Cita c WHERE " +
    	       "(:#{#filtro.idTrabajador} IS NULL OR c.idTrabajador = :#{#filtro.idTrabajador}) AND " +
    	       "(:#{#filtro.idCliente} IS NULL OR c.idCliente = :#{#filtro.idCliente}) AND " +
    	       "(:#{#filtro.idServicio} IS NULL OR c.servicio.id = :#{#filtro.idServicio}) AND " +
    	       "(:#{#filtro.fechaInicio} IS NULL OR c.fecha >= :#{#filtro.fechaInicio}) AND " +
    	       "(:#{#filtro.fechaFin} IS NULL OR c.fecha <= :#{#filtro.fechaFin}) AND " +
    	       "(:#{#filtro.estado} IS NULL OR c.estado = :#{#filtro.estado}) " +
    	       "ORDER BY c.fecha DESC, c.horaInicio DESC")
    List<Cita> findByFiltros(@Param("filtro") CitaFiltroDTO filtro);

}
