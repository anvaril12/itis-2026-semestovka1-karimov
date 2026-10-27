package ru.itis.karimov.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.itis.karimov.exception.NotFoundException;
import ru.itis.karimov.service.RoomService;

import java.io.IOException;

@WebServlet("/rooms/view")
public class RoomViewServlet extends HttpServlet {
    private RoomService roomService;

    @Override
    public void init() {
        roomService = (RoomService) getServletContext().getAttribute("roomService");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Long roomId = Long.parseLong(request.getParameter("id"));
            request.setAttribute("roomView", roomService.getById(roomId));
            request.getRequestDispatcher("/WEB-INF/views/room-view.jsp").forward(request, response);
        } catch (NumberFormatException | NotFoundException e) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}
