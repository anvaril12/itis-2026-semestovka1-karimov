package ru.itis.karimov.servlet.rooms;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.itis.karimov.dto.RoomView;
import ru.itis.karimov.entity.UserEntity;
import ru.itis.karimov.service.RoomService;

import java.io.IOException;
import java.util.List;

@WebServlet("/rooms")
public class RoomListServlet extends HttpServlet {
    private RoomService roomService;

    @Override
    public void init() {
        roomService = (RoomService) getServletContext().getAttribute("roomService");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserEntity user = (UserEntity) request.getSession().getAttribute("user");
        List<RoomView> list = roomService.getRoomsInMyDormitory(user.getId());
        request.setAttribute("rooms", list);
        request.getRequestDispatcher("/WEB-INF/views/rooms.jsp").forward(request, response);
    }
}
