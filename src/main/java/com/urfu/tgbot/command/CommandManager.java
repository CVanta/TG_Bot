package com.urfu.tgbot.command;

import com.urfu.tgbot.enums.State;
import com.urfu.tgbot.service.StateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Этот класс управляет обработкой пользовательских команд в Telegram-боте.
 */
@Component
public class CommandManager {

    private final StartCommand startCommand;
    private final HelpCommand helpCommand;

    private final StateService stateService;

    private final NameEditor nameEditor;

    private final EditCommand editCommand;

    @Autowired
    public CommandManager(StartCommand startCommand, HelpCommand helpCommand, StateService stateService, NameEditor nameEditor, EditCommand editCommand) {
        this.startCommand = startCommand;
        this.helpCommand = helpCommand;
        this.stateService = stateService;
        this.nameEditor = nameEditor;
        this.editCommand = editCommand;
    }

    /**
     * Обрабатывает вводимые пользователем данные и реагирует соответствующим образом в зависимости от текущего состояния.
     *
     * @param messageText вводимое пользователем сообщение
     * @param chatId идентификатор чата пользователя
     * @return ответ бота на вводимые пользователем данные
     */
    public String handleInputUpdateState(String messageText, long chatId) {
        State state = stateService.getState(chatId);
        String answer = messageText;
        if (state == null){
            startCommand.changeState(chatId);
            answer = startCommand.getBotText();
            return answer;
        }
        switch (state) {
            case WAITING_FOR_INPUT_NAME -> {
                try {
                    answer = nameEditor.editName(chatId, messageText);
                } catch (Exception e) {
                    return "Вы не изменили имя";
                }
            }
            case WAITING_FOR_INPUT_INSTITUTE -> {
                try {
                    answer = nameEditor.editInstitute(chatId, messageText);
                } catch (Exception e) {
                    return "Вы не изменили институт";
                }
            }
            case WAITING_FOR_INPUT_PHONE_NUMBER -> {
                try {
                    answer = nameEditor.editPhoneNumber(chatId, messageText);
                } catch (Exception e) {
                    return "Вы не изменили номер телефона";
                }
            }
            case WAITING_FOR_INPUT_EDIT_CONFIRMATION -> answer = editCommand.handleConfirmInput(messageText, chatId);

            case WAITING_FOR_COMMAND -> answer = handleCommand(messageText, chatId);
        }
        return answer;
    }

    /**
     * Обрабатывает пользовательский ввод для определенной команды.
     *
     * @param command команда, введенная пользователем
     * @param chatID идентификатор чата пользователя
     * @return ответ бота на команду пользователя
     */
    private String handleCommand(String command, long chatID) {
        switch (command) {
            case "/start" -> {
                startCommand.changeState(chatID);
                return startCommand.getBotText();
            }
            case "/help" -> {
                return helpCommand.getBotCommand();
            }
            case "/edit" -> {
                editCommand.updateState(chatID);
                return editCommand.getBotText(chatID);
            }
            default -> {
                return "Не удалось распознать команду";
            }
        }
    }
}

