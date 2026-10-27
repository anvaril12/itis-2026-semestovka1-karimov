package ru.itis.karimov.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ru.itis.karimov.entity.UserEntity;
import ru.itis.karimov.exception.ValidationException;
import ru.itis.karimov.service.RoommateRequestService;

import java.io.IOException;

@WebServlet("/request/send")
public class RoommateRequestServlet extends HttpServlet {

    private RoommateRequestService roommateRequestService;

    @Override
    public void init() {
        roommateRequestService = (RoommateRequestService) getServletContext().getAttribute("roommateRequestService");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession httpSession = request.getSession();
        UserEntity user = (UserEntity) httpSession.getAttribute("user");
        try {
            Long receiverId = Long.parseLong(request.getParameter("receiverId"));
            roommateRequestService.send(user.getId(), receiverId);
            httpSession.setAttribute("flashSuccess", "Заявка отправлена");
        } catch (ValidationException e) {
            httpSession.setAttribute("flashError", e.getError().get("request"));
        } catch (NumberFormatException e) {
            httpSession.setAttribute("flashError", "Некорректный получатель");
        }
        response.sendRedirect(request.getContextPath() + "/matches");
    }
}
