package ru.itis.karimov.listener;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.postgresql.ds.PGSimpleDataSource;
import ru.itis.karimov.dao.*;
import ru.itis.karimov.dao.impl.*;
import ru.itis.karimov.service.*;
import ru.itis.karimov.service.impl.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@WebListener
public class AppContextListener implements ServletContextListener {

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

        UserService userService = new UserServiceImpl(userDao, dormitoryDao);
        DormitoryService dormitoryService = new DormitoryServiceImpl(dormitoryDao, universityDao);
        QuestionnaireService questionnaireService = new QuestionnaireServiceImpl(questionDao, answerOptionDao, userAnswerDao);
        CompatibilityService compatibilityService = new CompatibilityServiceImpl(userDao, userAnswerDao);
        RoommateRequestService roommateRequestService = new RoommateRequestServiceImpl(userDao,roommateRequestDao);
        RoomService roomService = new RoomServiceImpl(roomDao, userDao);
        RoomMemberService roomMemberService = new RoomMemberServiceImpl(roomDao, roomMemberDao, userDao, userAnswerDao, compatibilityService);
        QuestionAdminService questionAdminService = new QuestionAdminServiceImpl(questionDao);
        UniversityAdminService universityAdminService = new UniversityAdminServiceImpl(universityDao, dormitoryDao);
        DormitoryAdminService dormitoryAdminService = new DormitoryAdminServiceImpl(dormitoryDao, roomDao, userDao, universityDao);
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
        context.setAttribute("dormitoryService", dormitoryService);
        context.setAttribute("questionnaireService", questionnaireService);
        context.setAttribute("compatibilityService", compatibilityService);
        context.setAttribute("roommateRequestService", roommateRequestService);
        context.setAttribute("roomService", roomService);
        context.setAttribute("roomMemberService", roomMemberService);
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
