package com.pinosoft.micita.controller;
import com.pinosoft.micita.dto.AuthRequestDTO;
import com.pinosoft.micita.dto.AuthResponseDTO;
import com.pinosoft.micita.dto.UsuarioDTO;
import com.pinosoft.micita.model.Usuario;
import com.pinosoft.micita.repository.UsuarioRepository;
import com.pinosoft.micita.service.AuthService;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	 private final AuthService authService;


	    public AuthController(AuthService authService, UsuarioRepository usuarioRepository) {
	        this.authService = authService;
	    }
	  
	  

	    @PostMapping("/login")
	    public ResponseEntity<AuthResponseDTO> login(@RequestBody AuthRequestDTO authRequest) {
	        AuthResponseDTO response = authService.login(authRequest);
	        return ResponseEntity.ok(response);
	    }

	    @PostMapping("/registro")
	    public ResponseEntity<Usuario> register(@RequestBody UsuarioDTO usuarioDTO) {
	        Usuario usuario = authService.register(usuarioDTO);
	        return new ResponseEntity<>(usuario, HttpStatus.CREATED);
	    }
	    
	    @GetMapping("/usuario/{correo}")
	    public ResponseEntity<Usuario> obtenerUsuarioPorCorreo(@PathVariable String correo) {
	        Optional<Usuario> usuario = authService.buscarPorCorreo(correo);
	        return usuario.map(ResponseEntity::ok)
	                     .orElse(ResponseEntity.notFound().build());
	    }
	    
	    
	    @PutMapping("/usuario/{id}")
	    public ResponseEntity<Usuario> actualizarUsuario(
	            @PathVariable Long id, 
	            @RequestBody UsuarioDTO usuarioDTO) {
	        try {
	            Usuario usuarioActualizado = authService.actualizarUsuario(id, usuarioDTO);
	            return ResponseEntity.ok(usuarioActualizado);
	        } catch (RuntimeException e) {
	            return ResponseEntity.notFound().build();
	        }
	    }
	
}


