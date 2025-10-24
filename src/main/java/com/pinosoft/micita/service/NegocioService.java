package com.pinosoft.micita.service;

import com.pinosoft.micita.dto.NegocioDTO;
import com.pinosoft.micita.model.CategoriaNegocio;
import com.pinosoft.micita.model.Negocio;
import com.pinosoft.micita.model.Usuario;
import com.pinosoft.micita.repository.CategoriaNegocioRepository;
import com.pinosoft.micita.repository.NegocioRepository;
import com.pinosoft.micita.security.CustomUserDetailsService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.pinosoft.micita.repository.UsuarioRepository;
import java.util.List;

@Service
public class NegocioService {
	private final NegocioRepository negocioRepository;
	private final CustomUserDetailsService customUserDetailsService;
	private final UsuarioRepository usuarioRepository;
	private final CategoriaNegocioRepository categoriaNegocioRepository;

	public NegocioService(NegocioRepository negocioRepository, CustomUserDetailsService customUserDetailsService,
			UsuarioRepository usuarioRepository, CategoriaNegocioRepository categoriaNegocioRepository) {
		this.negocioRepository = negocioRepository;
		this.customUserDetailsService = customUserDetailsService;
		this.usuarioRepository = usuarioRepository;
		this.categoriaNegocioRepository = categoriaNegocioRepository;
	}

	@Transactional
	public Negocio crearNegocio(NegocioDTO negocioDTO, Authentication authentication) {
		Long administradorId = customUserDetailsService.getUserIdFromAuthentication(authentication);

		// Carga el usuario completo desde la base de datos
		Usuario administrador = usuarioRepository.findById(administradorId)
				.orElseThrow(() -> new RuntimeException("Administrador no encontrado"));

		// Verifica que sea administrador
		if (!"administrador".equalsIgnoreCase(administrador.getRol())) {
			throw new RuntimeException("El usuario no tiene rol de administrador");
		}

		if (negocioRepository.existsByNombreAndAdministradorId(negocioDTO.getNombre(), administradorId)) {
			throw new RuntimeException("Ya tienes un negocio con este nombre");
		}

		CategoriaNegocio categoriaNegocio = categoriaNegocioRepository.findById(negocioDTO.getCategoriaNegocioId())
				.orElseThrow(() -> new RuntimeException("Categoría de negocio no encontrada"));

		Negocio negocio = new Negocio();
		negocio.setNombre(negocioDTO.getNombre());
		negocio.setDireccion(negocioDTO.getDireccion());
		negocio.setTelefono(negocioDTO.getTelefono());
		negocio.setAdministrador(administrador); // Asigna el objeto completo
		negocio.setCategoriaNegocio(categoriaNegocio); // Asigna la categoría de negocio

		return negocioRepository.save(negocio);
	}

	@Transactional(readOnly = true)
	public List<Negocio> obtenerTodosNegocios() {
		return negocioRepository.findAll();
	}

	@Transactional(readOnly = true)
	public List<Negocio> obtenerNegociosDelAdministrador(Authentication authentication) {
		Long administradorId = customUserDetailsService.getUserIdFromAuthentication(authentication);
		return negocioRepository.findByAdministradorId(administradorId);
	}

	@Transactional(readOnly = true)
	public Negocio obtenerNegocioPorId(Long id, Authentication authentication) {
		Long administradorId = customUserDetailsService.getUserIdFromAuthentication(authentication);
		return negocioRepository.findByIdAndAdministradorId(id, administradorId)
				.orElseThrow(() -> new RuntimeException("Negocio no encontrado o no tienes permisos"));
	}

	@Transactional
	public Negocio actualizarNegocio(Long id, NegocioDTO negocioDTO, Authentication authentication) {
		Long administradorId = customUserDetailsService.getUserIdFromAuthentication(authentication);

		// Verificar que el negocio exista y pertenezca al administrador
		Negocio negocio = negocioRepository.findByIdAndAdministradorId(id, administradorId)
				.orElseThrow(() -> new RuntimeException("Negocio no encontrado o no tienes permisos"));

		// Verificar si el nuevo nombre ya está en uso por otro negocio del mismo
		// administrador
		if (!negocio.getNombre().equals(negocioDTO.getNombre())
				&& negocioRepository.existsByNombreAndAdministradorId(negocioDTO.getNombre(), administradorId)) {
			throw new RuntimeException("Ya tienes otro negocio con este nombre");
		}


		CategoriaNegocio categoriaNegocio = categoriaNegocioRepository.findById(negocioDTO.getCategoriaNegocioId())
				.orElseThrow(() -> new RuntimeException("Categoría de negocio no encontrada"));
		
		
		
		
		// Actualizar los datos
		negocio.setNombre(negocioDTO.getNombre());
		negocio.setDireccion(negocioDTO.getDireccion());
		negocio.setTelefono(negocioDTO.getTelefono());
		negocio.setCategoriaNegocio(categoriaNegocio);

		return negocioRepository.save(negocio);
	}

	@Transactional
	public void eliminarNegocio(Long id, Authentication authentication) {
		Long administradorId = customUserDetailsService.getUserIdFromAuthentication(authentication);

		// Verificar que el negocio exista y pertenezca al administrador
		Negocio negocio = negocioRepository.findByIdAndAdministradorId(id, administradorId)
				.orElseThrow(() -> new RuntimeException("Negocio no encontrado o no tienes permisos"));

		negocioRepository.delete(negocio);
	}

	@Transactional(readOnly = true)
	public boolean existeNegocioParaAdministrador(Long negocioId, Authentication authentication) {
		Long administradorId = customUserDetailsService.getUserIdFromAuthentication(authentication);
		return negocioRepository.existsByIdAndAdministradorId(negocioId, administradorId);
	}

}
