package net.brubio.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.annotation.JsonCreator.Mode;

import net.brubio.model.Solicitud;
import net.brubio.model.Usuario;
import net.brubio.model.Vacante;
import net.brubio.service.ISolicitudesService;
import net.brubio.service.IUsuariosService;
import net.brubio.service.IVacantesService;
import net.brubio.util.Utileria;

import java.security.Principal;

@Controller
@RequestMapping("solicitudes")
public class SolicitudesController {
	
	@Value("${jobsapp.path.cv}")
	private String rutaCv;
	
	@Autowired
	private IVacantesService serviceVacantes;
	
	@Autowired
	private IUsuariosService serviceUsuarios;
	
	@Autowired
	private ISolicitudesService serviceSolicitudes;
	
	@Autowired
	private JavaMailSender javaMailSender;
	
	
	@GetMapping("/indexPaginate")
	public String mostrarIndexPaginado(Pageable pageable, Model model) {
		Page<Solicitud> lista = serviceSolicitudes.buscarTodas(pageable);
		model.addAttribute("listaSolicitudes", lista);
		return "solicitudes/listSolicitudes";
	}


	@GetMapping("/indexPaginate_usuario")
	public String mostrarIndexPaginado(Pageable pageable, Model model, Principal principal) {
		String username = principal.getName();
		Usuario user = serviceUsuarios.buscarPorUsername(username);
		System.err.println(user);

		Page<Solicitud> lista = serviceSolicitudes.buscarPorUsuario(user, pageable);

		if(lista.isEmpty()) {
			System.err.println("lista de solicitudes vacia !");
			model.addAttribute("msg_null", "No hay solicitudes registradas para este usuario");
		}else {
			model.addAttribute("listaSolicitudes", lista);
		}

		/*for (Perfil perfil : user.getPerfiles()) {
            System.err.println("Perfil ID: " + perfil.getId() + ", Perfil: " + perfil.getPerfil());

            if(perfil.getPerfil().equals("USUARIO")) {
            	lista = serviceSolicitudes.buscarPorUsuario(user, pageable);
            }
            if(perfil.getPerfil().equals("ADMINISTRADOR")) {
            	lista = serviceSolicitudes.buscarTodas(pageable);
            }
            if(perfil.getPerfil().equals("SUPERVISOR") && perfil.getPerfil().equals("ADMINISTRADOR")) {
            	lista = serviceSolicitudes.buscarTodas(pageable);
            }
        } */

		//filtrar las solicitdes segun el rol
		//Page<Solicitud> lista= serviceSolicitudes.buscarTodas(pageable);
		//model.addAttribute("listaSolicitudes", lista);
		return "solicitudes/listSolicitudes";
	}
	
	
	@GetMapping("/create/{idVacante}")
	public String crear(Solicitud solicitud, @PathVariable("idVacante") Integer idVacante, Model model) {
		Vacante vacante = serviceVacantes.buscarVacantePorId(idVacante);
		System.out.println("id de la Vacante : " + idVacante);
		model.addAttribute("vacante", vacante);
		return "solicitudes/formSolicitud";
	}
	
	
	@PostMapping("/save")
	public String guardar(Solicitud solicitud, BindingResult result, @RequestParam("archivoCV") MultipartFile multipart, 
			Authentication autentication, RedirectAttributes attributes) {
		
		//recuperamos el nombre del usuario actual
		String username = autentication.getName();
		
		if(result.hasErrors()) {
			System.out.println("Existieron Errores");
			return "solicitudes/formSolicitud";
		}
		
		if(!multipart.isEmpty()) {
			//String nombreArchivo = Utileria.guardarArchivo(multipart, rutaCv);
			String nombreArchivo = Utileria.guardarArchivo2(multipart);
			if(nombreArchivo != null) {
				solicitud.setArchivo(nombreArchivo);
			}
		}
		
		//relacionamos al Usuario con la Solicitud
		Usuario usuario = serviceUsuarios.buscarPorUsername(username);
		solicitud.setUsuario(usuario);
		
		//guardamos en bd
		serviceSolicitudes.guardar(solicitud);
		attributes.addFlashAttribute("msg", "Gracias por enviar tu CV !!!");
		
		System.out.println("Solicitud: " + solicitud);
		return "redirect:/";
	}
	
	
	@GetMapping("/delete/{idSolicitud}")
	public String eliminar(@PathVariable("idSolicitud") Integer idSolicitud, RedirectAttributes attributes) {
		serviceSolicitudes.eliminar(idSolicitud);
		attributes.addFlashAttribute("msg", "Solcitud Eliminada !!!");
		return "redirect:/solicitudes/indexPaginate";
	}
	
	
	@GetMapping("/sendMail/{id}")
	public String enviarEmail(@PathVariable("id") Integer idSolicitud, RedirectAttributes attributes) {
		SimpleMailMessage message = new SimpleMailMessage();
		Solicitud solicitud = serviceSolicitudes.buscarPorId(idSolicitud);
		System.out.println(solicitud.getUsuario().getEmail());
		
		message.setTo(solicitud.getUsuario().getEmail());
		message.setSubject("Recepcion de CV");
		message.setText("Tu hoja de vida esta siendo evaluada");
		
		try {
			javaMailSender.send(message);
			attributes.addFlashAttribute("msg", "Correo enviado exitosamente.");
		} catch (Exception e) {
			attributes.addFlashAttribute("msg", "Error al enviar el correo.");
		}
		
		return "redirect:/solicitudes/indexPaginate";
	} 
	
	
	
		
}
