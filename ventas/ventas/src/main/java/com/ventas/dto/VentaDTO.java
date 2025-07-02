package com.ventas.dto;
import java.time.LocalDate;
import org.springframework.hateoas.RepresentationModel;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VentaDTO extends RepresentationModel<VentaDTO>{
    private Integer idVenta;
    private Integer idCliente;
    private Integer idVendedor;
    private LocalDate fechaVenta;

}
