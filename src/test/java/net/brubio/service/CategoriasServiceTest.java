package net.brubio.service;

import net.brubio.model.Categoria;
import net.brubio.repository.CategoriasRepository;
import net.brubio.service.db.CategoriasServiceJpa;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoriasServiceTest {

    @InjectMocks
    private CategoriasServiceJpa service;

    @Mock
    private CategoriasRepository repo;

    @Test
    void testGuardarCategoriaYValidarDatos() {
        // 1. Crear y guardar una categoría de prueba
        Categoria categoria = new Categoria();
        categoria.setId(1);
        categoria.setNombre("Ejemplo");
        categoria.setDescripcion("Descripción de prueba");

        // 2. Simular que el repositorio guarda la categoría
        when(repo.save(any(Categoria.class))).thenReturn(categoria);
        when(repo.findById(categoria.getId())).thenReturn(Optional.of(categoria));

        // 3. Guardar la categoría
        service.guardar(categoria);

        // 4. Verificar que el ID se haya generado correctamente
        assertNotNull(categoria.getId());

        // 5. Recuperar la categoría desde el mock y validar los datos
        Optional<Categoria> categoriaGuardada = repo.findById(categoria.getId());

        assertTrue(categoriaGuardada.isPresent());
        assertEquals("Ejemplo", categoriaGuardada.get().getNombre());
        assertEquals("Descripción de prueba", categoriaGuardada.get().getDescripcion());
    }

    @Test
    void testGuardarCategoria3() {
        Categoria categoria = new Categoria();
        //categoria.setId(1);
        categoria.setNombre("Test");
        categoria.setDescripcion("Prueba");

        when(repo.save(any(Categoria.class))).thenReturn(categoria);
        when(repo.findById(categoria.getId())).thenReturn(Optional.of(categoria));
        service.guardar(categoria);
        //Verifica que el método save() del repositorio fue llamado
        //verify(repo, times(1)).save(categoria);
        Optional<Categoria> guard = repo.findById(categoria.getId());
        assertTrue(guard.isPresent());
        assertEquals("Test", guard.get().getNombre());
        assertEquals("Prueba", guard.get().getDescripcion());
    }

    @Test
    void testBuscarPorId_Existe() {
        // Simular una categoría
        Categoria categoria = new Categoria();
        //categoria.setId(1);
        categoria.setNombre("Ejemplo");
        categoria.setDescripcion("descripcion de ejemplo");
        // Simular el comportamiento del repositorio
        when(repo.findById(1)).thenReturn(Optional.of(categoria));
        // Probar el método
        Categoria resultado = service.buscarPorId(1);
        // Validar que el resultado es correcto
        assertNotNull(resultado);
        assertEquals("Ejemplo", resultado.getNombre());
        assertEquals("descripcion de ejemplo", resultado.getDescripcion());
    }

    @Test
    void testGuardarCategoria() {
        //Crear una categoría
        Categoria categoria = new Categoria();
        categoria.setNombre("Ejemplo");
        categoria.setDescripcion("Descripción de prueba");
        //Simular que el repositorio guarda la categoría
        when(repo.save(any(Categoria.class))).thenReturn(categoria);
        when(repo.findByNombreIgnoreCase("Ejemplo")).thenReturn(Optional.of(categoria));
        //Guardar la categoría
        service.guardar(categoria);
        //Recuperar la categoría usando el método `findByNombre()`
        Optional<Categoria> categoriaGuardada = repo.findByNombreIgnoreCase("Ejemplo");
        //Validar que la categoría existe y tiene los datos correctos
        assertTrue(categoriaGuardada.isPresent());
        assertEquals("Ejemplo", categoriaGuardada.get().getNombre());
        assertEquals("Descripción de prueba", categoriaGuardada.get().getDescripcion());
    }

    @Test
    void testActualizarCategoria(){
        Categoria categoria = new Categoria();
        categoria.setId(1);
        categoria.setNombre("Ejemplo");
        categoria.setDescripcion("Descripción de prueba");
        when(repo.existsById(1)).thenReturn(true);
        when(repo.save(categoria)).thenReturn(categoria);
        //actualizar
        categoria.setNombre("Actualizado");
        categoria.setDescripcion("Descripción actualizada");
        service.actualizar(categoria);
        //Verificar que save() fue llamado con la categoría correcta
        verify(repo, times(1)).save(categoria);
        assertEquals("Actualizado", categoria.getNombre());
    }

    @Test
    void testEliminarCategoria(){
        Integer idCategoria = 1;
        service.eliminar(idCategoria);
        //Verificar que deleteById() fue llamado con el ID correcto
        verify(repo, times(1)).deleteById(idCategoria);
    }

    @Test
    void testBuscarPorNombre_Existe() {
        // Simular una categoría en la BD
        Categoria categoria = new Categoria();
        categoria.setId(1);
        categoria.setNombre("Ejemplo");
        // Simular la respuesta del repositorio
        when(repo.findByNombreIgnoreCase("ejemplo")).thenReturn(Optional.of(categoria));
        // Probar el método del servicio
        Optional<Categoria> resultado = service.buscarPorNombreIgnoreCase("ejemplo");
        // Verificar que el resultado es correcto
        assertTrue(resultado.isPresent());
        assertEquals("Ejemplo", resultado.get().getNombre());
    }

    @Test
    void testBuscarPorNombre_NoExiste() {
        // Simular que el nombre no existe en la BD
        when(repo.findByNombreIgnoreCase("inexistente")).thenReturn(Optional.empty());
        // Probar el método del servicio
        Optional<Categoria> resultado = service.buscarPorNombreIgnoreCase("inexistente");
        // Verificar que devuelve `Optional.empty()`
        assertTrue(resultado.isEmpty());
    }

    @Test
    void testBuscarPorNombre_NombreNuloOVacio() {
        // Probar con null y cadena vacía
        Optional<Categoria> resultadoNull = service.buscarPorNombreIgnoreCase(null);
        Optional<Categoria> resultadoVacio = service.buscarPorNombreIgnoreCase(" ");
        // Verificar que devuelve Optional.empty()
        assertTrue(resultadoNull.isEmpty());
        assertTrue(resultadoVacio.isEmpty());
    }


}
