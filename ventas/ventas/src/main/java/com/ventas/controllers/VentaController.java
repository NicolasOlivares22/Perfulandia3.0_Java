package com.ventas.controllers;

import com.ventas.dto.VentaDTO;
import com.ventas.dto.VentaDTOConDetalles;
import com.ventas.services.VentaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/ventas")
public class VentaController {

    @Autowired
    private VentaService service;

    @PostMapping
    public ResponseEntity<VentaDTO> crear(@RequestBody VentaDTO dto) {
        return ResponseEntity.ok(service.crear(dto));
    }

    @GetMapping
    public ResponseEntity<List<VentaDTO>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<VentaDTOConDetalles> obtener(@PathVariable Integer id) {
        return ResponseEntity.ok(service.obtenerVentaCompleta(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<VentaDTO> actualizar(@PathVariable Integer id, @RequestBody VentaDTO dto) {
        return ResponseEntity.ok(service.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    //HATEOAS

    @GetMapping("/hateoas/{id}")
    public VentaDTO obtenerHATEOAS(@PathVariable Integer id) {   //tuve que agregar un opcional en el obtener por id en el service 
        VentaDTO dto = service.obtenerPorId(id)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada"));

        dto.add(linkTo(methodOn(VentaController.class).obtenerHATEOAS(id)).withSelfRel());
        dto.add(linkTo(methodOn(VentaController.class).obtenerTodosHATEOAS()).withRel("todas"));
        dto.add(linkTo(methodOn(VentaController.class).eliminar(id)).withRel("eliminar"));

        // Links a través del API Gateway
        dto.add(Link.of("http://localhost:8087/api/proxy/ventas/" + dto.getIdVenta()).withSelfRel());
        dto.add(Link.of("http://localhost:8087/api/proxy/ventas/" + dto.getIdVenta())
                .withRel("Modificar HATEOAS").withType("PUT"));
        dto.add(Link.of("http://localhost:8087/api/proxy/ventas/" + dto.getIdVenta())
                .withRel("Eliminar HATEOAS").withType("DELETE"));

        return dto;
    }

    @GetMapping("/hateoas")
    public List<VentaDTO> obtenerTodosHATEOAS() {
        List<VentaDTO> lista = service.listar();

        for (VentaDTO dto : lista) {
            dto.add(linkTo(methodOn(VentaController.class).obtenerHATEOAS(dto.getIdVenta())).withSelfRel());

            dto.add(Link.of("http://localhost:8087/api/proxy/ventas").withRel("Get todas HATEOAS"));
            dto.add(Link.of("http://localhost:8087/api/proxy/ventas/" + dto.getIdVenta())
                    .withRel("Crear HATEOAS").withType("POST"));
        }

        return lista;
    }
}
