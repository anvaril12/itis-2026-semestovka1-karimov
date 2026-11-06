package ru.itis.karimov.bot;

import org.telegram.telegrambots.longpolling.util.DefaultLongPollingUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.itis.karimov.service.ConfirmationService;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConfirmationBot extends DefaultLongPollingUpdateConsumer {

    private static final Logger LOGGER = Logger.getLogger(ConfirmationBot.class.getName());
    private static final int MAX_ATTEMPTS_PER_MINUTE = 5;

    private final TelegramClient telegramClient;
    private final ConfirmationService confirmationService;
    private final Map<Long, Attempts> attemptsByChat = new HashMap<>();

    public ConfirmationBot(TelegramClient telegramClient, ConfirmationService confirmationService) {
        this.telegramClient = telegramClient;
        this.confirmationService = confirmationService;
    }

    @Override
    public void consume(Update update) {
        if (!update.hasMessage() || !update.getMessage().hasText()
                || !update.getMessage().getChat().isUserChat()) {
            return;
        }

        long chatId = update.getMessage().getChatId();
        String text = update.getMessage().getText().trim();
        if (text.equals("/start") || text.startsWith("/start ")) {
            send(chatId, "Привет! Отправь шестизначный код со страницы подтверждения на сайте.");
            return;
        }

        if (!allowAttempt(chatId)) {
            send(chatId, "Слишком много попыток. Подожди минуту и попробуй снова.");
            return;
        }

        try {
            if (confirmationService.confirm(text)) {
                send(chatId, "Аккаунт подтверждён! Вернись на сайт и нажми «Я подтвердил».");
            } else {
                send(chatId, "Код неверный, просрочен или уже использован. Проверь код на сайте.");
            }
        } catch (RuntimeException e) {
            LOGGER.log(Level.SEVERE, "Ошибка подтверждения аккаунта", e);
            send(chatId, "Не удалось проверить код. Попробуй позже.");
        }
    }

    private boolean allowAttempt(long chatId) {
        LocalDateTime now = LocalDateTime.now();
        attemptsByChat.values().removeIf(attempts -> !attempts.startedAt().plusMinutes(1).isAfter(now));
        Attempts attempts = attemptsByChat.get(chatId);
        if (attempts == null) {
            attemptsByChat.put(chatId, new Attempts(1, now));
            return true;
        }
        if (attempts.count() >= MAX_ATTEMPTS_PER_MINUTE) {
            return false;
        }
        attemptsByChat.put(chatId, new Attempts(attempts.count() + 1, attempts.startedAt()));
        return true;
    }

    private void send(long chatId, String text) {
        try {
            telegramClient.execute(SendMessage.builder().chatId(chatId).text(text).build());
        } catch (TelegramApiException e) {
            LOGGER.warning("Не удалось отправить ответ Telegram-бота");
        }
    }

    private record Attempts(int count, LocalDateTime startedAt) {
    }
}
