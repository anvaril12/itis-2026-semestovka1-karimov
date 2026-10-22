package ru.itis.karimov.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.itis.karimov.dto.RegistrationForm;
import ru.itis.karimov.exception.ValidationException;
import ru.itis.karimov.service.UserService;

import java.io.IOException;

@WebServlet("/signup")
public class SignUpServlet extends HttpServlet {

    private UserService userService;

    @Override
    public void init() {
        userService = (UserService) getServletContext().getAttribute("userService");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/signup.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        RegistrationForm form = new RegistrationForm(
                request.getParameter("login"),
                request.getParameter("firstName"),
                request.getParameter("lastName"),
                request.getParameter("password"),
                request.getParameter("passwordConfirm"),
                request.getParameter("email"),
                request.getParameter("gender"),
                request.getParameter("contacts"),
                request.getParameter("course"),
                request.getParameter("birthYear")
        );

        try {
            userService.register(form);
            response.sendRedirect(request.getContextPath() + "/signin");
        } catch (ValidationException e) {
            request.setAttribute("errors", e.getError());
            request.setAttribute("form", form);
            request.getRequestDispatcher("/WEB-INF/views/signup.jsp").forward(request, response);
        }
    }
 }
