package com.envios.controllers;

import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;                      
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.envios.dto.EnvioDTO;
import com.envios.services.EnvioService;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController                                            
@RequestMapping("/api/envios")
public class EnvioController {

    @Autowired
    private EnvioService service;


    @PostMapping
    public ResponseEntity<EnvioDTO> crear(@RequestBody EnvioDTO dto) {
        return ResponseEntity.ok(service.guardar(dto));
    }

    @GetMapping
    public ResponseEntity<List<EnvioDTO>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EnvioDTO> obtener(@PathVariable Integer id) {
        return service.obtenerPorId(id)
                      .map(ResponseEntity::ok)
                      .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<EnvioDTO> actualizar(@PathVariable Integer id,
                                               @RequestBody EnvioDTO dto) {
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

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<EnvioDTO>> buscarPorEstado(@PathVariable String estado) {
        return ResponseEntity.ok(service.buscarPorEstado(estado));
    }

    @GetMapping("/venta/{idVenta}")
    public ResponseEntity<List<EnvioDTO>> buscarPorVenta(@PathVariable Integer idVenta) {
        return ResponseEntity.ok(service.buscarPorVenta(idVenta));
    }

    @GetMapping("/fechas")
    public ResponseEntity<List<EnvioDTO>> buscarPorRangoFechas(@RequestParam Date inicio,
                                                               @RequestParam Date fin) {
        return ResponseEntity.ok(service.buscarPorRangoFechas(inicio, fin));
    }

//Metodos hateoas

    @GetMapping("/hateoas/{id}")
    public EnvioDTO obtenerHATEOAS(@PathVariable Integer id) {
        EnvioDTO dto = service.obtenerPorId(id)
                              .orElseThrow(() -> new RuntimeException("Envío no encontrado"));

        dto.add(linkTo(methodOn(EnvioController.class).obtenerHATEOAS(id)).withSelfRel());
        dto.add(linkTo(methodOn(EnvioController.class).obtenerTodosHATEOAS()).withRel("todos"));
        dto.add(linkTo(methodOn(EnvioController.class).eliminar(id)).withRel("eliminar"));

        String proxyUrl = "http://localhost:8083/api/proxy/envios/" + dto.getIdEnvio();
        dto.add(Link.of(proxyUrl).withSelfRel());
        dto.add(Link.of(proxyUrl).withRel("Modificar HATEOAS").withType("PUT"));
        dto.add(Link.of(proxyUrl).withRel("Eliminar HATEOAS").withType("DELETE"));

        return dto;
    }

    @GetMapping("/hateoas")
    public ResponseEntity<List<EnvioDTO>> obtenerTodosHATEOAS() {
        List<EnvioDTO> lista = service.listar();

        lista.forEach(dto -> {
            dto.add(linkTo(methodOn(EnvioController.class)
                   .obtenerHATEOAS(dto.getIdEnvio()))
                   .withSelfRel());

            dto.add(Link.of("http://localhost:8083/api/proxy/envios")
                        .withRel("Get todos HATEOAS"));
            dto.add(Link.of("http://localhost:8083/api/proxy/envios/" + dto.getIdEnvio())
                        .withRel("Crear HATEOAS")
                        .withType("POST"));
        });

        return ResponseEntity.ok(lista);
    }
}
