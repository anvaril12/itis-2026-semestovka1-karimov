package ru.itis.karimov.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.itis.karimov.entity.UserEntity;
import ru.itis.karimov.exception.AuthenticationException;
import ru.itis.karimov.service.UserService;

import java.io.IOException;

@WebServlet("/signin")
public class SignInServlet extends HttpServlet {
    private UserService userService;

    @Override
    public void init() {
        userService = (UserService) getServletContext().getAttribute("userService");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/signin.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String login = request.getParameter("login");
        String password = request.getParameter("password");
        try {
            UserEntity userEntity = userService.login(login, password);
            request.getSession();
            request.changeSessionId();
            request.getSession().setAttribute("user", userEntity);
            response.sendRedirect(request.getContextPath() + "/profile");
        } catch(AuthenticationException e) {
            request.setAttribute("error", e.getMessage());
            request.setAttribute("login", login);
            request.getRequestDispatcher("/WEB-INF/views/signin.jsp").forward(request, response);
        }
    }
}
