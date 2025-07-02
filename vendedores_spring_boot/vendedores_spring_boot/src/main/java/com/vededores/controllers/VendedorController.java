package com.vededores.controllers;

import com.vededores.dto.VendedorDTO;
import com.vededores.services.VendedorService;
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
@RequestMapping("/api/vendedores")
public class VendedorController {

    @Autowired
    private VendedorService service;

    @PostMapping
    public ResponseEntity<VendedorDTO> crear(@RequestBody VendedorDTO dto) {
        return ResponseEntity.ok(service.guardar(dto));
    }

    @GetMapping
    public ResponseEntity<List<VendedorDTO>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<VendedorDTO> obtener(@PathVariable Integer id) {
        return service.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<VendedorDTO> actualizar(@PathVariable Integer id, @RequestBody VendedorDTO dto) {
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
    public VendedorDTO obtenerHATEOAS(@PathVariable Integer id) {
        VendedorDTO dto = service.obtenerPorId(id)
                .orElseThrow(() -> new RuntimeException("Vendedor no encontrado"));

        dto.add(linkTo(methodOn(VendedorController.class).obtenerHATEOAS(id)).withSelfRel());
        dto.add(linkTo(methodOn(VendedorController.class).obtenerTodosHATEOAS()).withRel("todos"));
        dto.add(linkTo(methodOn(VendedorController.class).eliminar(id)).withRel("eliminar"));

        // Links a través del API Gateway
        dto.add(Link.of("http://localhost:8085/api/proxy/vendedores/" + dto.getIdVendedor()).withSelfRel());
        dto.add(Link.of("http://localhost:8085/api/proxy/vendedores/" + dto.getIdVendedor())
                .withRel("Modificar HATEOAS").withType("PUT"));
        dto.add(Link.of("http://localhost:8085/api/proxy/vendedores/" + dto.getIdVendedor())
                .withRel("Eliminar HATEOAS").withType("DELETE"));

        return dto;
    }

    @GetMapping("/hateoas")
    public List<VendedorDTO> obtenerTodosHATEOAS() {
        List<VendedorDTO> lista = service.listar();

        for (VendedorDTO dto : lista) {
            dto.add(linkTo(methodOn(VendedorController.class).obtenerHATEOAS(dto.getIdVendedor())).withSelfRel());

            dto.add(Link.of("http://localhost:8085/api/proxy/vendedores").withRel("Get todos HATEOAS"));
            dto.add(Link.of("http://localhost:8085/api/proxy/vendedores/" + dto.getIdVendedor())
                    .withRel("Crear HATEOAS").withType("POST"));
        }

        return lista;
    }
}
