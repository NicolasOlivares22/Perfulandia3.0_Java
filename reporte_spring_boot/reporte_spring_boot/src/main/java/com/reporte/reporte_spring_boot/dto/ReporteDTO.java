package com.reporte.reporte_spring_boot.dto;

import java.sql.Date;

import lombok.Data;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@Data
@Relation(collectionRelation = "reportes", itemRelation = "reporte")
public class ReporteDTO extends RepresentationModel<ReporteDTO> {
    private Integer idReporte;
    private String tipoReporte;
    private Date fechaGeneracion;
    private String descripcion;
    private String jsonDatos;

}
