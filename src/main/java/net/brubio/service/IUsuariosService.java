package net.brubio.service;

import java.util.List;

import net.brubio.model.Usuario;

public interface IUsuariosService {
	
	void guardar(Usuario usuario);
	
	void eliminar(Integer idUsuario);
	
	List<Usuario> buscarTodos();
	
	Usuario buscarPorUsername(String username);
	
	Usuario buscarPorId(Integer idUsuario);

}
