package com.pinosoft.micita.service;

import com.pinosoft.micita.dto.AuthRequestDTO;
import com.pinosoft.micita.dto.AuthResponseDTO;
import com.pinosoft.micita.dto.UsuarioDTO;
import com.pinosoft.micita.model.Usuario;
import com.pinosoft.micita.repository.UsuarioRepository;
import com.pinosoft.micita.security.JwtTokenProvider;

import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

	private final AuthenticationManager authenticationManager;
	private final UsuarioRepository usuarioRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtTokenProvider jwtTokenProvider;

	public AuthService(AuthenticationManager authenticationManager, UsuarioRepository usuarioRepository,
			PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider) {
		this.authenticationManager = authenticationManager;
		this.usuarioRepository = usuarioRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtTokenProvider = jwtTokenProvider;
	}

	public AuthResponseDTO login(AuthRequestDTO authRequest) {
		try {
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(authRequest.getCorreo(), authRequest.getContrasena()));

			SecurityContextHolder.getContext().setAuthentication(authentication);

			Usuario usuario = usuarioRepository.findByCorreo(authRequest.getCorreo())
					.orElseThrow(() -> new BadCredentialsException("Credenciales inválidas"));

			// Validación adicional del rol
			if (usuario.getRol() == null) {
				throw new RuntimeException("El usuario no tiene rol asignado");
			}

			String token = jwtTokenProvider.generateToken(authentication);

			return new AuthResponseDTO(token, usuario.getCorreo(), usuario.getRol());
		} catch (Exception e) {
			throw new BadCredentialsException("Error en autenticación: " + e.getMessage());
		}
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

	public Usuario register(UsuarioDTO usuarioDTO) {
		try {
			if (usuarioRepository.existsByCorreo(usuarioDTO.getCorreo())) {
				throw new DataIntegrityViolationException("El correo ya está registrado");
			}

			Usuario usuario = new Usuario();
			usuario.setNombres(usuarioDTO.getNombres());
			usuario.setApellidos(usuarioDTO.getApellidos());
			usuario.setCorreo(usuarioDTO.getCorreo());
			usuario.setEstado(true); // Por defecto, el usuario está activo
			usuario.setContrasena(passwordEncoder.encode(usuarioDTO.getContrasena()));
			usuario.setTelefono(formatearTelefono(usuarioDTO.getTelefono()));

			// Asegurar que el rol nunca sea nulo
			usuario.setRol(usuarioDTO.getRol() != null ? usuarioDTO.getRol() : "cliente");

			usuario.setNegocioId(usuarioDTO.getNegocioId());

			return usuarioRepository.save(usuario);
		} catch (DataIntegrityViolationException e) {
			throw new RuntimeException("Error al registrar usuario: " + e.getMessage());
		}
	}
	
	 public Optional<Usuario> buscarPorCorreo(String correo) {
	        return usuarioRepository.findByCorreo(correo);
	    }
	 
	 public Usuario actualizarUsuario(Long id, UsuarioDTO usuarioDTO) {
	        try {
	            Usuario usuario = usuarioRepository.findById(id)
	                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));

	            // Actualizar campos básicos
	            usuario.setNombres(usuarioDTO.getNombres());
	            usuario.setApellidos(usuarioDTO.getApellidos());
	            usuario.setTelefono(formatearTelefono(usuarioDTO.getTelefono()));
	            usuario.setEstado(usuarioDTO.isEstado());

	            // Actualizar contraseña solo si se proporciona una nueva
	            if (usuarioDTO.getContrasena() != null && !usuarioDTO.getContrasena().trim().isEmpty()) {
	                usuario.setContrasena(passwordEncoder.encode(usuarioDTO.getContrasena()));
	            }

	            // Actualizar rol si se proporciona
	            if (usuarioDTO.getRol() != null) {
	                usuario.setRol(usuarioDTO.getRol());
	            }

	            // Actualizar negocioId si se proporciona
	            if (usuarioDTO.getNegocioId() != null) {
	                usuario.setNegocioId(usuarioDTO.getNegocioId());
	            }

	            return usuarioRepository.save(usuario);
	            
	        } catch (Exception e) {
	            throw new RuntimeException("Error al actualizar usuario: " + e.getMessage());
	        }
	    }

}
