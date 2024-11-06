package net.brubio.model;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import jakarta.persistence.*;


@Entity
@Table(name = "Usuarios")
public class Usuario {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String username;
	private String nombre;
	private String email;
	private String password;
	private Integer estatus;
	private Date fechaRegistro;
	
	/*UN USUARIO TENDRA VARIOS PERFILES*/
	@ManyToMany(fetch = FetchType.EAGER)  //fetch = FetchType.EAGER -> al buscar un usuario, se recupere en la misma consulta todos los perfiles de ese usuario
	@JoinTable(name = "UsuarioPerfil", 
			joinColumns = @JoinColumn(name = "idUsuario"),
			inverseJoinColumns = @JoinColumn(name = "idPerfil")) 
	private List<Perfil> perfiles; //Un usuario va a tener MUCHOS perfiles

	private String token;
	private boolean confirmado = true;
	
	//Metodo Helper que ayude agregar perfiles a la lista
	public void agregar(Perfil tempPerfil) {
		if(perfiles == null) {
			perfiles = new LinkedList<>();
		}
		perfiles.add(tempPerfil);
	}
	
	
	
	//GETTERS AND SETTERS
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getEstatus() {
		return estatus;
	}

	public void setEstatus(Integer estatus) {
		this.estatus = estatus;
	}

	public Date getFechaRegistro() {
		return fechaRegistro;
	}

	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	public List<Perfil> getPerfiles() {
		return perfiles;
	}

	public void setPerfiles(List<Perfil> perfiles) {
		this.perfiles = perfiles;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public boolean isConfirmado() {
		return confirmado;
	}

	public void setConfirmado(boolean confirmado) {
		this.confirmado = confirmado;
	}

	@Override
	public String toString() {
		return "Usuario{" +
				"id=" + id +
				", username='" + username + '\'' +
				", nombre='" + nombre + '\'' +
				", email='" + email + '\'' +
				", password='" + password + '\'' +
				", estatus=" + estatus +
				", fechaRegistro=" + fechaRegistro +
				", perfiles=" + perfiles +
				", token='" + token + '\'' +
				", confirmado=" + confirmado +
				'}';
	}
}
