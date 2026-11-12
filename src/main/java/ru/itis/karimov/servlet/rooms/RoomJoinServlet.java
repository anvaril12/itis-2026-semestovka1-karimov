package ru.itis.karimov.servlet.rooms;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ru.itis.karimov.entity.UserEntity;
import ru.itis.karimov.exception.ValidationException;
import ru.itis.karimov.service.RoomMemberService;

import java.io.IOException;

@WebServlet("/rooms/join")
public class RoomJoinServlet extends HttpServlet {
    private RoomMemberService roomMemberService;

    @Override
    public void init() {
        roomMemberService = (RoomMemberService) getServletContext().getAttribute("roomMemberService");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        UserEntity user = (UserEntity) session.getAttribute("user");
        try {
            Long roomId = Long.parseLong(request.getParameter("roomId"));
            roomMemberService.join(roomId, user.getId());
            session.setAttribute("flashSuccess", "Заявка в комнату отправлена");
            response.sendRedirect(request.getContextPath() + "/rooms/view?id=" + roomId);
        } catch (ValidationException e) {
            session.setAttribute("flashError", e.getError().get("join"));
            response.sendRedirect(request.getContextPath() + "/rooms");
        } catch (NumberFormatException e) {
            session.setAttribute("flashError", "Некорректная комната");
            response.sendRedirect(request.getContextPath() + "/rooms");
        }
    }
}
