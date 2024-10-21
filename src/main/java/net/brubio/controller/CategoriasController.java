package net.brubio.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import net.brubio.model.Categoria;
import net.brubio.service.ICategoriasService;

@Controller
@RequestMapping(value="/categorias")
public class CategoriasController {
	
	@Autowired
	private ICategoriasService serviceCategorias;
	

	//@RequestMapping(value = "/index", method = RequestMethod.GET)
	@GetMapping("/index")
	public String mostrarIndex(Model model) {
		List<Categoria> lista = serviceCategorias.buscarTodas();
		model.addAttribute("listaCategorias", lista);
		return "categorias/listCategorias";
	}
	
	@GetMapping("/indexPaginate")
	public String mostrarIndexPaginado(Model model, Pageable page) {
		Page<Categoria> lista = serviceCategorias.buscarTodas(page);
		model.addAttribute("listaCategorias", lista);
		return "categorias/listCategorias";
	}

	//@RequestMapping(value = "/create", method = RequestMethod.GET) 
	@GetMapping("/create")
	public String crear(Categoria categoria) {//IMPORTANTE, es necesario el el obj categoria ya q se llamara en el form como ${categoria.id} y demas ..
		return "categorias/formCategoria";
	}

	//@RequestMapping(value = "/save", method = RequestMethod.POST)
	@PostMapping("/save")
	public String guardar(Categoria categoria, BindingResult result, RedirectAttributes attributes) {
		
		if (result.hasErrors()) {
			for (ObjectError error : result.getAllErrors()) {
				System.out.println("Ocurrio un error: " + error.getDefaultMessage());
			}
			return "categorias/formCategoria";
		}
		
		serviceCategorias.guardar(categoria);
		attributes.addFlashAttribute("msg", "Categoria Guardada");
		System.out.println("Categoria: " + categoria);
		
		return "redirect:/categorias/index";
	}
	
	
	@GetMapping("/delete/{id}")
	public String eliminar(@PathVariable("id") int idCategoria, Model model, RedirectAttributes attributes) {
		System.out.println("Borrando Categoría con id: " + idCategoria);
		serviceCategorias.eliminar(idCategoria);
		attributes.addFlashAttribute("msg", "La categoria fue Eliminada !!!");
		return "redirect:/categorias/index";
	}
	
	
	@GetMapping("/edit/{id}")
	public String editar(@PathVariable("id") int idCategoria, Model model) {
		Categoria categoria = serviceCategorias.buscarPorId(idCategoria);
		model.addAttribute("categoria", categoria);
		System.out.println(categoria.getNombre());
		return "categorias/formCategoria";
	}

}








