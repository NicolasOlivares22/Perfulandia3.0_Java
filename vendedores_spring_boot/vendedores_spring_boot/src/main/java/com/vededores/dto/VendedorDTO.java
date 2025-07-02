package com.vededores.dto;

import lombok.Data;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@Data
@Relation(collectionRelation = "vendedores", itemRelation = "vendedor")
public class VendedorDTO extends RepresentationModel<VendedorDTO> {
    private Integer idVendedor;
    private Integer idUsuario;
    private String nombreCompleto;
    private Integer rut;
    private String areaVentas;
    
}
