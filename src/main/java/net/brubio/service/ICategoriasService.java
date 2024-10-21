package net.brubio.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import net.brubio.model.Categoria;

public interface ICategoriasService {
	
	List<Categoria> buscarTodas();
	
	void guardar(Categoria categoria);
	
	Categoria buscarPorId(Integer idCategoria);
	
	void eliminar(Integer idCategoria);
	
	Page<Categoria> buscarTodas(Pageable page);

}
