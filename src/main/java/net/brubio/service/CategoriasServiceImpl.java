package net.brubio.service;

import java.util.LinkedList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import net.brubio.model.Categoria;


@Service
public class CategoriasServiceImpl implements ICategoriasService{
	
	private List<Categoria> lista = null;
	
	
	public CategoriasServiceImpl() {
		lista = new LinkedList<>();
		
		try {
			Categoria categoria1 = new Categoria();
			categoria1.setId(1);
			categoria1.setNombre("Ventas");
			categoria1.setDescripcion("vendedores, comercio, ...");
			
			Categoria categoria2 = new Categoria();
			categoria2.setId(2);
			categoria2.setNombre("Contabilidad");
			categoria2.setDescripcion("contadores, administradores, etc");
			
			Categoria categoria3 = new Categoria();
			categoria3.setId(3);
			categoria3.setNombre("Informática");
			categoria3.setDescripcion("Programación y más");
			
			lista.add(categoria1);
			lista.add(categoria2);
			lista.add(categoria3);
			
		} catch (Exception e) {
			System.out.print("Error: " + e.getMessage());
		}
	}


	@Override
	public void guardar(Categoria categoria) {
		lista.add(categoria);	
	}

	
	@Override
	public List<Categoria> buscarTodas() {	
		return lista;
	}

	
	@Override
	public Categoria buscarPorId(Integer idCategoria) {
		for(Categoria cate : lista) {
			if(cate.getId() == idCategoria) {
				return cate;
			}
		}
		return null;
	}


	@Override
	public void eliminar(Integer idCategoria) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public Page<Categoria> buscarTodas(Pageable page) {
		// TODO Auto-generated method stub
		return null;
	}

}
