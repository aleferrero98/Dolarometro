package com.telegram.dolarometro.service;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.objects.Ability;
import org.telegram.abilitybots.api.objects.Flag;
import org.telegram.abilitybots.api.objects.Locality;
import org.telegram.abilitybots.api.objects.Privacy;
import org.telegram.abilitybots.api.objects.Reply;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

import com.telegram.dolarometro.model.EBotCommand;
import com.telegram.dolarometro.model.EUsdType;
import com.telegram.dolarometro.model.UsdExchangeRateEntity;

import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component("DolarometroBot")
public class DolarometroBot extends AbilityBot {

    private final ResponseHandler responseHandler;

    private final SubscriberService subscriberService;

    private final UsdExchangeRateService usdExchangeRateService;

    public DolarometroBot(@Value("${telegram.bot.token}") String botToken,
                                @Value("${telegram.bot.username}") String botUsername,
                                SubscriberService subscriberService,
                                UsdExchangeRateService usdExchangeRateService) {
        super(botToken, botUsername);
        this.responseHandler = new ResponseHandler(silent, db);
        this.subscriberService = subscriberService;
        this.usdExchangeRateService = usdExchangeRateService;
        registerBotCommands();
    }

    @Override
    public long creatorId() {
        return 1L;
    }

    private void registerBotCommands() {
        try {
            SetMyCommands setMyCommands = new SetMyCommands();
            List<BotCommand> commands = Arrays.asList(EBotCommand.values())
                                                .stream()
                                                .map(command ->  new BotCommand(command.getName(), command.getDescription()))
                                                .collect(Collectors.toList());
            setMyCommands.setCommands(commands);
            sender.execute(setMyCommands);
            log.info("Bot commands registered successfully");

        } catch (Exception e) {
            log.error("Error registering bot commands: {}", e.getMessage());
        }
    }

    // Command /start
    public Ability start() {
        return Ability.builder()
                .name(EBotCommand.START.getName())
                .info(EBotCommand.START.getDescription())
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
                .name(EBotCommand.PAUSE.getName())
                .info(EBotCommand.PAUSE.getDescription())
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
                .name(EBotCommand.DELETE.getName())
                .info(EBotCommand.DELETE.getDescription())
                .locality(Locality.USER)
                .privacy(Privacy.PUBLIC)
                .action(ctx -> {
                    subscriberService.deleteSubscriber(ctx.chatId());
                    responseHandler.replyToDelete(ctx.chatId());
                })
                .build();
    }

    // Command /help
    public Ability help() {
        return Ability.builder()
                .name(EBotCommand.HELP.getName())
                .info(EBotCommand.HELP.getDescription())
                .locality(Locality.USER)
                .privacy(Privacy.PUBLIC)
                .action(ctx -> {
                    responseHandler.replyToHelp(ctx.chatId());
                })
                .build();
    }

    // Command /rates
    public Ability rates() {
        return Ability.builder()
                .name(EBotCommand.RATES.getName())
                .info(EBotCommand.RATES.getDescription())
                .locality(Locality.USER)
                .privacy(Privacy.PUBLIC)
                .action(ctx -> {
                    responseHandler.replyToGetRates(ctx.chatId());
                })
                .build();
    }

    public Reply handleRatesCallback() {
        return Reply.of((bot, upd) -> {
            String callbackData = upd.getCallbackQuery().getData();
            long chatId = upd.getCallbackQuery().getMessage().getChatId();
            List<UsdExchangeRateEntity> exchangeRates;

            if ("all".equals(callbackData)) {
                exchangeRates = usdExchangeRateService.getLastUsdExchangeRatesForEachUsdType();
                responseHandler.replyToCurrentRate(chatId, exchangeRates);

            } else {
                EUsdType usdType = EUsdType.getUsdTypeFromShortName(callbackData);

                if (usdType != null) {
                    exchangeRates = usdExchangeRateService.getLastUsdExchangeRatesByUsdType(usdType);
                    responseHandler.replyToCurrentRate(chatId, exchangeRates);
                }
            }
        }, isCallbackQuery());
    }

    // This method allows filtering by buttons (not commands or text)
    private Predicate<Update> isCallbackQuery() {
        return upd -> upd.hasCallbackQuery();
    }

    private Predicate<Update> isNotCommand() {
        return upd -> {
            if (!upd.hasMessage() || !upd.getMessage().hasText()) {
                return false;
            }
            String text = upd.getMessage().getText();
            String commandName = text.substring(1); // removes the slash

            return !EBotCommand.isValidCommand(commandName);
        };
    }

    public Reply handleUnknownMessage() {
        return Reply.of((bot, upd) -> {
            long chatId = upd.getMessage().getChatId();
            responseHandler.handleUnknownMessage(chatId);
        }, Flag.TEXT, isNotCommand());
    }

    @Override
    public List<Reply> replies() {
        return List.of(
            handleRatesCallback(),
            handleUnknownMessage()
        );
    }

    // public Reply replyToButtons() {
    //     BiConsumer<BaseAbilityBot, Update> action = (abilityBot, upd) -> responseHandler.replyToButtons(getChatId(upd), upd.getMessage());
    //     return Reply.of(action, Flag.TEXT,upd -> responseHandler.userIsActive(getChatId(upd)));
    // }

 
}
