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

@WebServlet("/request/action")
public class RoommateRequestActionServlet extends HttpServlet {
    private RoommateRequestService roommateRequestService;

    @Override
    public void init() {
        roommateRequestService = (RoommateRequestService) getServletContext().getAttribute("roommateRequestService");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        UserEntity user = (UserEntity) session.getAttribute("user");
        try {
            Long requestId = Long.parseLong(request.getParameter("requestId"));
            String action = request.getParameter("action");
            if("accept".equals(action)) {
                roommateRequestService.accept(requestId, user.getId());
                session.setAttribute("flashSuccess", "Заявка принята");
            } else if("reject".equals(action)) {
                roommateRequestService.reject(requestId, user.getId());
                session.setAttribute("flashSuccess", "Заявка отклонена");
            } else {
                session.setAttribute("flashError", "Неизвестное действие");
            }
        } catch(ValidationException e){
            session.setAttribute("flashError", e.getError().get("request"));
        } catch (NumberFormatException e) {
            session.setAttribute("flashError", "Некорректная заявка");
        }
        response.sendRedirect(request.getContextPath() + "/requests");
    }
 }
