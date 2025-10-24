package com.pinosoft.micita.service;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import com.pinosoft.micita.dto.DisponibilidadCitaDTO;
import com.pinosoft.micita.dto.DisponibilidadBaseDTO;
import com.pinosoft.micita.dto.DisponibilidadEspecialDTO;
import com.pinosoft.micita.dto.CitaDTO;
import com.pinosoft.micita.dto.ServicioDTO;
import com.pinosoft.micita.dto.IntervaloDisponibleDTO;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class DisponibilidadCitaService {
	private final DisponibilidadBaseService disponibilidadBaseService;
	private final DisponibilidadEspecialService disponibilidadEspecialService;
	private final CitaService citaService;
	private final ServicioService servicioService;
	private final TrabajadorServicioService trabajadorServicioService;


	 //Obtiene los intervalos disponibles para agendar una cita
	public DisponibilidadCitaDTO obtenerIntervalosDisponibles(Long trabajadorId, Long servicioId, LocalDate fecha) {
		// Validar que el trabajador ofrece el servicio
		if (!trabajadorServicioService.existeRelacion(trabajadorId, servicioId)) {
			throw new RuntimeException("El trabajador no ofrece este servicio");
		}

		// Obtener información del servicio
		ServicioDTO servicio = servicioService.obtenerPorId(servicioId);
		Integer duracionMinutos = servicio.getDuracionMinutos();

		// Obtener disponibilidad base para el día de la semana
		Integer diaSemana = fecha.getDayOfWeek().getValue(); // 1=Lunes, 7=Domingo
		List<DisponibilidadBaseDTO> disponibilidadesBase = disponibilidadBaseService
				.obtenerActivasPorTrabajadorYDia(trabajadorId, diaSemana);

		// Obtener disponibilidades especiales para la fecha
		List<DisponibilidadEspecialDTO> disponibilidadesEspeciales = disponibilidadEspecialService
				.obtenerPorTrabajadorYFecha(trabajadorId, fecha);

		// Obtener citas existentes para la fecha
		List<CitaDTO> citasExistentes = citaService.obtenerPorTrabajadorYFecha(trabajadorId, fecha);

		// Generar intervalos disponibles
		List<IntervaloDisponibleDTO> intervalosDisponibles = generarIntervalosDisponibles(fecha, disponibilidadesBase,
				disponibilidadesEspeciales, citasExistentes, duracionMinutos);

		DisponibilidadCitaDTO resultado = new DisponibilidadCitaDTO();
		resultado.setTrabajadorId(trabajadorId);
		resultado.setServicioId(servicioId);
		resultado.setFecha(fecha);
		resultado.setIntervalosDisponibles(intervalosDisponibles);
		return resultado;
	}


	 //Genera los intervalos disponibles basándose en la disponibilidad y citas existentes
	private List<IntervaloDisponibleDTO> generarIntervalosDisponibles(LocalDate fecha,
			List<DisponibilidadBaseDTO> disponibilidadesBase,
			List<DisponibilidadEspecialDTO> disponibilidadesEspeciales, List<CitaDTO> citasExistentes,
			Integer duracionMinutos) {
		List<IntervaloDisponibleDTO> intervalosDisponibles = new ArrayList<>();

		// Combinar disponibilidades base y especiales
		List<IntervaloDisponibleDTO> ventanasDisponibilidad = combinarDisponibilidades(fecha, disponibilidadesBase,
				disponibilidadesEspeciales);

		// Para cada ventana de disponibilidad, generar intervalos considerando las
		// citas existentes
		for (IntervaloDisponibleDTO ventana : ventanasDisponibilidad) {
			List<IntervaloDisponibleDTO> intervalosEnVentana = generarIntervalosEnVentana(ventana, citasExistentes,
					duracionMinutos);
			intervalosDisponibles.addAll(intervalosEnVentana);
		}

		return intervalosDisponibles;
	}


	 //Combina disponibilidades base y especiales
	private List<IntervaloDisponibleDTO> combinarDisponibilidades(LocalDate fecha,
	        List<DisponibilidadBaseDTO> disponibilidadesBase,
	        List<DisponibilidadEspecialDTO> disponibilidadesEspeciales) {
	    List<IntervaloDisponibleDTO> ventanas = new ArrayList<>();

	    // Procesar disponibilidades base
	    for (DisponibilidadBaseDTO base : disponibilidadesBase) {
	        LocalTime inicio = LocalTime.parse(base.getHoraInicio());
	        LocalTime fin = LocalTime.parse(base.getHoraFin());
	        
	        // Crear ventana base
	        IntervaloDisponibleDTO ventanaBase = new IntervaloDisponibleDTO();
	        ventanaBase.setFecha(fecha);
	        ventanaBase.setHoraInicio(inicio);
	        ventanaBase.setHoraFin(fin);
	        
	        // Aplicar bloques a esta ventana base
	        List<IntervaloDisponibleDTO> ventanasSinBloqueos = aplicarBloquesAVentana(ventanaBase, disponibilidadesEspeciales);
	        ventanas.addAll(ventanasSinBloqueos);
	    }

	    // Procesar disponibilidades especiales (solo las de tipo EXTRA)
	    for (DisponibilidadEspecialDTO especial : disponibilidadesEspeciales) {
	        if (especial.getTipo() == DisponibilidadEspecialDTO.TipoDisponibilidad.EXTRA) {
	            LocalTime inicio = LocalTime.parse(especial.getHoraInicio());
	            LocalTime fin = LocalTime.parse(especial.getHoraFin());
	            
	            IntervaloDisponibleDTO intervalo = new IntervaloDisponibleDTO();
	            intervalo.setFecha(fecha);
	            intervalo.setHoraInicio(inicio);
	            intervalo.setHoraFin(fin);
	            ventanas.add(intervalo);
	        }
	    }

	    return ventanas;
	}
	
	private List<IntervaloDisponibleDTO> aplicarBloquesAVentana(IntervaloDisponibleDTO ventana,
	        List<DisponibilidadEspecialDTO> disponibilidadesEspeciales) {
	    List<IntervaloDisponibleDTO> ventanasResultado = new ArrayList<>();
	    ventanasResultado.add(ventana); // Inicialmente, la ventana completa está disponible

	    // Aplicar cada bloqueo
	    for (DisponibilidadEspecialDTO especial : disponibilidadesEspeciales) {
	        if (especial.getTipo() == DisponibilidadEspecialDTO.TipoDisponibilidad.BLOQUEO) {
	            LocalTime inicioBloqueo = LocalTime.parse(especial.getHoraInicio());
	            LocalTime finBloqueo = LocalTime.parse(especial.getHoraFin());
	            
	            // Aplicar este bloqueo a todas las ventanas actuales
	            List<IntervaloDisponibleDTO> ventanasActualizadas = new ArrayList<>();
	            for (IntervaloDisponibleDTO ventanaActual : ventanasResultado) {
	                ventanasActualizadas.addAll(aplicarBloqueoIndividual(ventanaActual, inicioBloqueo, finBloqueo));
	            }
	            ventanasResultado = ventanasActualizadas;
	        }
	    }

	    return ventanasResultado;
	}
	
	private List<IntervaloDisponibleDTO> aplicarBloqueoIndividual(IntervaloDisponibleDTO ventana,
	        LocalTime inicioBloqueo, LocalTime finBloqueo) {
	    List<IntervaloDisponibleDTO> resultado = new ArrayList<>();
	    
	    LocalTime inicioVentana = ventana.getHoraInicio();
	    LocalTime finVentana = ventana.getHoraFin();
	    
	    // Si no hay solapamiento, mantener la ventana completa
	    if (finBloqueo.isBefore(inicioVentana) || inicioBloqueo.isAfter(finVentana)) {
	        resultado.add(ventana);
	        return resultado;
	    }
	    
	    // Si el bloqueo cubre completamente la ventana, no hay disponibilidad
	    if (inicioBloqueo.isBefore(inicioVentana) && finBloqueo.isAfter(finVentana)) {
	        return resultado; // Lista vacía
	    }
	    
	    // Dividir la ventana alrededor del bloqueo
	    // Parte antes del bloqueo
	    if (inicioVentana.isBefore(inicioBloqueo)) {
	        IntervaloDisponibleDTO antes = new IntervaloDisponibleDTO();
	        antes.setFecha(ventana.getFecha());
	        antes.setHoraInicio(inicioVentana);
	        antes.setHoraFin(inicioBloqueo);
	        resultado.add(antes);
	    }
	    
	    // Parte después del bloqueo
	    if (finBloqueo.isBefore(finVentana)) {
	        IntervaloDisponibleDTO despues = new IntervaloDisponibleDTO();
	        despues.setFecha(ventana.getFecha());
	        despues.setHoraInicio(finBloqueo);
	        despues.setHoraFin(finVentana);
	        resultado.add(despues);
	    }
	    
	    return resultado;
	}

	
	 //Genera intervalos dentro de una ventana de disponibilidad
	private List<IntervaloDisponibleDTO> generarIntervalosEnVentana(IntervaloDisponibleDTO ventana,
			List<CitaDTO> citasExistentes, Integer duracionMinutos) {
		List<IntervaloDisponibleDTO> intervalos = new ArrayList<>();

		LocalTime horaActual = ventana.getHoraInicio();
		LocalTime horaFinVentana = ventana.getHoraFin();

		while (horaActual.plusMinutes(duracionMinutos).isBefore(horaFinVentana)
				|| horaActual.plusMinutes(duracionMinutos).equals(horaFinVentana)) {

			LocalTime horaFinIntervalo = horaActual.plusMinutes(duracionMinutos);

			// Verificar si el intervalo está libre de citas
			if (estaIntervaloDisponible(horaActual, horaFinIntervalo, citasExistentes)) {
			    IntervaloDisponibleDTO intervalo = new IntervaloDisponibleDTO();
			    intervalo.setFecha(ventana.getFecha());
			    intervalo.setHoraInicio(horaActual);
			    intervalo.setHoraFin(horaFinIntervalo);
			    intervalos.add(intervalo);
			}

			// Avanzar en intervalos de la duración del servicio
			horaActual = horaActual.plusMinutes(duracionMinutos);
		}

		return intervalos;
	}


	// Verifica si un intervalo está disponible (no hay citas que se solapen)
	private boolean estaIntervaloDisponible(LocalTime inicioIntervalo, LocalTime finIntervalo,
			List<CitaDTO> citasExistentes) {
		for (CitaDTO cita : citasExistentes) {
			// Solo considerar citas que no estén canceladas
			if (cita.getEstado() != CitaDTO.EstadoCita.CANCELADA) {
				// Verificar solapamiento
				if (seSolapan(inicioIntervalo, finIntervalo, cita.getHoraInicio(), cita.getHoraFin())) {
					return false;
				}
			}
		}
		return true;
	}

	
	 //Verifica si dos intervalos de tiempo se solapan
	
	private boolean seSolapan(LocalTime inicio1, LocalTime fin1, LocalTime inicio2, LocalTime fin2) {
		return (inicio1.isBefore(fin2) && fin1.isAfter(inicio2));
	}

	
 //Obtiene disponibilidad para un rango de fechas
	public List<DisponibilidadCitaDTO> obtenerIntervalosDisponiblesRango(Long trabajadorId, Long servicioId,
			LocalDate fechaInicio, LocalDate fechaFin) {
		List<DisponibilidadCitaDTO> resultado = new ArrayList<>();

		LocalDate fechaActual = fechaInicio;
		while (!fechaActual.isAfter(fechaFin)) {
			try {
				DisponibilidadCitaDTO disponibilidadDia = obtenerIntervalosDisponibles(trabajadorId, servicioId,
						fechaActual);
				resultado.add(disponibilidadDia);
			} catch (RuntimeException e) {
				// Si hay error para un día específico, continuar con los demás
				System.out.println("Error obteniendo disponibilidad para " + fechaActual + ": " + e.getMessage());
			}
			fechaActual = fechaActual.plusDays(1);
		}

		return resultado;
	}

}
