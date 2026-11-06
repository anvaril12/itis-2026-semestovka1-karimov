package ru.itis.karimov.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ru.itis.karimov.entity.ConfirmationCodeEntity;
import ru.itis.karimov.entity.UserEntity;
import ru.itis.karimov.exception.AuthenticationException;
import ru.itis.karimov.exception.NotFoundException;
import ru.itis.karimov.service.ConfirmationService;
import ru.itis.karimov.service.UserService;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

@WebServlet("/confirm")
public class ConfirmationServlet extends HttpServlet {

    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm");
    private ConfirmationService confirmationService;
    private UserService userService;

    @Override
    public void init() {
        confirmationService = (ConfirmationService) getServletContext().getAttribute("confirmationService");
        userService = (UserService) getServletContext().getAttribute("userService");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        UserEntity user = session == null ? null : (UserEntity) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/signin");
            return;
        }
        if (user.isConfirmed()) {
            response.sendRedirect(request.getContextPath() + "/profile");
            return;
        }

        ConfirmationCodeEntity code;
        try {
            code = confirmationService.getOrCreateCode(user.getId());
        } catch (AuthenticationException | NotFoundException e) {
            session.invalidate();
            response.sendRedirect(request.getContextPath() + "/signin");
            return;
        } catch (IllegalStateException e) {
            response.sendRedirect(request.getContextPath() + "/profile");
            return;
        }

        request.setAttribute("code", code.getCode());
        request.setAttribute("issuedAt", code.getSendAt().format(TIME_FORMAT));
        request.setAttribute("botLink", getServletContext().getAttribute("telegramBotLink"));
        request.setAttribute("error", session.getAttribute("confirmationError"));
        session.removeAttribute("confirmationError");
        request.getRequestDispatcher("/WEB-INF/views/confirm.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        UserEntity user = session == null ? null : (UserEntity) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/signin");
            return;
        }

        try {
            UserEntity freshUser = userService.getActiveUser(user.getId());
            session.setAttribute("user", freshUser);
            if (freshUser.isConfirmed()) {
                session.removeAttribute("confirmationError");
                response.sendRedirect(request.getContextPath() + "/profile");
            } else {
                session.setAttribute("confirmationError", "Аккаунт пока не подтверждён. Отправьте код боту и попробуйте ещё раз.");
                response.sendRedirect(request.getContextPath() + "/confirm");
            }
        } catch (AuthenticationException | NotFoundException e) {
            session.invalidate();
            response.sendRedirect(request.getContextPath() + "/signin");
        }
    }
}
