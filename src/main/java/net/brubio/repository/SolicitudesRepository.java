package net.brubio.repository;

import net.brubio.model.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import net.brubio.model.Solicitud;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SolicitudesRepository extends JpaRepository<Solicitud, Integer>{

    Page<Solicitud> findByUsuario(Usuario usuario, Pageable pageable);

    Optional<Solicitud> findByUsuarioIdAndVacanteId(int usuarioId, int vacanteId);

//    Page<Solicitud> findByUsuarioNombreContainingIgnoreCaseOrVacanteNombreContainingIgnoreCase(
//            String nombreUsuario,
//            String nombreVacante,
//            Pageable pageable
//    );

    @Query("SELECT s FROM Solicitud s " +
            "JOIN s.usuario u " +
            "JOIN s.vacante v " +
            "WHERE LOWER(u.nombre) LIKE LOWER(CONCAT('%', :nombre, '%')) " +
            "   OR LOWER(v.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))")
    Page<Solicitud> buscarPorUsuarioONombre(@Param("nombre") String nombre, Pageable pageable);
}
