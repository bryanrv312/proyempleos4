package net.brubio.repository;

import net.brubio.model.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import net.brubio.model.Solicitud;

public interface SolicitudesRepository extends JpaRepository<Solicitud, Integer>{

    Page<Solicitud> findByUsuario(Usuario usuario, Pageable pageable);
	
}
