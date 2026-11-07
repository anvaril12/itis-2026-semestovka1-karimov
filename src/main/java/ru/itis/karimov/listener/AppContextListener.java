package ru.itis.karimov.listener;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import org.postgresql.ds.PGSimpleDataSource;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.meta.api.methods.GetMe;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.itis.karimov.bot.ConfirmationBot;
import ru.itis.karimov.dao.*;
import ru.itis.karimov.dao.impl.*;
import ru.itis.karimov.service.*;
import ru.itis.karimov.service.impl.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@WebListener
public class AppContextListener implements ServletContextListener {

    private TelegramBotsLongPollingApplication botsApplication;
    private ScheduledExecutorService botExecutor;
    private OkHttpClient telegramHttpClient;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        Properties props = loadProperties();

        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setUrl(props.getProperty("db.url"));
        dataSource.setUser(props.getProperty("db.user"));
        dataSource.setPassword(props.getProperty("db.password"));

        UniversityDao universityDao = new UniversityDaoImpl(dataSource);
        DormitoryDao dormitoryDao = new DormitoryDaoImpl(dataSource);
        UserDao userDao = new UserDaoImpl(dataSource);
        QuestionDao questionDao = new QuestionDaoImpl(dataSource);
        AnswerOptionDao answerOptionDao = new AnswerOptionDaoImpl(dataSource);
        UserAnswerDao userAnswerDao = new UserAnswerDaoImpl(dataSource);
        RoommateRequestDao roommateRequestDao = new RoommateRequestDaoImpl(dataSource);
        RoomDao roomDao = new RoomDaoImpl(dataSource);
        RoomMemberDao roomMemberDao = new RoomMemberDaoImpl(dataSource);
        DormReviewDao dormReviewDao = new DormReviewDaoImpl(dataSource);
        ComplaintDao complaintDao = new ComplaintDaoImpl(dataSource);
        ConfirmationCodeDao confirmationCodeDao = new ConfirmationCodeDaoImpl(dataSource);

        UserService userService = new UserServiceImpl(userDao, dormitoryDao);
        ConfirmationService confirmationService = new ConfirmationServiceImpl(userDao, confirmationCodeDao);
        DormitoryService dormitoryService = new DormitoryServiceImpl(dormitoryDao, universityDao);
        QuestionnaireService questionnaireService = new QuestionnaireServiceImpl(questionDao, answerOptionDao, userAnswerDao);
        CompatibilityService compatibilityService = new CompatibilityServiceImpl(userDao, userAnswerDao);
        RoommateRequestService roommateRequestService = new RoommateRequestServiceImpl(userDao,roommateRequestDao);
        RoomService roomService = new RoomServiceImpl(roomDao, userDao, roomMemberDao);
        RoomMemberService roomMemberService = new RoomMemberServiceImpl(roomDao, roomMemberDao, userDao, userAnswerDao, compatibilityService);
        QuestionAdminService questionAdminService = new QuestionAdminServiceImpl(questionDao);
        UniversityAdminService universityAdminService = new UniversityAdminServiceImpl(universityDao, dormitoryDao);
        DormitoryAdminService dormitoryAdminService = new DormitoryAdminServiceImpl(dormitoryDao, roomDao, userDao, universityDao, dormReviewDao);
        DormReviewService dormReviewService = new DormReviewServiceImpl(dormReviewDao, dormitoryDao);
        ComplaintService complaintService = new ComplaintServiceImpl(complaintDao, userDao);
        ComplaintAdminService complaintAdminService = new ComplaintAdminServiceImpl(complaintDao);
        UserAdminService userAdminService = new UserAdminServiceImpl(userDao);

        ServletContext context = sce.getServletContext();

        context.setAttribute("userAdminService", userAdminService);
        context.setAttribute("complaintAdminService", complaintAdminService);
        context.setAttribute("dormReviewService", dormReviewService);
        context.setAttribute("complaintService", complaintService);
        context.setAttribute("dormitoryAdminService", dormitoryAdminService);
        context.setAttribute("universityAdminService", universityAdminService);
        context.setAttribute("questionAdminService", questionAdminService);
        context.setAttribute("userService", userService);
        context.setAttribute("confirmationService", confirmationService);
        context.setAttribute("dormitoryService", dormitoryService);
        context.setAttribute("questionnaireService", questionnaireService);
        context.setAttribute("compatibilityService", compatibilityService);
        context.setAttribute("roommateRequestService", roommateRequestService);
        context.setAttribute("roomService", roomService);
        context.setAttribute("roomMemberService", roomMemberService);
        startBot(context, confirmationService);
    }

    private void startBot(ServletContext context, ConfirmationService confirmationService) {
        String token = System.getenv("TELEGRAM_BOT_TOKEN");
        if (token == null || token.isBlank()) {
            context.log("TELEGRAM_BOT_TOKEN не задан — бот подтверждения не запущен");
            return;
        }

        telegramHttpClient = new OkHttpClient();
        botExecutor = Executors.newSingleThreadScheduledExecutor();
        botsApplication = new TelegramBotsLongPollingApplication(
                ObjectMapper::new, () -> telegramHttpClient, () -> botExecutor);
        TelegramClient client = new OkHttpTelegramClient(telegramHttpClient, token);
        ConfirmationBot bot = new ConfirmationBot(client, confirmationService);
        try {
            String username = client.execute(new GetMe()).getUserName();
            botsApplication.registerBot(token, bot);
            context.setAttribute("telegramBotLink", "https://t.me/" + username);
            context.log("Telegram-бот подтверждения запущен");
        } catch (Exception e) {
            bot.close();
            stopBot(context);
            context.log("Не удалось запустить Telegram-бота. Сайт продолжит работу без бота. Проверьте токен и доступ к Telegram.");
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        stopBot(sce.getServletContext());
    }

    private void stopBot(ServletContext context) {
        try {
            if (botsApplication != null) {
                botsApplication.close();
            }
        } catch (Exception e) {
            context.log("Ошибка остановки Telegram-бота");
        } finally {
            if (botExecutor != null) {
                botExecutor.shutdownNow();
            }
            if (telegramHttpClient != null) {
                telegramHttpClient.dispatcher().cancelAll();
                telegramHttpClient.dispatcher().executorService().shutdownNow();
                telegramHttpClient.connectionPool().evictAll();
            }
        }
    }

    private Properties loadProperties() {
        try (InputStream in = getClass().getClassLoader().getResourceAsStream("db.properties")) {
            if (in == null) {
                throw new IllegalStateException("Не найден db.properties в classpath");
            }
            Properties props = new Properties();
            props.load(in);
            return props;
        } catch (IOException e) {
            throw new IllegalStateException("Не удалось прочитать db.properties", e);
        }
    }
}
