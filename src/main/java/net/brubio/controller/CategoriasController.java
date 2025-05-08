package net.brubio.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
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
		
		return "redirect:/categorias/indexPaginate";
	}
	
	
	@GetMapping("/delete/{id}")
	public String eliminar(@PathVariable("id") int idCategoria, Model model, RedirectAttributes attributes) {
		System.out.println("Borrando Categoría con id: " + idCategoria);
		serviceCategorias.eliminar(idCategoria);
		attributes.addFlashAttribute("msg", "La categoria fue Eliminada !!!");
		return "redirect:/categorias/indexPaginate";
	}
	
	
	@GetMapping("/edit/{id}")
	public String editar(@PathVariable("id") int idCategoria, Model model) {
		Categoria categoria = serviceCategorias.buscarPorId(idCategoria);
		model.addAttribute("categoria", categoria);
		System.out.println(categoria.getNombre());
		return "categorias/formCategoria";
	}

	@GetMapping("/findbyname")
		public String buscarPorNombre(@RequestParam("nombre") String nombreCategoria, Model model, Pageable pageable){
		Optional<Categoria> categoria = serviceCategorias.buscarPorNombreIgnoreCase(nombreCategoria);
		Page<Categoria> pageCategorias;
		if (categoria.isPresent()) {
			pageCategorias = new PageImpl<>(List.of(categoria.get()), pageable, 5); // Página con 1 resultado
			for(Categoria cat : List.of(categoria.get())){
				System.err.println(cat.getNombre());
			}
		} else {
			pageCategorias = Page.empty(); // Página vacía
			model.addAttribute("msg", "No se encontró la categoría con nombre: " + nombreCategoria);
		}
		model.addAttribute("listaCategorias", pageCategorias);
		return "categorias/listCategorias";
	}

	@GetMapping("/findbyname2")
	public String buscarPorNombre2(@RequestParam("nombre") String nombreCategoria, Model model, Pageable pageable){
		List<Categoria> lista = serviceCategorias.buscarPorNombreIgnoreCase2(nombreCategoria);
		Page<Categoria> pageCategorias;
		if (lista.isEmpty()) {
			pageCategorias = Page.empty(); // Página vacía
			model.addAttribute("msg", "No se encontró la categoría con nombre: " + nombreCategoria);
		} else {
			pageCategorias = new PageImpl<>(lista, pageable, 1);
			lista.forEach(cat -> System.err.println(cat.getNombre()));
		}
		model.addAttribute("listaCategorias", pageCategorias);
		return "categorias/listCategorias";
	}

	@GetMapping("/findbyname3")
	public String buscarPorNombre3(@RequestParam("nombre") String nombreCategoria, Model model, Pageable pageable){
//		Page<Categoria> listaPage = serviceCategorias.buscarPorNombreIgnoreCase2(nombreCategoria, pageable);
//		if (listaPage.isEmpty()) {
//			//listaPage = Page.empty(); // Página vacía
//			model.addAttribute("msg", "No se encontró la categoría con nombre: " + nombreCategoria);
//		} else {
//			listaPage = new PageImpl<>(lista, pageable, 1);
//			lista.forEach(cat -> System.err.println(cat.getNombre()));
//		}
		Page<Categoria> listaPage = serviceCategorias.buscarPorNombreIgnoreCase2(nombreCategoria, pageable);
		model.addAttribute("listaCategorias", listaPage);
		return "categorias/listCategorias";
	}


}








