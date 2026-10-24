package ru.itis.karimov.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.itis.karimov.entity.UserEntity;
import ru.itis.karimov.exception.ValidationException;
import ru.itis.karimov.service.QuestionnaireService;

import java.io.IOException;

@WebServlet("/questionnaire")
public class QuestionnaireServlet extends HttpServlet {
    private QuestionnaireService questionnaireService;

    @Override
    public void init() {
        questionnaireService = (QuestionnaireService) getServletContext().getAttribute("questionnaireService");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserEntity user = (UserEntity) request.getSession().getAttribute("user");
        request.setAttribute("questions", questionnaireService.getQuestionnaire(user.getId()));
        request.getRequestDispatcher("/WEB-INF/views/questionnaire.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserEntity user = (UserEntity) request.getSession().getAttribute("user");
        try {
            questionnaireService.saveAnswers(user.getId(), request.getParameterMap());
            response.sendRedirect(request.getContextPath() + "/questionnaire");
        } catch(ValidationException e) {
            request.setAttribute("errors", e.getError());
            doGet(request, response);
        }
    }
}
