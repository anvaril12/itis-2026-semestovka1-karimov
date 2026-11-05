package ru.itis.karimov.servlet.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ru.itis.karimov.entity.QuestionEntity;
import ru.itis.karimov.exception.NotFoundException;
import ru.itis.karimov.exception.ValidationException;
import ru.itis.karimov.service.QuestionAdminService;

import java.io.IOException;
import java.util.List;

@WebServlet("/admin/questions")
public class QuestionAdminServlet extends HttpServlet {

    private QuestionAdminService questionAdminService;

    @Override
    public void init() {
        questionAdminService = (QuestionAdminService) getServletContext().getAttribute("questionAdminService");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("create".equals(action)) {
            request.getRequestDispatcher(
                    "/WEB-INF/views/admin/question-form.jsp"
            ).forward(request, response);
            return;
        }

        String idParam = request.getParameter("id");
        if(idParam != null) {
            try {
                Integer id = Integer.parseInt(idParam);
                QuestionEntity questionEntity = questionAdminService.findById(id);
                request.setAttribute("question", questionEntity);
                request.getRequestDispatcher("/WEB-INF/views/admin/question-view.jsp").forward(request, response);
                return;
            } catch (NumberFormatException | NotFoundException e) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
        }

        List<QuestionEntity> questions = questionAdminService.findAll();
        request.setAttribute("questions", questions);
        request.getRequestDispatcher("/WEB-INF/views/admin/questions.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String id = request.getParameter("id");
        String text = request.getParameter("text");
        String[] optionValues = request.getParameterValues("optionValues");
        String[] optionLabels = request.getParameterValues("optionLabels");
        String action = request.getParameter("action");
        try {
            if("create".equals(action)) {
                questionAdminService.create(text, optionValues, optionLabels);
                session.setAttribute("flashSuccess", "Вопрос успешно создан!");
            } else if ("delete".equals(action)) {
                int idQuestion = Integer.parseInt(id);
                questionAdminService.delete(idQuestion);
                session.setAttribute("flashSuccess", "Вопрос успешно удален!");
            } else if("update".equals(action)) {
                int idQuestion = Integer.parseInt(id);
                questionAdminService.update(idQuestion, text);
                session.setAttribute("flashSuccess", "Вопрос успешно обновлен");
            } else {
                session.setAttribute("flashError", "Неизвестное действие");
            }
        } catch (ValidationException e) {
            request.setAttribute("errors", e.getError());
            request.setAttribute("text", text);
            request.getRequestDispatcher("/WEB-INF/views/admin/question-form.jsp").forward(request, response);
            return;
        } catch (NumberFormatException e) {
            response.sendError((HttpServletResponse.SC_BAD_REQUEST));
            return;
        }
        response.sendRedirect(request.getContextPath() + "/admin/questions");
    }
 }
