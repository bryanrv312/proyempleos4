package net.brubio.controller;

import java.text.SimpleDateFormat;
//import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import net.brubio.model.Perfil;
import net.brubio.model.Usuario;
import net.brubio.model.Vacante;
import net.brubio.service.ICategoriasService;
import net.brubio.service.IUsuariosService;
import net.brubio.service.IVacantesService;



@Controller
public class HomeController {
	
	@Autowired
	private IVacantesService VacantesService;
	
	@Autowired
	private IUsuariosService serviceUsuarios;
	
	@Autowired
	private ICategoriasService serviceCategorias;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private JavaMailSender javaMailSender;
	
	
	@GetMapping("/tabla")
	public String mostrarTabla(Model model){
		//List<Vacante> lista = getVacantes(); //usaremos la de buscarTodas que esta dentro de la interface de servicio
		List<Vacante> lista = VacantesService.buscarTodas();
		model.addAttribute("vacantesLista", lista);
		
		return "tabla";
	}
	
	
/*
	private List<Vacante> getVacantes(){
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy");
		List<Vacante> lista = new LinkedList<>();
		
		try {
			Vacante vacante1 = new Vacante();
			vacante1.setId(1);
			vacante1.setNombre("Ingeniero de Civil");
			vacante1.setDescripcion("Se solicita ingeniero para dar soporte a Intranet");
			vacante1.setFecha(sdf.parse("08-10-2022"));
			vacante1.setSalario(970.00);
			vacante1.setDestacado(1);
			vacante1.setImagen("empresa1.png");

			Vacante vacante2 = new Vacante();
			vacante2.setId(2);
			vacante2.setNombre("Contador Publico");
			vacante2.setDescripcion("Se solicita ingeniero para dar soporte a Intranet");
			vacante2.setFecha(sdf.parse("08-05-2022"));
			vacante2.setSalario(920.00);
			vacante2.setDestacado(0);
			vacante2.setImagen("empresa2.png");
	
			Vacante vacante3 = new Vacante();
			vacante3.setId(3);
			vacante3.setNombre("Ingeniero Electrico");
			vacante3.setDescripcion("Se solicita ingeniero para dar soporte a Intranet");
			vacante3.setFecha(sdf.parse("08-11-2022"));
			vacante3.setSalario(888.00);
			vacante3.setDestacado(0);
			//vacante3.setImagen("empresa3.png");
			
			lista.add(vacante1);
			lista.add(vacante2);
			lista.add(vacante3);
			
		}catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}
		
		return lista;
	} 
	*/
	
	@GetMapping("/detalle")
	public String mostrarDetalle(Model model) {
		Vacante vacante = new Vacante();
		vacante.setNombre("Ingeniero de Comunicaciones");
		vacante.setDescripcion("Se solicita ingeniero para dar soporte a Intranet");
		vacante.setFecha(new Date());
		vacante.setSalario(970.00);
		model.addAttribute("vacante", vacante);
		return "detalle";
	}
	
	@GetMapping("/listado")
	public String mostrarListado(Model model) {
		List<String> lista = new LinkedList<>();
		lista.add("Ingeniero de Sistemas");
		lista.add("Auxiliar de contabilidad");
		lista.add("Vendedor");
		lista.add("Arquitecto");
		model.addAttribute("empleos", lista);
		return "listado";
	}
	
	/*
	@GetMapping("/")
	public String mostrarHome(Model model) {
		
		//model.addAttribute("mensaje", "Bienvenidos a Empleos App");
		//model.addAttribute("fecha", new Date());
		
		
		String nombre = "Auxiliar de Contabilidad";
		Date fechaPub = new Date();
		double salario = 9000.0;
		boolean vigente = true;
		
		model.addAttribute("nombre", nombre);
		model.addAttribute("fecha", fechaPub);
		model.addAttribute("salario", salario);
		model.addAttribute("vigente", vigente);
		
		return "home";//en referencia al home.html
	}
	*/
	
	//Root Directory
	@GetMapping("/")
	public String mostrarHome(Model model) {
		//List<Vacante> lista = VacantesService.buscarTodas();
		//model.addAttribute("vacantesLista", lista); //ahora en setGenericos()
		return "home";
	}
	
	@GetMapping("/index")
	public String mostrarIndex(Authentication auth, HttpSession session) {// auth->verify user and pass
		String username = auth.getName();//recover user name
		System.err.println("Nombre del usuario: " + username);
		
		for(GrantedAuthority rol:auth.getAuthorities()) {
			System.out.println("Rol: " + rol.getAuthority());
		}
		
		if(session.getAttribute("usuario") == null) {
			Usuario usuario = serviceUsuarios.buscarPorUsername(username);
			usuario.setPassword(null);
			System.out.println("Usuario en sesion: " + usuario);
			session.setAttribute("usuario", usuario);
		}
		
		return "redirect:/";
	}

	/*public String generarYAsignarToken(Usuario usuario) {
		// Generar el token
		String token = UUID.randomUUID().toString();
		usuario.setToken(token);
		serviceUsuarios.guardar(usuario);
		return token;
	}*/
	
	@GetMapping("/signup")
	//public String crear(Usuario usuario) {
	public String registrarse(Usuario usuario) {
		return "usuarios/formRegistro";
	}
	
	@PostMapping("/signup")
	public String guardarRegistro(Usuario usuario, RedirectAttributes attributes) {
		String token = UUID.randomUUID().toString();
		usuario.setToken(token);
		//String confirmacionLink = "https://proyempleos4.onrender.com/confirmar-registro?token=" + token;
		String confirmacionLink = "http://localhost:8080/confirmar-registro?token=" + token;


		String pwdplano = usuario.getPassword();
		String pwdEncriptado = passwordEncoder.encode(pwdplano);
		usuario.setPassword(pwdEncriptado);
		
		usuario.setFechaRegistro(new Date());
		usuario.setEstatus(0);
		usuario.setConfirmado(false);
		
		Perfil perfil = new Perfil();
		perfil.setId(3);		
		usuario.agregar(perfil);

		serviceUsuarios.guardar(usuario);

		String subject = "Confirmación de registro";
		String message = "Haz clic en el siguiente enlace para confirmar tu registro: " + confirmacionLink;
		SimpleMailMessage email = new SimpleMailMessage();
		email.setTo(usuario.getEmail());
		email.setSubject(subject);
		email.setText(message);
		javaMailSender.send(email);

		System.err.println("/registrarUsuario - USUARIO POR CONFIRMAR - SE ENVIO EMAIL!!!");
		attributes.addFlashAttribute("msg", "Se envio un mensaje de confirmación a su correo");

		return "redirect:/login";
		/*attributes.addFlashAttribute("msg", "Registro Guardado");
		System.out.println("Usuario: " + usuario);
		return "redirect:/usuarios/index"; */
	}
	//Confirm email
	@GetMapping("/confirmar-registro")
	public String confirmarRegistro(@RequestParam("token") String token, Model model, RedirectAttributes attributes) {
		System.out.println("- token recibido: " + token);

		//Verifica si el token es válido
		Usuario usuario = serviceUsuarios.buscarUsuarioPorToken(token);
		System.err.println("-usuario del token: " + usuario.getNombre());

		if (usuario != null && usuario.getEstatus() == 0) {
			System.err.println("entro pa");
			usuario.setEstatus(1);
			//usuario.setConfirmado(true); // actualizara a 1 la confirmacion en la bd
			serviceUsuarios.guardar(usuario);
			System.err.println("/confirmar_email - confirmacion_exitosa");

			//model.addAttribute("msg", "Correo confirmado, Ingrese sus credenciales");
			attributes.addFlashAttribute("msg", "Correo confirmado, Ingrese sus credenciales");
			return "redirect:/login";
		}

		System.err.println("/confirmar_email - confirmacion_errone");
		attributes.addFlashAttribute("msg", "Error en la confirmación de su correo");
		return "redirect:/";
	}


	
	//Responde a la peticion get del form en la busqueda
	//recibimos los daotos capturados del form busqueda
	@GetMapping("/search")
	public String buscar(@ModelAttribute("search") Vacante vacante, Model model) {
		System.out.println("Buscando por : " + vacante);
		//where descripcion like '%?%' -- para la descripcion
		ExampleMatcher matcher = ExampleMatcher.matching().withMatcher("descripcion", ExampleMatcher.GenericPropertyMatchers.contains());
		
		Example<Vacante> example = Example.of(vacante, matcher);
		List<Vacante> lista = VacantesService.buscarByExample(example);//guardamos los resultados en la lista
		model.addAttribute("vacantesLista", lista);
		return "home";
	}
	
	
	@ModelAttribute
	public void setGenericos(Model model) {
		Vacante vacanteSearch = new Vacante();//para el databinding
		vacanteSearch.reset();
		model.addAttribute("search", vacanteSearch);//se manda como atributo al th:object en el form
		model.addAttribute("vacantesLista", VacantesService.buscarDestacadas());
		model.addAttribute("categoriasLista", serviceCategorias.buscarTodas());
	}
	
	//encriptar los pw anteriores con el {noop}
	@GetMapping("/bcrypt/{texto}")
	@ResponseBody //para q no renderice una vista sino el texto
	public String encriptar(@PathVariable("texto") String texto) {
		return texto + " Encriptado en Bcrypt: " + passwordEncoder.encode(texto);
	}
	
	
	@GetMapping("/login")
	public String mostrarLogin() {
		return "formLogin";
	}
	
	@GetMapping("/logout")
	public String logout(HttpServletRequest request){
		SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
		logoutHandler.logout(request, null, null);
		return "redirect:/login";
	}
	
	//cuando solo busco por categoria y no descripcion, al atributo descripcion se le agrega null para q no sea vacio
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
	}
	
}
