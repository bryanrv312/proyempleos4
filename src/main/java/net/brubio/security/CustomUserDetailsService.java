package net.brubio.security;

import net.brubio.model.Usuario;
import net.brubio.repository.UsuariosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuariosRepository usuariosRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //cuando sea un Optional<Usuario>
        /*Usuario usuario = usuariosRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));*/

        Usuario usuario = usuariosRepository.findByUsername(username);

        if (usuario == null) {
            throw new UsernameNotFoundException("Usuario no encontrado");
        }

        // Obtener los roles del usuario desde la relación ManyToMany
        List<GrantedAuthority> authorities = usuario.getPerfiles()
                .stream()
                .map(perfil -> new SimpleGrantedAuthority(perfil.getPerfil()))
                .collect(Collectors.toList());

        return User.builder()
                .username(usuario.getUsername())
                .password(usuario.getPassword())
                .disabled(usuario.getEstatus() == 0) // manejo de estatus
                .accountLocked(!usuario.isConfirmado())// manejo de confirmado
                .authorities(authorities)
                .build();

    }

}
