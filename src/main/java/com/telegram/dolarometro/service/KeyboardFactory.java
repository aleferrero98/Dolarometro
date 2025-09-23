package com.telegram.dolarometro.service;

import java.util.List;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

public class KeyboardFactory {

public static InlineKeyboardMarkup getUsdTypesKeyboard() {
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows;
        
        // Row 1
        List<InlineKeyboardButton> row1 = List.of(
            InlineKeyboardButton.builder()
                .text("🌎 CCL")
                .callbackData("ccl")
                .build(),
            InlineKeyboardButton.builder()
                .text("📈 MEP")
                .callbackData("mep")
                .build()
        );
        
        // Row 2
        List<InlineKeyboardButton> row2 = List.of(
            InlineKeyboardButton.builder()
                .text("🏛️ Oficial")
                .callbackData("oficial")
                .build(),
            InlineKeyboardButton.builder()
                .text("🏦 Banco Nación")
                .callbackData("banco_nacion")
                .build()
        );
        
        // Row 3
        List<InlineKeyboardButton> row3 = List.of(
            InlineKeyboardButton.builder()
                .text("💙 Blue")
                .callbackData("blue")
                .build(),
            InlineKeyboardButton.builder()
                .text("₿ Cripto")
                .callbackData("crypto")
                .build()
        );

        // Row 4
        List<InlineKeyboardButton> row4 = List.of(
            InlineKeyboardButton.builder()
                .text("💲 Todos")
                .callbackData("all")
                .build()
        );

        rows = List.of(row1, row2, row3, row4);
        keyboard.setKeyboard(rows);

        return keyboard;
    }

}
