package com.pinosoft.micita.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import lombok.RequiredArgsConstructor;
import com.pinosoft.micita.dto.DisponibilidadCitaDTO;
import com.pinosoft.micita.service.DisponibilidadCitaService;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/disponibilidad-citas")
@RequiredArgsConstructor
public class DisponibilidadCitaController {
private final DisponibilidadCitaService disponibilidadCitaService;
    
    @GetMapping("/intervalos")
    public ResponseEntity<DisponibilidadCitaDTO> obtenerIntervalosDisponibles(
            @RequestParam Long trabajadorId,
            @RequestParam Long servicioId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        
        try {
            DisponibilidadCitaDTO intervalos = 
                disponibilidadCitaService.obtenerIntervalosDisponibles(trabajadorId, servicioId, fecha);
            return ResponseEntity.ok(intervalos);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/intervalos/rango")
    public ResponseEntity<List<DisponibilidadCitaDTO>> obtenerIntervalosDisponiblesRango(
            @RequestParam Long trabajadorId,
            @RequestParam Long servicioId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        
        try {
            List<DisponibilidadCitaDTO> intervalos = 
                disponibilidadCitaService.obtenerIntervalosDisponiblesRango(trabajadorId, servicioId, fechaInicio, fechaFin);
            return ResponseEntity.ok(intervalos);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

}
