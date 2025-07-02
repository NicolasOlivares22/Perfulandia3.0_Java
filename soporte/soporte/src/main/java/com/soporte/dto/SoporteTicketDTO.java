package com.soporte.dto;

import java.sql.Date;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@Data
@Relation(collectionRelation = "tickets", itemRelation = "ticket")
public class SoporteTicketDTO extends RepresentationModel<SoporteTicketDTO> {
    private Integer idTicket;
    private Integer idUsuario;
    private String tipoTicket;
    private String descripcion;
    private String estado;
    private Date fechaCreacion;
    private Date fechaResolucion;
} 