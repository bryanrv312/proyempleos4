package net.brubio.service;

import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import net.brubio.model.Vacante;


@Service
public class VacantesServiceImpl implements IVacantesService{
	
	//attrib privado a nivel de la clase
	private List<Vacante> lista = null;
	
	//constructor
	public VacantesServiceImpl() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy");
		lista = new LinkedList<>();
		
		try {
			Vacante vacante1 = new Vacante();
			vacante1.setId(1);
			vacante1.setNombre("Ingeniero de Civil");
			vacante1.setDescripcion("Se solicita ingeniero para dar soporte a Intranet y reeestablecer todo lima");
			vacante1.setFecha(sdf.parse("08-10-2022"));
			vacante1.setSalario(970.00);
			vacante1.setDestacado(1);
			vacante1.setImagen("empresa1.png");

			Vacante vacante2 = new Vacante();
			vacante2.setId(2);
			vacante2.setNombre("Contador Publico");
			vacante2.setDescripcion("Se solicita ingeniero contador publico para hueviar");
			vacante2.setFecha(sdf.parse("08-05-2022"));
			vacante2.setSalario(920.00);
			vacante2.setDestacado(0);
			vacante2.setImagen("empresa2.png");
	
			Vacante vacante3 = new Vacante();
			vacante3.setId(3);
			vacante3.setNombre("Ingeniero Electrico");
			vacante3.setDescripcion("Se solicita ingeniero electrico con 50 años de experiencia");
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
		
	}

	@Override
	public List<Vacante> buscarTodas() {
		return lista;
	}

	@Override
	public Vacante buscarVacantePorId(Integer idVacante) {
		
		for(Vacante vac : lista) {
			if(vac.getId() == idVacante) {
				return vac;
			}
		}
		
		return null;
	}

	@Override
	public void guardar(Vacante vacante) {
		lista.add(vacante);	
	}

	@Override
	public List<Vacante> buscarDestacadas() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void eliminar(Integer idVacante) {
		// TODO Auto-generated method stub
	}

	@Override
	public List<Vacante> buscarByExample(Example<Vacante> example) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<Vacante> buscarTodas(org.springframework.data.domain.Pageable page) {
		// TODO Auto-generated method stub
		return null;
	}	

}
