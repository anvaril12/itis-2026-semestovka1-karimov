package ru.itis.karimov.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ru.itis.karimov.entity.ComplaintEntity;
import ru.itis.karimov.entity.UserEntity;
import ru.itis.karimov.exception.NotFoundException;
import ru.itis.karimov.exception.ValidationException;
import ru.itis.karimov.service.ComplaintService;

import java.io.IOException;

@WebServlet("/complaints")
public class ComplaintServlet extends HttpServlet {
    private ComplaintService complaintService;

    @Override
    public void init() {
        complaintService = (ComplaintService) getServletContext().getAttribute("complaintService");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            request.setAttribute("receiverId", Long.parseLong(request.getParameter("receiverId")));
            request.getRequestDispatcher("/WEB-INF/views/complaint-form.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserEntity sender = (UserEntity) request.getSession().getAttribute("user");
        String text = request.getParameter("text");
        try {
            ComplaintEntity complaint = ComplaintEntity.builder()
                    .senderId(sender.getId())
                    .receiverId(Long.parseLong(request.getParameter("receiverId")))
                    .text(text)
                    .build();
            complaintService.save(complaint);
            request.getSession().setAttribute("flashSuccess", "Жалоба отправлена администратору");
            response.sendRedirect(request.getContextPath() + "/matches");
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        } catch (NotFoundException e) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        } catch (ValidationException e) {
            request.setAttribute("receiverId", request.getParameter("receiverId"));
            request.setAttribute("text", text == null ? "" : text);
            request.setAttribute("errors", e.getError());
            request.getRequestDispatcher("/WEB-INF/views/complaint-form.jsp").forward(request, response);
        }
    }
}
