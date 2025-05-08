package net.brubio.controller;

import net.brubio.model.Categoria;
import net.brubio.service.ICategoriasService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CategoriasController.class)
@AutoConfigureMockMvc(addFilters = false)
public class CategoryControllerTest {

    /*probar las peticiones get y post*/
    @Autowired
    private MockMvc mockMvc;

    //internamente se utiliza esta inyeccion dentro del metodo del controlador
    //@Autowired
    @MockBean //solo quiero simular una inyeccion
    private ICategoriasService service;

    @Test
    void readAllTest() throws Exception {
        List<Categoria> categorias = new ArrayList<>();
        Categoria categoria = new Categoria();
        categoria.setId(1);
        categoria.setNombre("Ejemplo");
        categoria.setDescripcion("Descripción");
        categorias.add(categoria);

        Page<Categoria> pageCategorias = new PageImpl<>(categorias, PageRequest.of(0, 5), categorias.size()); // Página válida con datos

        when(service.buscarTodas(any(Pageable.class))).thenReturn(pageCategorias);

        mockMvc.perform(MockMvcRequestBuilders.get("/categorias/indexPaginate"))
                .andExpect(status().isOk()) // Verifica que el endpoint responde correctamente
                .andExpect(model().attributeExists("listaCategorias")) // Verifica que el modelo contiene la lista
                .andExpect(model().attribute("listaCategorias", pageCategorias)); // Asegura que el objeto esperado está presente
    }

    @Test
    void testMostrarIndexPaginado() throws Exception {
        // Simula una página de categorías con valores
        Categoria categoria1 = new Categoria();
        categoria1.setId(1);
        categoria1.setNombre("Categoría 1");
        categoria1.setNombre("descripcion 1");

        Categoria categoria2 = new Categoria();
        categoria2.setId(2);
        categoria2.setNombre("Categoría 2");
        categoria2.setDescripcion("descripcion 2");
        Page<Categoria> paginaCategorias = new PageImpl<>(List.of(categoria1, categoria2));
        when(service.buscarTodas(any(Pageable.class))).thenReturn(paginaCategorias);

        // Realiza la prueba
        mockMvc.perform(get("/categorias/indexPaginate"))
                .andExpect(status().isOk())
                .andExpect(view().name("categorias/listCategorias"))
                .andExpect(model().attributeExists("listaCategorias"))
                .andExpect(model().attribute("listaCategorias", paginaCategorias));
    }

    @Test
    void testCrearCategoria() throws Exception {
        mockMvc.perform(get("/categorias/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("categorias/formCategoria")) // Verificar que usa la vista correcta
                .andExpect(model().attributeExists("categoria")); // Validar que el modelo contiene la categoría
    }

    @Test
    void testGuardarCategoria() throws Exception {
        mockMvc.perform(post("/categorias/save") // Simular envío de formulario
                        .param("id", "1") // Simular valores en el request
                        .param("nombre", "Ejemplo")
                        .param("descripcion", "Descripción de prueba"))
                .andExpect(status().is3xxRedirection()) // Verificar que redirige
                .andExpect(redirectedUrl("/categorias/indexPaginate")) // Verifica la URL de redirección
                .andExpect(flash().attributeExists("msg")); // Verificar que el mensaje flash está presente
    }

    @Test
    void testEliminarCategoria() throws Exception {
        int idCategoria = 1;

        // Simular que el servicio elimina correctamente la categoría
        doNothing().when(service).eliminar(idCategoria);

        mockMvc.perform(get("/categorias/delete/{id}", idCategoria)) // Simular petición GET
                .andExpect(status().is3xxRedirection()) // Verificar que redirige
                .andExpect(redirectedUrl("/categorias/indexPaginate")) // Verificar la URL de redirección
                .andExpect(flash().attribute("msg", "La categoria fue Eliminada !!!")); // Validar el mensaje flash
    }

    @Test
    void testEditarCategoria() throws Exception {
        int idCategoria = 1;

        // Simular la respuesta del servicio
        Categoria categoria = new Categoria();
        categoria.setId(idCategoria);
        categoria.setNombre("Ejemplo");
        categoria.setDescripcion("Descripción de prueba");

        when(service.buscarPorId(idCategoria)).thenReturn(categoria);

        mockMvc.perform(get("/categorias/edit/{id}", idCategoria)) // Simular la petición GET con ID
                .andExpect(status().isOk())
                .andExpect(view().name("categorias/formCategoria")) // Verificar que carga la vista correcta
                .andExpect(model().attributeExists("categoria"))
                .andExpect(model().attribute("categoria", categoria)); // Asegurar que la categoría esperada está presente
    }

    @Test
    void testBuscarPorNombre_CategoriaExiste() throws Exception {
        String nombreCategoria = "Ejemplo";
        Categoria categoria = new Categoria();
        categoria.setId(1);
        categoria.setNombre(nombreCategoria);
        categoria.setDescripcion("Descripción de prueba");

        when(service.buscarPorNombreIgnoreCase(nombreCategoria)).thenReturn(Optional.of(categoria));

        mockMvc.perform(get("/categorias/findbyname/{nombre}", nombreCategoria))
                .andExpect(status().isOk()) // Verifica que responde correctamente
                .andExpect(view().name("categorias/formCategoria")) // Verifica que carga la vista correcta
                .andExpect(model().attributeExists("categoria")) // Confirma que el modelo tiene la categoría
                .andExpect(model().attribute("categoria", categoria)); // Asegura que la categoría esperada está presente
    }


}
