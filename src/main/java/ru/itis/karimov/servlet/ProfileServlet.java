package ru.itis.karimov.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.itis.karimov.entity.UserEntity;
import ru.itis.karimov.exception.ValidationException;
import ru.itis.karimov.service.DormitoryService;
import ru.itis.karimov.service.UserService;

import java.io.IOException;

@WebServlet("/profile")
public class ProfileServlet extends HttpServlet {
    private DormitoryService dormitoryService;
    private UserService userService;

    @Override
    public void init() {
        dormitoryService = (DormitoryService) getServletContext().getAttribute("dormitoryService");
        userService = (UserService) getServletContext().getAttribute("userService");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("universities", dormitoryService.getAllUniversities());

        // В сессии у пользователя только id общежития, а показать нужно название
        UserEntity user = (UserEntity) request.getSession().getAttribute("user");
        if (user.getDormitoryId() != null) {
            dormitoryService.getDormitoryById(user.getDormitoryId())
                    .ifPresent(d -> request.setAttribute("currentDormitory", d));
        }

        request.getRequestDispatcher("/WEB-INF/views/profile.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String dormitoryId = request.getParameter("dormitoryId");
        String universityId = request.getParameter("universityId");
        UserEntity userEntity = (UserEntity) request.getSession().getAttribute("user");
        try {
            UserEntity updated = userService.updateDormitory(userEntity.getId(), dormitoryId, universityId);
            request.getSession().setAttribute("user", updated);
            response.sendRedirect(request.getContextPath() + "/profile");
        } catch (ValidationException e) {
            request.setAttribute("errors", e.getError());
            doGet(request, response);
        }
    }
}
