package ru.itis.karimov.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ru.itis.karimov.entity.UserEntity;
import ru.itis.karimov.entity.enums.RoomMemberStatus;
import ru.itis.karimov.exception.ValidationException;
import ru.itis.karimov.service.RoomMemberService;
import java.io.IOException;

@WebServlet("/rooms/members")
public class RoomMemberActionServlet extends HttpServlet {
    private RoomMemberService roomMemberService;

    @Override
    public void init() {
        roomMemberService = (RoomMemberService) getServletContext().getAttribute("roomMemberService");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        UserEntity user = (UserEntity) session.getAttribute("user");
        String roomIdParam = request.getParameter("roomId");
        String action = request.getParameter("action");
        try {
            Long roomId = Long.parseLong(roomIdParam);
            Long memberId = Long.parseLong(request.getParameter("memberId"));
            if("accept".equals(action)) {
                roomMemberService.changeStatus(roomId, memberId, user.getId(), RoomMemberStatus.ACCEPTED);
                session.setAttribute("flashSuccess", "Заявка принята");
            } else if("reject".equals(action)) {
                roomMemberService.changeStatus(roomId, memberId, user.getId(), RoomMemberStatus.REJECTED);
                session.setAttribute("flashSuccess", "Заявка отклонена");
            } else {
                session.setAttribute("flashError", "Неизвестное действие");
            }
        } catch(ValidationException e) {
            session.setAttribute("flashError", e.getError().get("member"));
        } catch(NumberFormatException e) {
            session.setAttribute("flashError", "Некорректная заявка");
        }
        response.sendRedirect(request.getContextPath() + "/rooms/view?id=" + roomIdParam);
    }
}
