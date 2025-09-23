package com.telegram.dolarometro.model;

import java.util.Arrays;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EBotCommand {

    START("start", "Iniciar el bot y ver el menú principal"),
    HELP("help", "Mostrar lista de comandos disponibles"),
    RATES("rates", "Consultar cotizaciones del dólar"),
    PAUSE("pause", "Pausar al usuario sin borrar configuración"),
    DELETE("delete", "Borrar datos del usuario y desvincularlo del sistema");

    private final String name;
    
    private final String description;

    public static boolean isValidCommand(String command) {
        return Arrays.asList(values()).stream().anyMatch(c -> c.getName().equals(command));
    }

}
