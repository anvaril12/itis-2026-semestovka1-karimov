package ru.itis.karimov.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.itis.karimov.entity.UserEntity;
import ru.itis.karimov.service.CompatibilityService;

import java.io.IOException;

@WebServlet("/matches")
public class MatchServlet extends HttpServlet {
    private CompatibilityService compatibilityService;

    @Override
    public void init() {
        compatibilityService = (CompatibilityService) getServletContext().getAttribute("compatibilityService");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserEntity user = (UserEntity) request.getSession().getAttribute("user");
        request.setAttribute("matches", compatibilityService.findMatches(user));
        request.getRequestDispatcher("/WEB-INF/views/matches.jsp").forward(request, response);
    }
}
