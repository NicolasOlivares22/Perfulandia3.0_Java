package com.reporte.reporte_spring_boot.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reporte.reporte_spring_boot.dto.ReporteDTO;
import com.reporte.reporte_spring_boot.services.ReporteService;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/reporte")
public class ReporteController {
    
    @Autowired
    private ReporteService service;

    @PostMapping
    public ResponseEntity<ReporteDTO> crear(@RequestBody ReporteDTO dto)
    {
        return ResponseEntity.ok(service.guardar(dto));
    }

    @GetMapping
    public ResponseEntity<List<ReporteDTO>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReporteDTO> obtener(@PathVariable Integer id) {
        return service.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReporteDTO> actualizar(@PathVariable Integer id, @RequestBody ReporteDTO dto) {
        return service.actualizar(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        return service.eliminar(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    // Métodos HATEOAS adicionales
    @GetMapping("/hateoas/{id}")
    public ReporteDTO obtenerHATEOAS(@PathVariable Integer id) {
        ReporteDTO dto = service.obtenerPorId(id)
                .orElseThrow(() -> new RuntimeException("Reporte no encontrado"));

        dto.add(linkTo(methodOn(ReporteController.class).obtenerHATEOAS(id)).withSelfRel());
        dto.add(linkTo(methodOn(ReporteController.class).obtenerTodosHATEOAS()).withRel("todos"));
        dto.add(linkTo(methodOn(ReporteController.class).eliminar(id)).withRel("eliminar"));

        // Links a través del API Gateway
        dto.add(Link.of("http://localhost:8082/api/proxy/reporte/" + dto.getIdReporte()).withSelfRel());
        dto.add(Link.of("http://localhost:8082/api/proxy/reporte/" + dto.getIdReporte())
                .withRel("Modificar HATEOAS").withType("PUT"));
        dto.add(Link.of("http://localhost:8082/api/proxy/reporte/" + dto.getIdReporte())
                .withRel("Eliminar HATEOAS").withType("DELETE"));

        return dto;
    }

    @GetMapping("/hateoas")
    public List<ReporteDTO> obtenerTodosHATEOAS() {
        List<ReporteDTO> lista = service.listar();

        for (ReporteDTO dto : lista) {
            dto.add(linkTo(methodOn(ReporteController.class).obtenerHATEOAS(dto.getIdReporte())).withSelfRel());

            dto.add(Link.of("http://localhost:8082/api/proxy/reporte").withRel("Get todos HATEOAS"));
            dto.add(Link.of("http://localhost:8082/api/proxy/reporte/" + dto.getIdReporte())
                    .withRel("Crear HATEOAS").withType("POST"));
        }

        return lista;
    }
}
