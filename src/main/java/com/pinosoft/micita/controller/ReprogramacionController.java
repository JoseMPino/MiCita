package com.pinosoft.micita.controller;

import com.pinosoft.micita.dto.ReprogramacionDTO;
import com.pinosoft.micita.model.Reprogramacion;
import com.pinosoft.micita.service.ReprogramacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/reprogramaciones")
@RequiredArgsConstructor
public class ReprogramacionController {
	private final ReprogramacionService reprogramacionService;

    @PostMapping
    public ResponseEntity<ReprogramacionDTO> crearReprogramacion(@RequestBody ReprogramacionDTO reprogramacionDTO) {
        ReprogramacionDTO nuevaReprogramacion = reprogramacionService.crearReprogramacion(reprogramacionDTO);
        return ResponseEntity.ok(nuevaReprogramacion);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReprogramacionDTO> obtenerPorId(@PathVariable Long id) {
        ReprogramacionDTO reprogramacion = reprogramacionService.obtenerPorId(id);
        return ResponseEntity.ok(reprogramacion);
    }

    @GetMapping("/cita/{idCita}")
    public ResponseEntity<List<ReprogramacionDTO>> obtenerPorCita(@PathVariable Long idCita) {
        List<ReprogramacionDTO> reprogramaciones = reprogramacionService.obtenerPorCita(idCita);
        return ResponseEntity.ok(reprogramaciones);
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<ReprogramacionDTO>> obtenerPorEstado(@PathVariable Reprogramacion.EstadoReprogramacion estado) {
        List<ReprogramacionDTO> reprogramaciones = reprogramacionService.obtenerPorEstado(estado);
        return ResponseEntity.ok(reprogramaciones);
    }

    @GetMapping("/pendientes")
    public ResponseEntity<List<ReprogramacionDTO>> obtenerPendientes() {
        List<ReprogramacionDTO> reprogramaciones = reprogramacionService.obtenerPendientes();
        return ResponseEntity.ok(reprogramaciones);
    }

    @GetMapping("/aprobadas")
    public ResponseEntity<List<ReprogramacionDTO>> obtenerAprobadas() {
        List<ReprogramacionDTO> reprogramaciones = reprogramacionService.obtenerAprobadas();
        return ResponseEntity.ok(reprogramaciones);
    }

    @GetMapping("/rechazadas")
    public ResponseEntity<List<ReprogramacionDTO>> obtenerRechazadas() {
        List<ReprogramacionDTO> reprogramaciones = reprogramacionService.obtenerRechazadas();
        return ResponseEntity.ok(reprogramaciones);
    }

    @GetMapping("/cita/{idCita}/ultima")
    public ResponseEntity<ReprogramacionDTO> obtenerUltimaPorCita(@PathVariable Long idCita) {
        ReprogramacionDTO reprogramacion = reprogramacionService.obtenerUltimaPorCita(idCita);
        return ResponseEntity.ok(reprogramacion);
    }

    @GetMapping("/cita/{idCita}/pendientes")
    public ResponseEntity<List<ReprogramacionDTO>> obtenerPendientesPorCita(@PathVariable Long idCita) {
        List<ReprogramacionDTO> reprogramaciones = reprogramacionService.obtenerPendientesPorCita(idCita);
        return ResponseEntity.ok(reprogramaciones);
    }

    @PutMapping("/{id}/aprobar")
    public ResponseEntity<ReprogramacionDTO> aprobarReprogramacion(@PathVariable Long id) {
        ReprogramacionDTO reprogramacion = reprogramacionService.aprobarReprogramacion(id);
        return ResponseEntity.ok(reprogramacion);
    }

    @PutMapping("/{id}/rechazar")
    public ResponseEntity<ReprogramacionDTO> rechazarReprogramacion(
            @PathVariable Long id,
            @RequestParam(required = false) String motivo) {
        ReprogramacionDTO reprogramacion = reprogramacionService.rechazarReprogramacion(id, motivo);
        return ResponseEntity.ok(reprogramacion);
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<ReprogramacionDTO> cambiarEstado(
            @PathVariable Long id,
            @RequestParam Reprogramacion.EstadoReprogramacion estado) {
        ReprogramacionDTO reprogramacion = reprogramacionService.cambiarEstado(id, estado);
        return ResponseEntity.ok(reprogramacion);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarReprogramacion(@PathVariable Long id) {
        reprogramacionService.eliminarReprogramacion(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/cita/{idCita}/existe-pendiente")
    public ResponseEntity<Boolean> existeReprogramacionPendiente(@PathVariable Long idCita) {
        boolean existe = reprogramacionService.existeReprogramacionPendiente(idCita);
        return ResponseEntity.ok(existe);
    }

    @GetMapping("/trabajador/{trabajadorId}/pendientes/count")
    public ResponseEntity<Long> contarReprogramacionesPendientesPorTrabajador(@PathVariable Long trabajadorId) {
        long count = reprogramacionService.contarReprogramacionesPendientesPorTrabajador(trabajadorId);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/cita/{idCita}/estado/{estado}")
    public ResponseEntity<List<ReprogramacionDTO>> obtenerPorCitaYEstado(
            @PathVariable Long idCita,
            @PathVariable Reprogramacion.EstadoReprogramacion estado) {
        List<ReprogramacionDTO> reprogramaciones = reprogramacionService.obtenerPorCitaYEstado(idCita, estado);
        return ResponseEntity.ok(reprogramaciones);
    }

}
