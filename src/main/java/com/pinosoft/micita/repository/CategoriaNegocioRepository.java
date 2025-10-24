package com.pinosoft.micita.repository;

import com.pinosoft.micita.model.CategoriaNegocio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoriaNegocioRepository extends JpaRepository<CategoriaNegocio, Long> {
	
	
	boolean existsByNombre(String nombre);

	Optional<CategoriaNegocio> findByNombre(String nombre);
	Optional<CategoriaNegocio> findById(Long id);
}
