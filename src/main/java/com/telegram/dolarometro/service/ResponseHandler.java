package com.telegram.dolarometro.service;

import org.telegram.abilitybots.api.db.DBContext;
import org.telegram.abilitybots.api.sender.SilentSender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class ResponseHandler {

    // private final Map<Long, EUserStatus> chatStatus;

    private final SilentSender sender;

    public ResponseHandler(SilentSender sender, DBContext db) {
        this.sender = sender;
        // this.chatStatus = db.getMap("chatStatus");
    }

    public void replyToStart(long chatId) { // TODO agregar mensaje de bienvenida con comandos posibles y funcionalidades
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Bienvenido a Dolarómetro! 💸");

        sender.execute(message);
        // chatStatus.put(chatId, AWAITING_NAME);
    }

    public void replyToPause(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Pusimos tus alertas en pausa 🚦. ¡Cuando quieras, volvés con /resume!");

        sender.execute(message);
    }

    public void replyToDelete(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Es una lástima que ya te vayas 😔. ¡Esperamos volver a verte pronto!");

        sender.execute(message);
    }

    private void replyToAnUnexpectedMessage(long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("No entendí eso 😕");

        sender.execute(sendMessage);
    }
    
}
