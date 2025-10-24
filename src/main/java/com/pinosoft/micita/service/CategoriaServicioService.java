package com.pinosoft.micita.service;
import com.pinosoft.micita.dto.CategoriaServicioDTO;
import com.pinosoft.micita.exception.CitaException;
import com.pinosoft.micita.model.CategoriaNegocio;
import com.pinosoft.micita.model.CategoriaServicio;
import com.pinosoft.micita.repository.CategoriaNegocioRepository;
import com.pinosoft.micita.repository.CategoriaServicioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoriaServicioService {
	
	private final CategoriaServicioRepository categoriaServicioRepository;
    private final CategoriaNegocioRepository categoriaNegocioRepository;

    public CategoriaServicioService(CategoriaServicioRepository categoriaServicioRepository,
                                     CategoriaNegocioRepository categoriaNegocioRepository) {
        this.categoriaServicioRepository = categoriaServicioRepository;
        this.categoriaNegocioRepository = categoriaNegocioRepository;
    }

    public List<CategoriaServicio> obtenerPorCategoriaNegocio(Long idCategoria) {
        return categoriaServicioRepository.findByCategoriaNegocioId(idCategoria);
    }

    @Transactional
    public CategoriaServicio crear(CategoriaServicioDTO dto) {
        CategoriaNegocio negocio = categoriaNegocioRepository.findById(dto.getCategoriaNegocioId())
                .orElseThrow(() -> CitaException.resourceNotFound("Categoría de negocio no encontrada"));

        if (categoriaServicioRepository.existsByNombreAndCategoriaNegocioId(dto.getNombre(), dto.getCategoriaNegocioId())) {
            throw CitaException.businessRuleViolation("Ya existe una subcategoría con este nombre para este negocio");
        }

        CategoriaServicio servicio = new CategoriaServicio();
        servicio.setNombre(dto.getNombre());
        servicio.setDescripcion(dto.getDescripcion());
        servicio.setCategoriaNegocio(negocio);
        return categoriaServicioRepository.save(servicio);
    }

    public CategoriaServicio buscarPorId(Long id) {
        return categoriaServicioRepository.findById(id)
                .orElseThrow(() -> CitaException.resourceNotFound("Categoría de servicio no encontrada"));
    }

}
