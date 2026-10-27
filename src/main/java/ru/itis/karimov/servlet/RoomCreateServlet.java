package ru.itis.karimov.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ru.itis.karimov.dto.RoomForm;
import ru.itis.karimov.entity.UserEntity;
import ru.itis.karimov.exception.ValidationException;
import ru.itis.karimov.service.RoomService;

import java.io.IOException;

@WebServlet("/rooms/create")
public class RoomCreateServlet extends HttpServlet {
    private RoomService roomService;

    @Override
    public void init() {
        roomService = (RoomService) getServletContext().getAttribute("roomService");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/room-form.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        UserEntity user = (UserEntity) request.getSession().getAttribute("user");
        RoomForm roomForm = new RoomForm(
                request.getParameter("roomNumber"),
                request.getParameter("description"),
                request.getParameter("capacity")
        );
        try {
            Long id = roomService.create(roomForm, user.getId());
            session.setAttribute("flashSuccess", "Комната создана");
            response.sendRedirect(request.getContextPath() + "/rooms/view?id=" + id);
        } catch(ValidationException e) {
            request.setAttribute("errors", e.getError());
            request.setAttribute("roomForm", roomForm);
            request.getRequestDispatcher("/WEB-INF/views/room-form.jsp").forward(request, response);
        }
    }
}
