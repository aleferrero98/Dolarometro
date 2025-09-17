package com.telegram.dolarometro.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AmbitoFinancieroUsdResponseDto {

    private String compra;

    private String venta;

    private String fecha;

    private String valor;

    private String valorCierreAnt;

}
