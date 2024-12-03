package net.brubio.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import net.brubio.model.Usuario;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UsuariosRepository extends JpaRepository<Usuario, Integer> {
	
	Usuario findByUsername(String username);

	Usuario findByToken(String token);

}
