package com.soporte.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.soporte.dto.SoporteTicketDTO;
import com.soporte.services.SoporteTicketService;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/soporte")
public class SoporteTicketController {

    @Autowired
    private SoporteTicketService service;

    @PostMapping
    public ResponseEntity<SoporteTicketDTO> crear(@RequestBody SoporteTicketDTO dto) {
        return ResponseEntity.ok(service.guardar(dto));
    }

    @GetMapping
    public ResponseEntity<List<SoporteTicketDTO>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SoporteTicketDTO> obtener(@PathVariable Integer id) {
        return service.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<SoporteTicketDTO> actualizar(@PathVariable Integer id, @RequestBody SoporteTicketDTO dto) {
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
    public SoporteTicketDTO obtenerHATEOAS(@PathVariable Integer id) {
        SoporteTicketDTO dto = service.obtenerPorId(id)
                .orElseThrow(() -> new RuntimeException("Ticket de soporte no encontrado"));

        dto.add(linkTo(methodOn(SoporteTicketController.class).obtenerHATEOAS(id)).withSelfRel());
        dto.add(linkTo(methodOn(SoporteTicketController.class).obtenerTodosHATEOAS()).withRel("todos"));
        dto.add(linkTo(methodOn(SoporteTicketController.class).eliminar(id)).withRel("eliminar"));

        // Links a través del API Gateway
        dto.add(Link.of("http://localhost:8086/api/proxy/soporte/" + dto.getIdTicket()).withSelfRel());
        dto.add(Link.of("http://localhost:8086/api/proxy/soporte/" + dto.getIdTicket())
                .withRel("Modificar HATEOAS").withType("PUT"));
        dto.add(Link.of("http://localhost:8086/api/proxy/soporte/" + dto.getIdTicket())
                .withRel("Eliminar HATEOAS").withType("DELETE"));

        return dto;
    }

    @GetMapping("/hateoas")
    public List<SoporteTicketDTO> obtenerTodosHATEOAS() {
        List<SoporteTicketDTO> lista = service.listar();

        for (SoporteTicketDTO dto : lista) {
            dto.add(linkTo(methodOn(SoporteTicketController.class).obtenerHATEOAS(dto.getIdTicket())).withSelfRel());

            dto.add(Link.of("http://localhost:8086/api/proxy/soporte").withRel("Get todos HATEOAS"));
            dto.add(Link.of("http://localhost:8086/api/proxy/soporte/" + dto.getIdTicket())
                    .withRel("Crear HATEOAS").withType("POST"));
        }

        return lista;
    }
} 