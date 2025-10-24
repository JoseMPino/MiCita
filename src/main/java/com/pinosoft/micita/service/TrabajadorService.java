package com.pinosoft.micita.service;


import com.pinosoft.micita.dto.TrabajadorDTO;
import com.pinosoft.micita.model.Usuario;
import com.pinosoft.micita.repository.UsuarioRepository;
import com.pinosoft.micita.repository.NegocioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class TrabajadorService {
	
	 private final UsuarioRepository usuarioRepository;
	    private final NegocioRepository negocioRepository;
	    private final PasswordEncoder passwordEncoder;

	    public TrabajadorService(UsuarioRepository usuarioRepository, 
	                           NegocioRepository negocioRepository,
	                           PasswordEncoder passwordEncoder) {
	        this.usuarioRepository = usuarioRepository;
	        this.negocioRepository = negocioRepository;
	        this.passwordEncoder = passwordEncoder;
	    }
	    
	    
	    private String formatearTelefono(String telefono) {
	        if (telefono == null) return null;
	        
	        // Eliminar cualquier caracter que no sea número
	        String soloNumeros = telefono.replaceAll("[^0-9]", "");
	        
	        // Formatear como 000-000-0000
	        if (soloNumeros.length() == 10) {
	            return soloNumeros.replaceFirst("(\\d{3})(\\d{3})(\\d{4})", "$1-$2-$3");
	        } else if (soloNumeros.length() > 10) {
	            // Para números internacionales, formatear diferente
	            return soloNumeros.replaceFirst("(\\d{3})(\\d{3})(\\d{4})(.*)", "$1-$2-$3");
	        }
	        
	        // Si no tiene 10 dígitos, devolver como está
	        return telefono;
	    }

	    @Transactional
	    public Usuario registrarTrabajador(TrabajadorDTO trabajadorDTO) {
	        // Verificar que el negocio exista
	        if (!negocioRepository.existsById(trabajadorDTO.getNegocioId())) {
	            throw new RuntimeException("El negocio no existe");
	        }
	        
	        // Verificar que el correo no esté registrado
	        if (usuarioRepository.existsByCorreo(trabajadorDTO.getCorreo())) {
	            throw new RuntimeException("El correo ya está registrado");
	        }
	        
	        // Crear usuario trabajador
	        
	  
	        Usuario trabajador = new Usuario();
	        trabajador.setNombres(trabajadorDTO.getNombres());
	        trabajador.setApellidos(trabajadorDTO.getApellidos());
	        trabajador.setCorreo(trabajadorDTO.getCorreo());
	        trabajador.setContrasena(passwordEncoder.encode(trabajadorDTO.getContrasena())); 
	        trabajador.setTelefono(formatearTelefono(trabajadorDTO.getTelefono()));
	        trabajador.setRol("trabajador");
	        trabajador.setNegocioId(trabajadorDTO.getNegocioId());
	        

	        return usuarioRepository.save(trabajador);
	    }
	    
	    // Método para enviar invitación (simulado)
	    public void enviarInvitacionTrabajador(String correo, Long negocioId) {
	        // En una implementación real, aquí enviarías un correo con un enlace de registro
	        System.out.println("Invitación enviada a " + correo + " para unirse al negocio ID: " + negocioId);
	    }

}
