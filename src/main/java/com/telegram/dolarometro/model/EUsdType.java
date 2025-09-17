package com.telegram.dolarometro.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EUsdType {
    CCL_DOLLAR("Dolar CCL", "https://mercados.ambito.com/dolarrava/cl/variacion", false),
    BANCO_NACION_DOLLAR("Dolar Banco Nacion", "https://mercados.ambito.com/dolarnacion/variacion", true),
    OFFICIAL_DOLLAR("Dolar Oficial", "https://mercados.ambito.com/dolar/oficial/variacion", true),
    BLUE_DOLLAR("Dolar Blue", "https://mercados.ambito.com/dolar/informal/variacion", true),
    CRYPTO_DOLLAR("Dolar Cripto", "https://mercados.ambito.com/dolarcripto/variacion", false),
    MEP_DOLLAR("Dolar MEP", "https://mercados.ambito.com/dolarrava/mep/variacion", false);

    private final String name;
    
    private final String url;

    private final Boolean hasSpread;
}
