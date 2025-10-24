package com.pinosoft.micita.repository;


import com.pinosoft.micita.model.DisponibilidadBase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DisponibilidadBaseRepository extends JpaRepository<DisponibilidadBase, Long> {
	
	 // Encontrar disponibilidad base por trabajador
    List<DisponibilidadBase> findByIdTrabajador(Long idTrabajador);
    
    // Encontrar disponibilidad base por trabajador y día de la semana
    List<DisponibilidadBase> findByIdTrabajadorAndDiaSemana(Long idTrabajador, Integer diaSemana);
    
    // Encontrar disponibilidad base activa por trabajador
    List<DisponibilidadBase> findByIdTrabajadorAndEstadoTrue(Long idTrabajador);
    
    // Encontrar disponibilidad base activa por trabajador y día
    List<DisponibilidadBase> findByIdTrabajadorAndDiaSemanaAndEstadoTrue(Long idTrabajador, Integer diaSemana);
    
    // Verificar si existe disponibilidad base para un trabajador en un día específico
    boolean existsByIdTrabajadorAndDiaSemanaAndEstadoTrue(Long idTrabajador, Integer diaSemana);
    
    // Contar disponibilidades base activas por trabajador
    long countByIdTrabajadorAndEstadoTrue(Long idTrabajador);
	
}
