package com.telegram.dolarometro.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.objects.Ability;
import org.telegram.abilitybots.api.objects.Locality;
import org.telegram.abilitybots.api.objects.Privacy;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component("DolarometroBot")
public class DolarometroBot extends AbilityBot {

    private final ResponseHandler responseHandler;

    private final SubscriberService subscriberService;

    public DolarometroBot(@Value("${telegram.bot.token}") String botToken,
                                @Value("${telegram.bot.username}") String botUsername,
                                SubscriberService subscriberService) {
        super(botToken, botUsername);
        this.responseHandler = new ResponseHandler(silent, db);
        this.subscriberService = subscriberService;
    }

    @Override
    public long creatorId() {
        return 1L;
    }

    // Comando /start con menú principal
    public Ability start() {
        return Ability.builder()
                .name("start")
                .info("Iniciar el bot y ver el menú principal")
                .locality(Locality.USER)
                .privacy(Privacy.PUBLIC)
                .action(ctx -> {
                    subscriberService.createSubscriber(ctx.chatId(), ctx.user());
                    responseHandler.replyToStart(ctx.chatId());
                })
                .build();
    }

    // Command /pause
    public Ability pause() {
        return Ability.builder()
                .name("pause")
                .info("Pausar al usuario sin borrar configuración")
                .locality(Locality.USER)
                .privacy(Privacy.PUBLIC)
                .action(ctx -> {
                    subscriberService.pauseSubscriber(ctx.chatId());
                    responseHandler.replyToPause(ctx.chatId());
                })
                .build();
    }

    // Command /delete
    public Ability delete() {
        return Ability.builder()
                .name("delete")
                .info("Borrar datos del usuario y desvincularlo del sistema")
                .locality(Locality.USER)
                .privacy(Privacy.PUBLIC)
                .action(ctx -> {
                    subscriberService.deleteSubscriber(ctx.chatId());
                    responseHandler.replyToDelete(ctx.chatId());
                })
                .build();
    }

    //TODO AGREAGAR PARA DESHABILITAR/PAUSAR Y BORRAR UN USUARIO


    // public Reply replyToButtons() {
    //     BiConsumer<BaseAbilityBot, Update> action = (abilityBot, upd) -> responseHandler.replyToButtons(getChatId(upd), upd.getMessage());
    //     return Reply.of(action, Flag.TEXT,upd -> responseHandler.userIsActive(getChatId(upd)));
    // }

 
}
