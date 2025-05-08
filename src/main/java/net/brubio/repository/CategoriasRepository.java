package net.brubio.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


import net.brubio.model.Categoria;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

//public interface CategoriasRepository extends CrudRepository<Categoria, Integer> {
public interface CategoriasRepository extends JpaRepository<Categoria, Integer> {

    Optional<Categoria> findByNombreIgnoreCase(String name);
    List<Categoria> findByNombreIgnoreCaseContaining(String nombre);
    Page<Categoria> findByNombreIgnoreCaseContaining(String nombre, Pageable pageable);

}
