package com.pinosoft.micita.repository;

import com.pinosoft.micita.model.CategoriaServicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoriaServicioRepository extends JpaRepository<CategoriaServicio, Long> {
	List<CategoriaServicio> findByCategoriaNegocioId(Long categoriaNegocioId);

	boolean existsByNombreAndCategoriaNegocioId(String nombre, Long categoriaNegocioId);
}