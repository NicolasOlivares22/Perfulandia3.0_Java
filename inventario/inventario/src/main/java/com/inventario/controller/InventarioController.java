package com.inventario.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import com.inventario.dto.InventarioDTO;
import com.inventario.models.Inventario;
import com.inventario.service.InventarioService;

@RestController
@RequestMapping("/api/inventario")
public class InventarioController {

    @Autowired
    private InventarioService inventarioService;



    @GetMapping
    public ResponseEntity<List<Inventario>> getAll() {
        List<Inventario> lista = inventarioService.findAll();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Integer id) {
        Optional<Inventario> inventario = inventarioService.findById(id);
        return inventario.map(ResponseEntity::ok)
                         .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/nuevo")
    public ResponseEntity<Inventario> crearInventario(@RequestBody Inventario inventario) {
        Inventario nuevoInventario = inventarioService.savInventario(inventario);
        return ResponseEntity.ok(nuevoInventario);
    }

    @PutMapping("/ajuste/{id}")
    public ResponseEntity<Inventario> actualizarInventario(@PathVariable Integer id, @RequestBody Inventario inventario) {
        Inventario actualizado = inventarioService.ActualizarInventario(id, inventario);
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInventario(@PathVariable Integer id) {
        if (inventarioService.findById(id).isPresent()) {
            inventarioService.deleteInventario(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

//Hateoas

    @GetMapping("/hateoas/{id}")
    public ResponseEntity<Inventario> obtenerHATEOAS(@PathVariable Integer id) {
        Optional<Inventario> invOpt = inventarioService.findById(id);
        if (!invOpt.isPresent()) return ResponseEntity.notFound().build();

        Inventario inv = invOpt.get();

    
        Link selfLink = linkTo(methodOn(InventarioController.class).obtenerHATEOAS(id)).withSelfRel();
        Link todosLink = linkTo(methodOn(InventarioController.class).obtenerTodosHATEOAS()).withRel("todos");
        Link eliminarLink = linkTo(methodOn(InventarioController.class).deleteInventario(id)).withRel("eliminar");


        Link selfGateway = Link.of("http://localhost:8088/api/proxy/inventario/" + inv.getId_Inventario()).withSelfRel();
        Link putGateway = Link.of("http://localhost:8088/api/proxy/inventario/" + inv.getId_Inventario()).withRel("Actualizar HATEOAS").withType("PUT");
        Link deleteGateway = Link.of("http://localhost:8088/api/proxy/inventario/" + inv.getId_Inventario()).withRel("Eliminar HATEOAS").withType("DELETE");

        return ResponseEntity.ok()
                .header("Link", selfLink.toString())
                .header("Link", todosLink.toString())
                .header("Link", eliminarLink.toString())
                .header("Link", selfGateway.toString())
                .header("Link", putGateway.toString())
                .header("Link", deleteGateway.toString())
                .body(inv);
    }


    @GetMapping("/hateoas")
    public ResponseEntity<List<Inventario>> obtenerTodosHATEOAS() {
        List<Inventario> lista = inventarioService.findAll();

        for (Inventario inv : lista) {
            Integer id = inv.getId_Inventario();
            Link selfLink = linkTo(methodOn(InventarioController.class).obtenerHATEOAS(id)).withSelfRel();
            Link crearLink = Link.of("http://localhost:8088/api/proxy/inventario/" + id).withRel("Crear HATEOAS").withType("POST");

            System.out.println("Link: " + selfLink + " | " + crearLink);
        }
        return ResponseEntity.ok(lista);
    }
}
