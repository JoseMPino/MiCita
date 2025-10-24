package com.pinosoft.micita.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.Collection;
import java.util.Collections;

@Entity
@Table(name = "usuarios")
@Data
public class Usuario {

	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;
	    
	    @Column(nullable = false)
	    private String nombres;
	    
	    @Column(nullable = false)
	    private String apellidos;
	    
	    @Column(nullable = false, unique = true)
	    private String correo;
	    
	    @Column(nullable = false)
	    private String contrasena;
	    
	    @Column(nullable = false)
	    private String rol = "cliente"; // 'cliente', 'trabajador', 'administrador'
	    
	    @Column(name = "telefono", nullable = false, unique = true)
	    private String telefono;
	    
	    @Column(name = "negocio_id")
	    private Long negocioId; // Solo para trabajadores y administradores
	    
	    @CreationTimestamp
	    @Column(name = "creado_en", updatable = false)
	    private LocalDateTime creadoEn;
	
	    public Collection<? extends GrantedAuthority> getAuthorities() {
	        // Añadida validación para rol nulo
	        String role = (this.rol != null) ? this.rol : "cliente";
	        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()));
	    }
	    @Column(name ="estado",nullable = false)
	    private boolean estado;
	    
}


