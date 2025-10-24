package com.pinosoft.micita.security;

import com.pinosoft.micita.model.Usuario;
import com.pinosoft.micita.repository.UsuarioRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
@Service
public class CustomUserDetailsService implements UserDetailsService {

	 private final UsuarioRepository usuarioRepository;

	    public CustomUserDetailsService(UsuarioRepository usuarioRepository) {
	        this.usuarioRepository = usuarioRepository;
	    }

	    @Override
	    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
	        Usuario usuario = usuarioRepository.findByCorreo(correo)
	                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con correo: " + correo));

	        // Asegurarse que el rol siempre tenga el prefijo ROLE_ y esté en mayúsculas
	        String role = (usuario.getRol() != null) ? 
	                     "ROLE_" + usuario.getRol().toUpperCase() : 
	                     "ROLE_CLIENTE";
	        
	        List<GrantedAuthority> authorities = Collections.singletonList(
	            new SimpleGrantedAuthority(role)
	        );

	        return new User(
	            usuario.getCorreo(),
	            usuario.getContrasena(),
	            true, true, true, true, // cuentas siempre activas
	            authorities
	        );
	    }

	    public Long getUserIdFromAuthentication(Authentication authentication) {
	        String username = authentication.getName();
	        Usuario usuario = usuarioRepository.findByCorreo(username)
	                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
	        return usuario.getId();
	    }
}
