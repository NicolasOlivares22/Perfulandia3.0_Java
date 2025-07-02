package com.inventario.dto;

import org.springframework.hateoas.RepresentationModel;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventarioDTO extends RepresentationModel<InventarioDTO>{

    private Integer idInventario;
    private Integer stockDisponible;
    private Integer idProducto;
    private Integer idSucursal;
    private Integer idUsuario;

}
