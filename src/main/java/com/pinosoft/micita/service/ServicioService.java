package com.pinosoft.micita.service;

import com.pinosoft.micita.dto.ServicioDTO;
import com.pinosoft.micita.model.Negocio;
import com.pinosoft.micita.model.Servicio;
import com.pinosoft.micita.repository.ServicioRepository;
import com.pinosoft.micita.repository.NegocioRepository;
import com.pinosoft.micita.model.CategoriaServicio;
import com.pinosoft.micita.repository.CategoriaServicioRepository;
import com.pinosoft.micita.dto.ServicioClienteDTO;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ServicioService {

	private final ServicioRepository servicioRepository;
	private final NegocioRepository negocioRepository;
	private final CategoriaServicioRepository categoriaServicioRepository;

	public ServicioService(ServicioRepository servicioRepository, NegocioRepository negocioRepository,
			CategoriaServicioRepository categoriaServicioRepository) {
		this.servicioRepository = servicioRepository;
		this.negocioRepository = negocioRepository;
		this.categoriaServicioRepository = categoriaServicioRepository;
	}

	// Convertir Entity a ServicioDTO (para administradores)
	private ServicioDTO convertToDTO(Servicio servicio) {
		ServicioDTO dto = new ServicioDTO();
		dto.setId(servicio.getId());
		dto.setNombre(servicio.getNombre());
		dto.setDescripcion(servicio.getDescripcion());
		dto.setPrecio(servicio.getPrecio());
		dto.setDuracionMinutos(servicio.getDuracionMinutos());
		dto.setNegocioId(servicio.getNegocio().getId());
		dto.setCategoriaServicioId(servicio.getCategoriaServicio().getId());
		dto.setEstado(servicio.getEstado());
		return dto;
	}

	// Convertir Entity a ServicioClienteDTO (para clientes)
	private ServicioClienteDTO convertToClienteDTO(Servicio servicio) {
		ServicioClienteDTO dto = new ServicioClienteDTO();
		dto.setId(servicio.getId());
		dto.setNombre(servicio.getNombre());
		dto.setDescripcion(servicio.getDescripcion());
		dto.setPrecio(servicio.getPrecio());
		dto.setDuracionMinutos(servicio.getDuracionMinutos());
		return dto;
	}

	// Convertir ServicioDTO a Entity
	private Servicio convertToEntity(ServicioDTO dto) {
		Servicio servicio = new Servicio();
		servicio.setId(dto.getId());
		servicio.setNombre(dto.getNombre());
		servicio.setDescripcion(dto.getDescripcion());
		servicio.setPrecio(dto.getPrecio());
		servicio.setDuracionMinutos(dto.getDuracionMinutos());
		servicio.setEstado(dto.getEstado());

		// Asignar negocio
		if (dto.getNegocioId() != null) {
			Negocio negocio = new Negocio();
			negocio.setId(dto.getNegocioId());
			servicio.setNegocio(negocio);
		}

		// Asignar categoría de servicio
		if (dto.getCategoriaServicioId() != null) {
			CategoriaServicio categoriaServicio = new CategoriaServicio();
			categoriaServicio.setId(dto.getCategoriaServicioId());
			servicio.setCategoriaServicio(categoriaServicio);
		}

		return servicio;
	}

	// Validar servicio
	private void validarServicio(ServicioDTO dto) {
		if (dto.getPrecio() <= 0) {
			throw new RuntimeException("El precio debe ser mayor a cero");
		}

		if (dto.getDuracionMinutos() <= 0) {
			throw new RuntimeException("La duración debe ser mayor a cero");
		}

		if (servicioRepository.existsByNombreAndNegocioId(dto.getNombre(), dto.getNegocioId())) {
			throw new RuntimeException("Ya existe un servicio con este nombre en el negocio");
		}
	}

	// Crear nuevo servicio
	@Transactional
	public ServicioDTO crearServicio(ServicioDTO servicioDTO) {
		validarServicio(servicioDTO);

		// Verificar que el negocio existe
		Negocio negocio = negocioRepository.findById(servicioDTO.getNegocioId())
				.orElseThrow(() -> new RuntimeException("Negocio no encontrado"));

		// Verificar que la categoría existe
		CategoriaServicio categoriaServicio = categoriaServicioRepository.findById(servicioDTO.getCategoriaServicioId())
				.orElseThrow(() -> new RuntimeException("Categoría de servicio no encontrada"));

		Servicio servicio = convertToEntity(servicioDTO);
		servicio.setNegocio(negocio);
		servicio.setCategoriaServicio(categoriaServicio);

		Servicio saved = servicioRepository.save(servicio);
		return convertToDTO(saved);
	}

	// Obtener servicio por ID como DTO de administrador
	public ServicioDTO obtenerPorId(Long id) {
		Servicio servicio = servicioRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Servicio no encontrado"));
		return convertToDTO(servicio);
	}

	// Obtener servicio por ID como DTO de cliente
	public ServicioClienteDTO obtenerPorIdParaCliente(Long id) {
		Servicio servicio = servicioRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Servicio no encontrado"));
		return convertToClienteDTO(servicio);
	}

	// Obtener todos los servicios de un negocio como DTOs de administrador
	public List<ServicioDTO> obtenerPorNegocio(Long negocioId) {
		if (!negocioRepository.existsById(negocioId)) {
			throw new RuntimeException("Negocio no encontrado");
		}
		return servicioRepository.findByNegocioId(negocioId).stream().map(this::convertToDTO)
				.collect(Collectors.toList());
	}

	// Obtener todos los servicios de un negocio como DTOs de cliente
	public List<ServicioClienteDTO> obtenerPorNegocioParaCliente(Long negocioId) {
		if (!negocioRepository.existsById(negocioId)) {
			throw new RuntimeException("Negocio no encontrado");
		}
		return servicioRepository.findByNegocioId(negocioId).stream().map(this::convertToClienteDTO)
				.collect(Collectors.toList());
	}

	// Obtener servicios por negocio y estado como DTOs de administrador
	public List<ServicioDTO> obtenerPorNegocioYEstado(Long negocioId, Boolean estado) {
		if (!negocioRepository.existsById(negocioId)) {
			throw new RuntimeException("Negocio no encontrado");
		}
		return servicioRepository.findByNegocioIdAndEstado(negocioId, estado).stream().map(this::convertToDTO)
				.collect(Collectors.toList());
	}

	// Obtener servicios por negocio y estado como DTOs de cliente
	public List<ServicioClienteDTO> obtenerPorNegocioYEstadoParaCliente(Long negocioId, Boolean estado) {
		if (!negocioRepository.existsById(negocioId)) {
			throw new RuntimeException("Negocio no encontrado");
		}
		return servicioRepository.findByNegocioIdAndEstado(negocioId, estado).stream().map(this::convertToClienteDTO)
				.collect(Collectors.toList());
	}

	// Actualizar servicio
	@Transactional
	public ServicioDTO actualizarServicio(Long id, ServicioDTO servicioDTO) {
		Servicio servicioExistente = servicioRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Servicio no encontrado"));

		// Verificar si el nombre ya existe para otro servicio en el mismo negocio
		if (!servicioExistente.getNombre().equals(servicioDTO.getNombre())
				&& servicioRepository.existsByNombreAndNegocioId(servicioDTO.getNombre(), servicioDTO.getNegocioId())) {
			throw new RuntimeException("Ya existe un servicio con este nombre en el negocio");
		}

		// Verificar que la categoría existe
		CategoriaServicio categoriaServicio = categoriaServicioRepository.findById(servicioDTO.getCategoriaServicioId())
				.orElseThrow(() -> new RuntimeException("Categoría de servicio no encontrada"));

		servicioExistente.setNombre(servicioDTO.getNombre());
		servicioExistente.setDescripcion(servicioDTO.getDescripcion());
		servicioExistente.setPrecio(servicioDTO.getPrecio());
		servicioExistente.setDuracionMinutos(servicioDTO.getDuracionMinutos());
		servicioExistente.setCategoriaServicio(categoriaServicio);
		servicioExistente.setEstado(servicioDTO.getEstado());

		Servicio updated = servicioRepository.save(servicioExistente);
		return convertToDTO(updated);
	}

	// Actualizar estado del servicio
	@Transactional
	public ServicioDTO actualizarEstadoServicio(Long id, Boolean estado) {
		Servicio servicio = servicioRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Servicio no encontrado"));

		servicio.setEstado(estado);
		Servicio updated = servicioRepository.save(servicio);
		return convertToDTO(updated);
	}

	// Eliminar servicio
	@Transactional
	public void eliminarServicio(Long id) {
		Servicio servicio = servicioRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Servicio no encontrado"));

		// Verificar si el servicio tiene citas asociadas
		if (servicioRepository.tieneCitasAsociadas(id)) {
			throw new RuntimeException("No se puede eliminar el servicio porque tiene citas asociadas");
		}

		servicioRepository.delete(servicio);
	}

	// Verificar si existe servicio por nombre y negocio
	public boolean existePorNombreYNegocio(String nombre, Long negocioId) {
		return servicioRepository.existsByNombreAndNegocioId(nombre, negocioId);
	}

	// Verificar si tiene citas asociadas
	public boolean tieneCitasAsociadas(Long servicioId) {
		return servicioRepository.tieneCitasAsociadas(servicioId);
	}
}
