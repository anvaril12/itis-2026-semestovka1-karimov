package ru.itis.karimov.servlet.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ru.itis.karimov.entity.UserEntity;
import ru.itis.karimov.exception.NotFoundException;
import ru.itis.karimov.exception.ValidationException;
import ru.itis.karimov.service.UserAdminService;

import java.io.IOException;
import java.util.List;

@WebServlet("/admin/users")
public class UserAdminServlet extends HttpServlet {

    private UserAdminService userAdminService;

    @Override
    public void init() {
        userAdminService = (UserAdminService) getServletContext().getAttribute("userAdminService");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String blocked = request.getParameter("blocked");
        List<UserEntity> users;

        if(blocked == null || blocked.isEmpty()) {
            users = userAdminService.findAll();
        } else if("true".equals(blocked)) {
            users = userAdminService.findByBlocked(true);
        } else if ("false".equals(blocked)) {
            users = userAdminService.findByBlocked(false);
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        request.setAttribute("users", users);
        request.setAttribute("selectedBlocked", blocked);
        request.getRequestDispatcher("/WEB-INF/views/admin/users.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        HttpSession session = request.getSession();
        try {
            Long id = Long.parseLong(request.getParameter("id"));
            if("block".equals(action)) {
                userAdminService.updateBlocked(id, true);
                session.setAttribute("flashSuccess", "Пользователь заблокирован!");
            } else if ("unblock".equals(action)) {
                userAdminService.updateBlocked(id, false);
                session.setAttribute("flashSuccess", "Пользователь разблокирован!");
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
        } catch (ValidationException e) {
            request.getSession().setAttribute("flashError", e.getError().get("users"));
        } catch (NotFoundException e) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        response.sendRedirect(request.getContextPath() + "/admin/users");
    }
}
