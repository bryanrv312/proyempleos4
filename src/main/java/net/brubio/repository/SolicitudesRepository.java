package net.brubio.repository;

import net.brubio.model.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import net.brubio.model.Solicitud;

import java.util.Optional;

public interface SolicitudesRepository extends JpaRepository<Solicitud, Integer>{

    Page<Solicitud> findByUsuario(Usuario usuario, Pageable pageable);

    Optional<Solicitud> findByUsuarioIdAndVacanteId(int usuarioId, int vacanteId);
	
}
