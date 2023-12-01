package com.urfu.tgbot.commands;

import com.urfu.tgbot.enums.States;
import com.urfu.tgbot.services.StateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AddCommand {

    private final StateService stateService;
    @Autowired
    private AddCommand(StateService stateService) {
        this.stateService = stateService;
    }

    /**
     * Обновляет состояние чата на WAITING_FOR_INPUT_DESTINATION.
     *
     * @param chatID Идентификатор чата.
     */
    public void updateState(long chatID) {
        try {
            stateService.updateState(chatID, States.WAITING_FOR_INPUT_DESTINATION);
        }
        catch (Exception e)
        {
            stateService.saveState(chatID, States.WAITING_FOR_INPUT_DESTINATION);
        }

    }

    /**
     * Возвращает текстовое сообщение бота для команды.
     *
     * @return Текстовое сообщение бота.
     */
    public String getBotText() {
        return "Введите место назначения";
    }
}
