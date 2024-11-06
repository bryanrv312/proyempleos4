package net.brubio.service;

import java.util.List;

import net.brubio.model.Solicitud;
import net.brubio.model.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IUsuariosService {
	
	void guardar(Usuario usuario);
	
	void eliminar(Integer idUsuario);
	
	List<Usuario> buscarTodos();
	
	Usuario buscarPorUsername(String username);
	
	Usuario buscarPorId(Integer idUsuario);

	Page<Usuario> buscarTodas(Pageable page);

	Usuario buscarUsuarioPorToken(String token);

}
