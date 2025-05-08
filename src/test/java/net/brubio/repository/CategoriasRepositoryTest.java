package net.brubio.repository;

import net.brubio.model.Categoria;
import org.h2.tools.Server;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@DataJpaTest
class CategoriasRepositoryTest {

    @Autowired
    CategoriasRepository repo;

    @Autowired
    TestEntityManager entity;


    //tsodo lo que esta se ejecutara antes de las pruebas
    @BeforeEach
    void setUp() {
        Categoria cat = new Categoria();
        //cat.setId(1);
        cat.setNombre("cat1");
        cat.setDescripcion("descp1");
        entity.persist(cat);
        entity.flush();
    }

    @Test
    void testTablaCategoriasExiste() {
        long count = repo.count();
        assertTrue(count > 0);// Si la tabla existe, count() no lanzará error
        assertEquals(1, count);
    }

    @Test
    void saveCategory() {
        Categoria nuevaCategoria = new Categoria();
        nuevaCategoria.setNombre("cat2");
        nuevaCategoria.setDescripcion("descp2");

        Categoria guardada = repo.save(nuevaCategoria);

        //Validar
        assertNotNull(guardada.getId()); // ID generado no debe ser nulo
        assertEquals("cat2", guardada.getNombre()); // El nombre debe coincidir
        assertEquals("descp2", guardada.getDescripcion()); // La descripción también
    }

    @Test
    void findAllCategories() {
        ArrayList<Categoria> lista = (ArrayList<Categoria>) repo.findAll();
        assertEquals(1, lista.size());
    }

    @Test
    public void findById(){
        Optional<Categoria> cat = repo.findById(1);
        assertEquals(1, cat.get().getId());
        assertEquals("cat1", cat.get().getNombre());
        assertEquals("descp1", cat.get().getDescripcion());
    }

    @Test
    public void updateCategory(){
        Optional<Categoria> cat = repo.findById(1);
        if (cat.isPresent()){
//            cat.get().setNombre("nombre1");
            cat.get().setDescripcion("descripcion1");
        }else {
            System.err.println("No se encontro la Categoría");
        }
        repo.save(cat.get());
        assertEquals("cat1", cat.get().getNombre());
        assertEquals("descripcion1", cat.get().getDescripcion());
    }

    @Test
    public void deleteCategory(){
        repo.deleteById(1);
        Optional<Categoria> catEliminada = repo.findById(1);
        assertTrue(catEliminada.isEmpty());
    }

    @Test
    public void findByNombreIgnoreCaseFound(){
        Optional<Categoria> cat = repo.findByNombreIgnoreCase("cat1");
        assertEquals(cat.get().getNombre(),"cat1");
    }

}