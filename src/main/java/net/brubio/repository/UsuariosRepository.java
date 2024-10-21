package net.brubio.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import net.brubio.model.Usuario;

public interface UsuariosRepository extends JpaRepository<Usuario, Integer> {
	
	Usuario findByUsername(String username);

}
