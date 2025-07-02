package com.clientes.controllers;

import com.clientes.dto.ClienteDTO;
import com.clientes.services.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
        Optional<ClienteDTO> dtoOpt = service.obtenerPorId(id);
        return dtoOpt.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteDTO> actualizar(@PathVariable Integer id,
                                                 @RequestBody ClienteDTO dto) {
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

    //MÉTODOS CON HATEOAS

    @GetMapping("/hateoas/{id}")
    public ResponseEntity<ClienteDTO> obtenerHateoas(@PathVariable Integer id) {
        Optional<ClienteDTO> dtoOpt = service.obtenerPorId(id);
        if (dtoOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        ClienteDTO dto = dtoOpt.get();
        dto.add(linkTo(methodOn(ClienteController.class)
                       .obtenerHateoas(id)).withSelfRel());
        dto.add(linkTo(methodOn(ClienteController.class)
                       .obtenerTodosHateoas()).withRel("todos"));
        dto.add(linkTo(methodOn(ClienteController.class)
                       .eliminar(id)).withRel("eliminar"));


        dto.add(Link.of("http://localhost:8080/api/proxy/clientes/" + id).withSelfRel());
        dto.add(Link.of("http://localhost:8080/api/proxy/clientes/" + id)
                    .withRel("Modificar HATEOAS").withType("PUT"));
        dto.add(Link.of("http://localhost:8080/api/proxy/clientes/" + id)
                    .withRel("Eliminar HATEOAS").withType("DELETE"));

        return ResponseEntity.ok(dto);
    }

    @GetMapping("/hateoas")
    public ResponseEntity<List<ClienteDTO>> obtenerTodosHateoas() {
        List<ClienteDTO> lista = service.listar();

        lista.forEach(dto -> {
            dto.add(linkTo(methodOn(ClienteController.class)
                           .obtenerHateoas(dto.getIdCliente())).withSelfRel());

            dto.add(Link.of("http://localhost:8080/api/proxy/clientes")
                        .withRel("Get todos HATEOAS"));
            dto.add(Link.of("http://localhost:8080/api/proxy/clientes/" + dto.getIdCliente())
                        .withRel("Crear HATEOAS").withType("POST"));
        });

        return ResponseEntity.ok(lista);
    }
}
