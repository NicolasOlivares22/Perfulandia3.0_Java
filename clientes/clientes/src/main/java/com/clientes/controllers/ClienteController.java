package com.clientes.controllers;

import com.clientes.dto.ClienteDTO;
import com.clientes.services.ClienteService;
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
@RequestMapping("/api/clientes")
public class ClienteController {

    @Autowired
    private ClienteService service;

    @PostMapping
    public ResponseEntity<ClienteDTO> crear(@RequestBody ClienteDTO dto) {
        return ResponseEntity.ok(service.guardar(dto));
    }

    @GetMapping
    public ResponseEntity<List<ClienteDTO>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteDTO> obtener(@PathVariable Integer id) {
        return service.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteDTO> actualizar(@PathVariable Integer id, @RequestBody ClienteDTO dto) {
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
    public ClienteDTO obtenerHATEOAS(@PathVariable Integer id) {
        ClienteDTO dto = service.obtenerPorId(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        dto.add(linkTo(methodOn(ClienteController.class).obtenerHATEOAS(id)).withSelfRel());
        dto.add(linkTo(methodOn(ClienteController.class).obtenerTodosHATEOAS()).withRel("todos"));
        dto.add(linkTo(methodOn(ClienteController.class).eliminar(id)).withRel("eliminar"));

        // Links a través del API Gateway
        dto.add(Link.of("http://localhost:8080/api/proxy/clientes/" + dto.getIdCliente()).withSelfRel());
        dto.add(Link.of("http://localhost:8080/api/proxy/clientes/" + dto.getIdCliente())
                .withRel("Modificar HATEOAS").withType("PUT"));
        dto.add(Link.of("http://localhost:8080/api/proxy/clientes/" + dto.getIdCliente())
                .withRel("Eliminar HATEOAS").withType("DELETE"));

        return dto;
    }

    @GetMapping("/hateoas")
    public List<ClienteDTO> obtenerTodosHATEOAS() {
        List<ClienteDTO> lista = service.listar();

        for (ClienteDTO dto : lista) {
            dto.add(linkTo(methodOn(ClienteController.class).obtenerHATEOAS(dto.getIdCliente())).withSelfRel());

            dto.add(Link.of("http://localhost:8080/api/proxy/clientes").withRel("Get todos HATEOAS"));
            dto.add(Link.of("http://localhost:8080/api/proxy/clientes/" + dto.getIdCliente())
                    .withRel("Crear HATEOAS").withType("POST"));
        }

        return lista;
    }
}