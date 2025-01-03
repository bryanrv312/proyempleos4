package net.brubio.service;

import java.util.List;

import net.brubio.model.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import net.brubio.model.Solicitud;

public interface ISolicitudesService {
	
	void guardar(Solicitud solicitud);
	
	void eliminar(Integer idSolicitud);
	
	List<Solicitud> buscarTodas();
	
	Solicitud buscarPorId(Integer idSolicitud);
	
	Page<Solicitud> buscarTodas(Pageable ppage);

	Page<Solicitud> buscarPorUsuario(Usuario user, Pageable pageable);

	boolean existeSolicitudParaVacante(Integer idUsuario, Integer idSolicitudVacante);

}
