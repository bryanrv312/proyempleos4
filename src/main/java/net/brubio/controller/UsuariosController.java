package net.brubio.controller;

import java.util.List;
import java.util.UUID;

import net.brubio.model.Vacante;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.*;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import net.brubio.model.Usuario;
import net.brubio.service.db.UsuariosServiceJpa;

@Controller
@RequestMapping("/usuarios")
public class UsuariosController {
	
	@Autowired
	private UsuariosServiceJpa serviceUsuarios;

	@Autowired
	private JavaMailSender javaMailSender;

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	
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


	@GetMapping("/formSetting")
	public String mostrarFormSetting(Authentication auth, Model model){
		Usuario user1 = serviceUsuarios.buscarPorUsername(auth.getName());
		Usuario user2 = serviceUsuarios.buscarPorUsername(auth.getName());
		Usuario user3 = serviceUsuarios.buscarPorUsername(auth.getName());
		model.addAttribute("user1",user1);
		model.addAttribute("user2",user2);
		model.addAttribute("user3",user3);
		return "usuarios/formSetting";
	}

	@PostMapping("/editarUsuario")
	public String editarUsuario(@ModelAttribute("user1") Usuario usu, Model model, RedirectAttributes attributes){
		System.err.println(usu);
		Usuario usu_actual = serviceUsuarios.buscarPorId(usu.getId());

		if(usu_actual.getUsername().equals(usu.getUsername()) && usu_actual.getNombre().equals(usu.getNombre())) {
			System.err.println("actual=nuevo");
			attributes.addFlashAttribute("msg_datos", "Debe cambiar al menos uno de los campos para poder editar");
			return "redirect:/usuarios/formSetting";
		}
		if(usu.getEmail() != null) {
			System.err.println(usu);
			serviceUsuarios.editar(usu);
			SecurityContextHolder.clearContext();
		}
		model.addAttribute("msg","Se edito sus credenciales, ingrese nuevamente !!!");
		return "formLogin";
	}


	@PostMapping("/editarCorreo")
	public String editarCorreo(@ModelAttribute("user2") Usuario usu, Model model, RedirectAttributes attributes){
		System.err.println(usu);
		Usuario usu_actual = serviceUsuarios.buscarPorId(usu.getId());

		if(usu_actual.getEmail().equals(usu.getEmail())) {
			System.err.println("actual=nuevo");
			attributes.addFlashAttribute("msg_correo", "Debe cambiar el correo para poder editar");
			return "redirect:/usuarios/formSetting";
		}

		if(usu.getEmail() != null) {
			String token = UUID.randomUUID().toString();
			usu.setToken(token);
			//String confirmacionLink = "https://proyempleos4.onrender.com/confirmar-registro?token=" + token;
			String confirmacionLink = "http://localhost:8080/confirmar-registro?token=" + token;
			usu.setConfirmado(false);
			System.err.println("cammpo confirmado = " + usu);

			//Verificacion del correo con msj
			String subject = "Cambio de correo";
			String message = "Haz clic en el siguiente enlace para confirmar tu registro: " + confirmacionLink;
			SimpleMailMessage email = new SimpleMailMessage();
			email.setTo(usu.getEmail());
			email.setSubject(subject);
			email.setText(message);
			javaMailSender.send(email);

			serviceUsuarios.editarCorreo(usu);
			SecurityContextHolder.clearContext();
		}
		model.addAttribute("msg","Se cambio el correo, ingrese nuevamente !!!");
		return "formLogin";
	}

	@PostMapping("/editarPassword")
	public String editarPassword(@ModelAttribute("user3") Usuario usu, @RequestParam("actual_pass") String actual,
								 @RequestParam("nuevo_pass") String nuevo, Model model, RedirectAttributes attributes){
		System.err.println(usu);
		System.err.println(actual + " - " + nuevo);
		Usuario usu_actual = serviceUsuarios.buscarPorId(usu.getId());

		if (nuevo.equals(actual)) {
			System.err.println("Actual == Nuevo");
			attributes.addFlashAttribute("msg_pass", "La contraseña nueva no puede ser igual a la actual.");
			return "redirect:/usuarios/formSetting";
		}

		if(passwordEncoder.matches(actual, usu_actual.getPassword())) {//en caso de q SI coincidan las pass
			System.err.println("correcto coincide las contraseñas");

			if(nuevo != actual) {
				usu_actual.setPassword(passwordEncoder.encode(nuevo));
				serviceUsuarios.guardar(usu_actual);
				model.addAttribute("msg","Se edito sus credenciales, ingrese nuevamente !!!");
				SecurityContextHolder.clearContext();
				return "formLogin";
			}else {
				System.err.println("Actual == Nuevo");
				attributes.addFlashAttribute("msg_pass","Debe ingresar una contraseña diferente a la Actual !!!");
				return "redirect:/usuarios/formSetting";
			}

		}else {
			System.err.println("No coincide las contraseñas");
			attributes.addFlashAttribute("msg_pass","Contraseña actual no coincide !!!");
			return "redirect:/usuarios/formSetting";
		}

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
