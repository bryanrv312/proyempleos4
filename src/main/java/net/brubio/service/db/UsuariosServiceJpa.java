package net.brubio.service.db;

import java.util.List;
import java.util.Optional;

import net.brubio.model.Solicitud;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import net.brubio.model.Usuario;
import net.brubio.repository.UsuariosRepository;
import net.brubio.service.IUsuariosService;


@Service
public class UsuariosServiceJpa implements IUsuariosService {
	
	@Autowired
	UsuariosRepository usuarioRepo;

	@Override
	public void guardar(Usuario usuario) {
		usuarioRepo.save(usuario);
	}

	@Override
	public void eliminar(Integer idUsuario) {
		usuarioRepo.deleteById(idUsuario);
	}

	@Override
	public List<Usuario> buscarTodos() {
		 return usuarioRepo.findAll();
		 
	}

	@Override
	public Usuario buscarPorUsername(String username) {
		return usuarioRepo.findByUsername(username);
	}

	@Override
	public Usuario buscarPorId(Integer idUsuario) {
		Optional<Usuario> optional = usuarioRepo.findById(idUsuario);
		if(optional.isPresent()) {
			return optional.get();
		}
		return null;
	}

	@Override
	public Page<Usuario> buscarTodas(Pageable page) {
		return usuarioRepo.findAll(page);
	}

	@Override
	public Usuario buscarUsuarioPorToken(String token) {
		return usuarioRepo.findByToken(token);
	}

	@Override
	public Usuario editar(Usuario usuario) {
		Optional<Usuario> user = usuarioRepo.findById(usuario.getId());
		if(user.isPresent()){
			Usuario user_edit = user.get();
			user_edit.setNombre(usuario.getNombre());
			user_edit.setUsername(usuario.getUsername());
			return usuarioRepo.save(user_edit);
		}else{
			return null;
		}
	}

	@Override
	public Usuario editarCorreo(Usuario usuario) {
		Optional<Usuario> user = usuarioRepo.findById(usuario.getId());
		if(user.isPresent()){
			Usuario user_edit = user.get();
			user_edit.setEmail(usuario.getEmail());
			user_edit.setConfirmado(usuario.isConfirmado());
			user_edit.setToken(usuario.getToken());
			return usuarioRepo.save(user_edit);
		}else{
			return null;
		}
	}

	@Override
	public Usuario editarPassword(Usuario usuario) {
		Optional<Usuario> user = usuarioRepo.findById(usuario.getId());
		if(user.isPresent()){
			Usuario user_edit = user.get();
			user_edit.setPassword(usuario.getPassword());
			return usuarioRepo.save(user_edit);
		}else{
			return null;
		}

	}

}
