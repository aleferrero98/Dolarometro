package com.telegram.dolarometro.model;

import java.util.Arrays;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EUsdType {

    CCL_DOLLAR("Dolar CCL", "https://mercados.ambito.com/dolarrava/cl/variacion", false, "🌎", "ccl"),
    BANCO_NACION_DOLLAR("Dolar Banco Nacion", "https://mercados.ambito.com/dolarnacion/variacion", true, "🏦", "banco_nacion"),
    OFFICIAL_DOLLAR("Dolar Oficial", "https://mercados.ambito.com/dolar/oficial/variacion", true, "🏛️", "official"),
    BLUE_DOLLAR("Dolar Blue", "https://mercados.ambito.com/dolar/informal/variacion", true, "💙", "blue"),
    CRYPTO_DOLLAR("Dolar Cripto", "https://mercados.ambito.com/dolarcripto/variacion", false, "₿", "crypto"),
    MEP_DOLLAR("Dolar MEP", "https://mercados.ambito.com/dolarrava/mep/variacion", false, "📈", "mep");

    private final String name;
    
    private final String url;

    private final Boolean hasSpread;

    private final String emoji;

    private final String shortName;

    public static EUsdType getUsdTypeFromShortName(String shortName) {
        return Arrays.asList(EUsdType.values()).stream().filter(usdType -> usdType.getShortName().equals(shortName)).findFirst().orElse(null);
    }

}
