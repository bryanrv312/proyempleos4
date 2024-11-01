package net.brubio.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import net.brubio.model.Vacante;
import net.brubio.service.ICategoriasService;
import net.brubio.service.IVacantesService;
import net.brubio.util.Utileria;

@Controller
@RequestMapping("/vacantes")
public class VacantesController {
	
  //@Value("${jobsapp.ruta.imagenes}")
	@Value("${jobsapp.path.imgs}")
	private String ruta;                   //    -->   VERIFICAR ESTOOOOOOOOOOOO (NO OLVIDAIS) (SOLUCIONADO)

	@Autowired
	private IVacantesService serviceVacantes;
	
	@Autowired
	private ICategoriasService serviceCategorias;
	

	@GetMapping("/index")
	public String mostrarIndex(Model model) {
		// Obtener el listado de vacantes
		List<Vacante> lista = serviceVacantes.buscarTodas();
		model.addAttribute("listaVacantes", lista);
		return "vacantes/listVacantes";
	}
	
	
	@GetMapping("/indexPaginate")
	public String mostrarIndexPaginado(Model model, Pageable page) {
		Page<Vacante> lista = serviceVacantes.buscarTodas(page);
		model.addAttribute("listaVacantes", lista);
		/*System.out.println("NUM de vacantes por Pagina: " + lista.getSize() + " - " + page);
		for(Vacante v:lista) {
			System.out.println(v.getNombre());
		}*/
		return "vacantes/listVacantes";
	}
	

	@GetMapping("/create")
	public String crear(Vacante vacante, Model model) {
		//List<Categoria> listaCategorias = serviceCategorias.buscarTodas();
		model.addAttribute("categorias", serviceCategorias.buscarTodas());
		return "vacantes/formVacante";
	}

	/*
	 * @PostMapping("/save") public String guardar(@RequestParam("nombre") String
	 * nombre, @RequestParam("descripcion") String descripcion,
	 * 
	 * @RequestParam("estatus") String estatus, @RequestParam("fecha") String fecha,
	 * 
	 * @RequestParam("destacado") int destacado, @RequestParam("salario") double
	 * salario,
	 * 
	 * @RequestParam("detalles") String detalles) {
	 * 
	 * System.out.println("Nombre Vacante: " + nombre);
	 * System.out.println("Descripcion: " + descripcion);
	 * System.out.println("Estatus: " + estatus); System.out.println("Fecha: " +
	 * fecha); System.out.println("Destacado: " + destacado);
	 * System.out.println("Salario: " + salario); System.out.println("Detalles: " +
	 * detalles);
	 * 
	 * return "vacantes/listVacantes"; }
	 */

	@PostMapping("/save")
	public String guardar(Vacante vacante, BindingResult result, /*Model model,*/ RedirectAttributes attributes, 
						@RequestParam("archivoImagen") MultipartFile multiPart) {

		if (result.hasErrors()) {
			for (ObjectError error : result.getAllErrors()) {
				System.out.println("Ocurrio un error: " + error.getDefaultMessage());
			}
			return "vacantes/formVacante";
		}
		
		if (!multiPart.isEmpty()) {
			//String ruta = "c:/empleos/img-vacantes/";
			//String nombreImagen = Utileria.guardarArchivo(multiPart, ruta);
			String nombreImagen = Utileria.guardarArchivo2(multiPart);
			if (nombreImagen != null) { // La imagen si se subio
				vacante.setImagen(nombreImagen);
			}
		}

		serviceVacantes.guardar(vacante);
		//model.addAttribute("msg", "Registro Guardado");
		attributes.addFlashAttribute("msg", "Registro Guardado");
		System.out.println("Vacante: " + vacante);

		return "redirect:/vacantes/indexPaginate"; //redireccionando a una URL y no a un doc HTML, (nueva peticion GET)
	}
	

	/*@GetMapping("/delete")
	public String eliminar(@RequestParam("id") int idVacante) {
		System.out.println("Borrando vacante con id: " + idVacante);
		return "mensaje";
	}*/
	@GetMapping("/delete/{id}")
	public String eliminar(@PathVariable("id") int idVacante, RedirectAttributes attributes) {
		System.out.println("Borrando vacante con id: " + idVacante);
		serviceVacantes.eliminar(idVacante);
		attributes.addFlashAttribute("msg", "La vacante fue Eliminada !!!");
		return "redirect:/vacantes/indexPaginate";
	}
	
	@GetMapping("/edit/{id}")
	public String editar(@PathVariable("id") int idVacante, Model model) {
		Vacante vacante = serviceVacantes.buscarVacantePorId(idVacante);
		model.addAttribute("vacante", vacante);
		model.addAttribute("categorias", serviceCategorias.buscarTodas());
		return "vacantes/formVacante";
	}
	

	@GetMapping("/view/{id}")
	public String verDetalle(@PathVariable("id") int idVacante, Model model) {
		Vacante vacante = serviceVacantes.buscarVacantePorId(idVacante);
		System.out.println("Vacante: " + vacante.getId());
		model.addAttribute("vacanteDetalle", vacante);
		return "vacantes/detalle";
	}
	

	@InitBinder
	public void initBinder(WebDataBinder webDataBinder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		webDataBinder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
	}

}





