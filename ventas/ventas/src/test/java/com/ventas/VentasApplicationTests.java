package com.ventas;

import com.ventas.controllers.VentaController;
import com.ventas.dto.DetalleVentaDTO;
import com.ventas.dto.VentaDTO;
import com.ventas.dto.VentaDTOConDetalles;
import com.ventas.services.VentaService;
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

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
class VentasApplicationTests {

	@Test
	void contextLoads() {
	}

}

@ExtendWith(MockitoExtension.class)
class VentaControllerTest {

    @Mock
    private VentaService ventaService;

    @InjectMocks
    private VentaController ventaController;

    @Test
    void testCrearVenta() {
   
        VentaDTO ventaEntrada = new VentaDTO();
        ventaEntrada.setIdCliente(1);
        ventaEntrada.setIdVendedor(2);
        ventaEntrada.setFechaVenta(LocalDate.of(2024, 1, 15));

        VentaDTO ventaEsperada = new VentaDTO();
        ventaEsperada.setIdVenta(1);
        ventaEsperada.setIdCliente(1);
        ventaEsperada.setIdVendedor(2);
        ventaEsperada.setFechaVenta(LocalDate.of(2024, 1, 15));

        when(ventaService.crear(any(VentaDTO.class))).thenReturn(ventaEsperada);

        // Act
        ResponseEntity<VentaDTO> respuesta = ventaController.crear(ventaEntrada);

        // Assert
        assertNotNull(respuesta);
        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertNotNull(respuesta.getBody());
        assertEquals(ventaEsperada.getIdVenta(), respuesta.getBody().getIdVenta());
        assertEquals(ventaEsperada.getIdCliente(), respuesta.getBody().getIdCliente());
        assertEquals(ventaEsperada.getIdVendedor(), respuesta.getBody().getIdVendedor());
        assertEquals(ventaEsperada.getFechaVenta(), respuesta.getBody().getFechaVenta());
    }

    @Test
    void testObtenerVentaPorId() {
        // Arrange
        Integer idVenta = 1;
        VentaDTOConDetalles ventaEsperada = new VentaDTOConDetalles();
        ventaEsperada.setIdVenta(idVenta);
        ventaEsperada.setIdCliente(3);
        ventaEsperada.setIdVendedor(4);
        ventaEsperada.setFechaVenta(LocalDate.of(2024, 1, 20));
        ventaEsperada.setDetalles(Arrays.asList(
            new DetalleVentaDTO(1, 1, 2, 150000.0, null),
            new DetalleVentaDTO(2, 1, 3, 80000.0, null)
        ));

        when(ventaService.obtenerVentaCompleta(idVenta)).thenReturn(ventaEsperada);

        // Act
        ResponseEntity<VentaDTOConDetalles> respuesta = ventaController.obtener(idVenta);

        // Assert
        assertNotNull(respuesta);
        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertNotNull(respuesta.getBody());
        assertEquals(idVenta, respuesta.getBody().getIdVenta());
        assertEquals(3, respuesta.getBody().getIdCliente());
        assertEquals(4, respuesta.getBody().getIdVendedor());
        assertEquals(LocalDate.of(2024, 1, 20), respuesta.getBody().getFechaVenta());
        assertNotNull(respuesta.getBody().getDetalles());
        assertEquals(2, respuesta.getBody().getDetalles().size());
    }
}
