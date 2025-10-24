package com.pinosoft.micita.repository;


import com.pinosoft.micita.model.DisponibilidadEspecial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DisponibilidadEspecialRepository extends JpaRepository<DisponibilidadEspecial, Long> {
	 // Encontrar disponibilidades especiales por trabajador
    List<DisponibilidadEspecial> findByIdTrabajador(Long idTrabajador);
    
    // Encontrar disponibilidades especiales por trabajador y fecha
    List<DisponibilidadEspecial> findByIdTrabajadorAndFecha(Long idTrabajador, LocalDate fecha);
    
    // Encontrar disponibilidades especiales por trabajador y rango de fechas
    List<DisponibilidadEspecial> findByIdTrabajadorAndFechaBetween(Long idTrabajador, LocalDate fechaInicio, LocalDate fechaFin);
    
    // Encontrar bloques por trabajador y fecha
    List<DisponibilidadEspecial> findByIdTrabajadorAndFechaAndTipo(Long idTrabajador, LocalDate fecha, DisponibilidadEspecial.TipoDisponibilidad tipo);
    
    // Encontrar disponibilidades especiales por tipo
    List<DisponibilidadEspecial> findByTipo(DisponibilidadEspecial.TipoDisponibilidad tipo);
    
    // Verificar si existe un bloqueo para un trabajador en una fecha y hora específica
    @Query("SELECT COUNT(d) > 0 FROM DisponibilidadEspecial d WHERE " +
           "d.idTrabajador = :trabajadorId AND d.fecha = :fecha AND " +
           "d.tipo = 'BLOQUEO' AND " +
           "(:hora BETWEEN d.horaInicio AND d.horaFin OR " +
           "d.horaInicio BETWEEN :horaInicio AND :horaFin)")
    boolean existsBloqueoEnHorario(@Param("trabajadorId") Long trabajadorId,
                                  @Param("fecha") LocalDate fecha,
                                  @Param("hora") String hora,
                                  @Param("horaInicio") String horaInicio,
                                  @Param("horaFin") String horaFin);
    
    // Encontrar disponibilidades especiales futuras por trabajador
    List<DisponibilidadEspecial> findByIdTrabajadorAndFechaGreaterThanEqual(Long idTrabajador, LocalDate fecha);

}
