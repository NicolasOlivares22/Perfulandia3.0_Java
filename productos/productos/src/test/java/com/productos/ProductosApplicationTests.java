package com.productos;

import com.productos.controllers.ProductoController;
import com.productos.dto.ProductoDTO;
import com.productos.services.ProductoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ProductosApplicationTests {

	@Test
	void contextLoads() {
	}

}

@ExtendWith(MockitoExtension.class)
class ProductoControllerTest {

    @Mock
    private ProductoService productoService;

    @InjectMocks
    private ProductoController productoController;

    @Test
    void testCrearProducto() {
        
        ProductoDTO productoEntrada = new ProductoDTO();
        productoEntrada.setNombre("Chanel N°5");
        productoEntrada.setDescripcion("Perfume clásico con notas de aldehídos, rosa y jazmín");
        productoEntrada.setPrecioUnitario(150.0);
        productoEntrada.setCategoria("Perfumes");
        productoEntrada.setActivo(true);

        ProductoDTO productoEsperado = new ProductoDTO();
        productoEsperado.setId(1);
        productoEsperado.setNombre("Chanel N°5");
        productoEsperado.setDescripcion("Perfume clásico con notas de aldehídos, rosa y jazmín");
        productoEsperado.setPrecioUnitario(150000.0);
        productoEsperado.setCategoria("Perfumes");
        productoEsperado.setActivo(true);

        when(productoService.crear(any(ProductoDTO.class))).thenReturn(productoEsperado);

        
        ResponseEntity<ProductoDTO> respuesta = productoController.crear(productoEntrada);

        
        assertNotNull(respuesta);
        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertNotNull(respuesta.getBody());
        assertEquals(productoEsperado.getId(), respuesta.getBody().getId());
        assertEquals(productoEsperado.getNombre(), respuesta.getBody().getNombre());
        assertEquals(productoEsperado.getPrecioUnitario(), respuesta.getBody().getPrecioUnitario());
    }

    @Test
    void testObtenerProductoPorId() {
   
        Integer idProducto = 1;
        ProductoDTO productoEsperado = new ProductoDTO();
        productoEsperado.setId(idProducto);
        productoEsperado.setNombre("Dior Sauvage");
        productoEsperado.setDescripcion("Perfume masculino con notas de bergamota, pimienta y madera de cedro");
        productoEsperado.setPrecioUnitario(120000.0);
        productoEsperado.setCategoria("Perfumes");
        productoEsperado.setActivo(true);

        when(productoService.obtenerPorId(idProducto)).thenReturn(productoEsperado);

        ResponseEntity<ProductoDTO> respuesta = productoController.obtener(idProducto);

        
        assertNotNull(respuesta);
        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertNotNull(respuesta.getBody());
        assertEquals(idProducto, respuesta.getBody().getId());
        assertEquals("Dior Sauvage", respuesta.getBody().getNombre());
        assertEquals(120000.0, respuesta.getBody().getPrecioUnitario());
        assertEquals("Perfumes", respuesta.getBody().getCategoria());
        assertTrue(respuesta.getBody().getActivo());
    }
}
