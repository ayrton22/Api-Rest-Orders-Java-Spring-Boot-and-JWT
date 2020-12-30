package com.codmain.orderapi.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

// ¿que pasaria si la entidad cambiara y le cambio algun nombre a las variables? esto lo que harai seria impactar el contrato, con esto nos blindamos y el contrato del, servicio permanece igual

@Builder
@Getter
@Setter
public class ProductDTO {
    private Long id;
    private String name;
    private Double price;
}
