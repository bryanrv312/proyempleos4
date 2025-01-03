package net.brubio.controller;

import net.brubio.service.db.S3Service;
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

	@Autowired
	private S3Service s3Service;
	
	
	@GetMapping("/indexPaginate")
	public String mostrarIndexPaginado(Pageable pageable, Model model) {
		Page<Solicitud> lista = serviceSolicitudes.buscarTodas(pageable);
		model.addAttribute("listaSolicitudes", lista);
		model.addAttribute("baseUrl", "/solicitudes/indexPaginate");
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
		model.addAttribute("baseUrl", "/solicitudes/indexPaginate_usuario");

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
			Authentication autentication, RedirectAttributes attributes, Model model) {
		
		//recuperamos el nombre del usuario actual
		String username = autentication.getName();
		
		if(result.hasErrors()) {
			System.out.println("Existieron Errores");
			return "solicitudes/formSolicitud";
		}

		//relacionamos al Usuario con la Solicitud
		Usuario usuario = serviceUsuarios.buscarPorUsername(username);
		solicitud.setUsuario(usuario);

		if (serviceSolicitudes.existeSolicitudParaVacante(usuario.getId(), solicitud.getVacante().getId())) {
			attributes.addFlashAttribute("error", "Ya has aplicado a esta vacante anteriormente.");
			return "redirect:/";
		}
		
		if(!multipart.isEmpty()) {
			if (multipart.getSize() > 3 * 1024 * 1024) {
				attributes.addFlashAttribute("error", "El tamaño máximo permitido es 3 MB.");
				return "redirect:/solicitudes/create/" + solicitud.getVacante().getId();
			}
			//validar el archivo
			String tipoArchivo = multipart.getContentType();
			System.err.println("TIPO DE ARCHIVO= " + tipoArchivo);
			if (!tipoArchivo.equals("application/pdf") &&
					!tipoArchivo.equals("application/msword") &&
					!tipoArchivo.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document")) {
				attributes.addFlashAttribute("error", "El archivo debe ser PDF o Word (doc, docx)");
				return "redirect:/solicitudes/create/" + solicitud.getVacante().getId();
			}
			//String nombreArchivo = Utileria.guardarArchivo(multipart, rutaCv);
			//String nombreArchivo = Utileria.guardarArchivo2(multipart);
			String nombreArchivo = s3Service.uploadFileAmazonS3(multipart);
			if(nombreArchivo != null) {
				solicitud.setArchivo(nombreArchivo);
			}
		}
		
		//guardamos en bd
		serviceSolicitudes.guardar(solicitud);
		attributes.addFlashAttribute("msg", "Gracias por enviar tu CV !!!");

		Vacante vacante = serviceVacantes.buscarVacantePorId(solicitud.getVacante().getId());
		String nombreVacante = vacante.getNombre();

		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(usuario.getEmail());
		message.setSubject("Gracias por tu postulación");
		message.setText("Aplicaste a " + solicitud.getVacante().getNombre());
		message.setText("Hola " + usuario.getNombre() + ", Aplicaste a " + nombreVacante + "\n\nGracias por enviar tu CV. Pronto revisaremos tu solicitud.\n\nSaludos,\nEl equipo de Recursos Humanos");

		javaMailSender.send(message); // Enviamos el correo
		
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
