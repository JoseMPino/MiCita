package com.pinosoft.micita.controller;
import com.pinosoft.micita.dto.TrabajadorDTO;
import com.pinosoft.micita.model.Usuario;
import com.pinosoft.micita.service.TrabajadorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/trabajadores")
public class TrabajadorController {
	
	 private final TrabajadorService trabajadorService;

	    public TrabajadorController(TrabajadorService trabajadorService) {
	        this.trabajadorService = trabajadorService;
	    }

	    @PostMapping
	    @PreAuthorize("hasRole('ADMINISTRADOR')")
	    public ResponseEntity<Usuario> registrarTrabajador(@RequestBody TrabajadorDTO trabajadorDTO) {
	        Usuario trabajador = trabajadorService.registrarTrabajador(trabajadorDTO);
	        return new ResponseEntity<>(trabajador, HttpStatus.CREATED);
	    }

	    @PostMapping("/invitar")
	    @PreAuthorize("hasRole('ADMINISTRADOR')")
	    public ResponseEntity<Void> invitarTrabajador(
	            @RequestParam String correo,
	            @RequestParam Long negocioId) {
	        trabajadorService.enviarInvitacionTrabajador(correo, negocioId);
	        return ResponseEntity.ok().build();
	    }

}
