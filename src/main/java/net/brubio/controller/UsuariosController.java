package net.brubio.controller;

import java.util.List;

import net.brubio.model.Vacante;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import net.brubio.model.Usuario;
import net.brubio.service.db.UsuariosServiceJpa;

@Controller
@RequestMapping("/usuarios")
public class UsuariosController {
	
	@Autowired
	private UsuariosServiceJpa serviceUsuarios;
	
	
	@GetMapping("/index")
	public String mostrarIndex(Model model) {
		List<Usuario> lista = serviceUsuarios.buscarTodos();
		model.addAttribute("listaUsuarios", lista);
		return "usuarios/listUsuarios";
	}

	@GetMapping("/indexPaginate")
	public String mostrarIndexPaginado(Model model, Pageable page) {
		Page<Usuario> lista = serviceUsuarios.buscarTodas(page);
		model.addAttribute("listaUsuarios", lista);
		return "usuarios/listUsuarios";
	}
	
	@GetMapping("/delete/{id}")
	public String eliminar(@PathVariable("id") int idUsuario, RedirectAttributes attributes) {
		System.out.println("Borrando usuario con id: " + idUsuario);
		serviceUsuarios.eliminar(idUsuario);
		attributes.addFlashAttribute("msg", "El Usuario fue Eliminado !!!");
		return "redirect:/usuarios/indexPaginate";
	}
	
	
	@GetMapping("/disable/{id}")
	public String bloquear(@PathVariable("id") int idUsuario, RedirectAttributes attributes) {
		System.out.println("BLOQUEANDO usuario con id: " + idUsuario);
		Usuario usuario = serviceUsuarios.buscarPorId(idUsuario);
		usuario.setEstatus(0);
		serviceUsuarios.guardar(usuario);
		attributes.addFlashAttribute("msg", "El Usuario fue Bloqueado !!!");
		return "redirect:/usuarios/indexPaginate";
	}
	
	
	@GetMapping("/enable/{id}")
	public String desbloquear(@PathVariable("id") int idUsuario, RedirectAttributes attributes) {
		System.out.println("DESBLOQUEANDO usuario con id: " + idUsuario);
		Usuario usuario = serviceUsuarios.buscarPorId(idUsuario);
		usuario.setEstatus(1);
		serviceUsuarios.guardar(usuario);
		attributes.addFlashAttribute("msg", "El Usuario fue Desbloqueado !!!");
		return "redirect:/usuarios/indexPaginate";
	}
	
	/*
	 * NOW EVERYTHING IS IN HomeController
	 * 
	@GetMapping("/create")
	public String crear(Usuario usuario) {
		return "usuarios/formRegistro";
	}
	
	
	@PostMapping("/save")
	public String guardar(Usuario usuario, RedirectAttributes attributes) {
		
		attributes.addFlashAttribute("msg", "Registro Guardado");
		usuario.setFechaRegistro(new Date());
		usuario.setEstatus(1);
		
		Perfil perfil = new Perfil();
		perfil.setId(3);		
		usuario.agregar(perfil);
		
		serviceUsuarios.guardar(usuario);
		System.out.println("Usuario: " + usuario);
		return "redirect:/usuarios/index"; 
	}*/

}
