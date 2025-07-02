package com.vededores.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "vendedores")
@Data
public class Vendedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Integer idVendedor;

    private Integer idUsuario;

    private String nombreCompleto;

    private Integer rut;

    private String areaVentas;
}