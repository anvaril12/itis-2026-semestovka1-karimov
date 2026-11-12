package ru.itis.karimov.servlet.rooms;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ru.itis.karimov.dto.RoomForm;
import ru.itis.karimov.entity.RoomEntity;
import ru.itis.karimov.entity.UserEntity;
import ru.itis.karimov.exception.ForbiddenException;
import ru.itis.karimov.exception.NotFoundException;
import ru.itis.karimov.exception.ValidationException;
import ru.itis.karimov.service.RoomService;

import java.io.IOException;

@WebServlet("/rooms/edit")
public class RoomEditServlet extends HttpServlet {
    private RoomService roomService;

    @Override
    public void init() {
        roomService = (RoomService) getServletContext().getAttribute("roomService");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserEntity user = (UserEntity) request.getSession().getAttribute("user");
        try {
            Long roomId = Long.parseLong(request.getParameter("id"));
            RoomEntity roomEntity = roomService.getOwnerRoom(roomId, user.getId());
            RoomForm roomForm = new RoomForm(roomEntity.getRoomNumber(), roomEntity.getDescription(), String.valueOf(roomEntity.getCapacity()));
            request.setAttribute("roomForm", roomForm);
            request.setAttribute("roomId", roomId);
            request.getRequestDispatcher("/WEB-INF/views/room-form.jsp").forward(request, response);
        } catch (NumberFormatException | NotFoundException e) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        } catch (ForbiddenException e) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        UserEntity userEntity = (UserEntity) session.getAttribute("user");
        RoomForm roomForm = new RoomForm(
                request.getParameter("roomNumber"),
                request.getParameter("description"),
                request.getParameter("capacity")
        );
        String action = request.getParameter("action");
        try {
            Long id = Long.parseLong(request.getParameter("id"));
            try {
                if("update".equals(action)) {
                    roomService.update(id, roomForm, userEntity.getId());
                    session.setAttribute("flashSuccess", "Комната обновлена");
                    response.sendRedirect(request.getContextPath() + "/rooms/view?id=" + id);
                } else if("delete".equals(action)) {
                    roomService.delete(id, userEntity.getId());
                    session.setAttribute("flashSuccess", "Комната удалена");
                    response.sendRedirect(request.getContextPath() + "/profile");
                } else {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                }
            } catch (ValidationException e) {
                request.setAttribute("errors", e.getError());
                request.setAttribute("roomForm", roomForm);
                request.setAttribute("roomId", id);
                request.getRequestDispatcher("/WEB-INF/views/room-form.jsp").forward(request, response);
            }
        } catch (NumberFormatException | NotFoundException e) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        } catch (ForbiddenException e) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
        }
    }
}
