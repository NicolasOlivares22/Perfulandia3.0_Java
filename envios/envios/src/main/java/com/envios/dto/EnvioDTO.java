package com.envios.dto;
import java.sql.Date;
import org.springframework.hateoas.RepresentationModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnvioDTO extends RepresentationModel<EnvioDTO>{
    
    private Integer idEnvio;
    private Integer idVenta;
    private String direccionEnvio;
    private String estadoEnvio;
    private Date fechaEnvio;
    private Date fechaEntrega;

}
