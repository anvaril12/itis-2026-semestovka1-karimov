package ru.itis.karimov.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ru.itis.karimov.entity.DormReviewEntity;
import ru.itis.karimov.entity.UserEntity;
import ru.itis.karimov.exception.ForbiddenException;
import ru.itis.karimov.exception.NotFoundException;
import ru.itis.karimov.exception.ValidationException;
import ru.itis.karimov.service.DormReviewService;

import java.io.IOException;

@WebServlet("/reviews")
public class DormReviewServlet extends HttpServlet {

    private DormReviewService dormReviewService;

    @Override
    public void init() {
        dormReviewService = (DormReviewService) getServletContext().getAttribute("dormReviewService");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Integer dormitoryId = Integer.parseInt(request.getParameter("dormitoryId"));
            request.setAttribute("reviews", dormReviewService.findByDormitoryId(dormitoryId));
            request.setAttribute("dormitoryId", dormitoryId);
            request.getRequestDispatcher("/WEB-INF/views/reviews.jsp").forward(request, response);
        } catch (NumberFormatException | NotFoundException e) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession();
        UserEntity user = (UserEntity) session.getAttribute("user");
        String action = request.getParameter("action");
        String text = request.getParameter("text");

        try {
            if("delete".equals(action)) {
                Long reviewId = Long.parseLong(request.getParameter("id"));
                Integer dormitoryId = Integer.parseInt(request.getParameter("dormitoryId"));

                dormReviewService.delete(user.getId(), reviewId);

                session.setAttribute("flashSuccess", "Отзыв успешно удален!");
                response.sendRedirect(request.getContextPath() + "/reviews?dormitoryId=" + dormitoryId);
                return;
            }

            Integer dormitoryId = Integer.parseInt(request.getParameter("dormitoryId"));
            Integer rating = parseRating(request.getParameter("rating"));

            if("create".equals(action)) {
                DormReviewEntity dormReviewEntity = DormReviewEntity.builder()
                        .userId(user.getId())
                        .dormitoryId(dormitoryId)
                        .text(text)
                        .rating(rating)
                        .build();

                dormReviewService.save(dormReviewEntity);
                session.setAttribute("flashSuccess", "Отзыв успешно добавлен");
            } else if ("update".equals(action)) {
                Long reviewId = Long.parseLong(request.getParameter("id"));
                DormReviewEntity dormReviewEntity = DormReviewEntity.builder()
                        .id(reviewId)
                        .userId(user.getId())
                        .dormitoryId(dormitoryId)
                        .text(text)
                        .rating(rating)
                        .build();

                dormReviewService.update(user.getId(), dormReviewEntity);
                session.setAttribute("flashSuccess", "Отзыв успешно изменен");
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
            response.sendRedirect(request.getContextPath() + "/reviews?dormitoryId=" + dormitoryId);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        } catch (NotFoundException e) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        } catch (ForbiddenException e) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
        } catch (ValidationException e) {
            Integer dormitoryId = Integer.parseInt(request.getParameter("dormitoryId"));
            request.setAttribute("errors", e.getError());
            request.setAttribute("reviews", dormReviewService.findByDormitoryId(dormitoryId));
            request.setAttribute("dormitoryId", dormitoryId);
            request.setAttribute("formAction", action);
            request.setAttribute("submittedText", text == null ? "" : text);
            request.setAttribute("submittedRating", parseRating(request.getParameter("rating")));
            if ("update".equals(action)) {
                request.setAttribute("editedReviewId", Long.parseLong(request.getParameter("id")));
            }
            request.getRequestDispatcher("/WEB-INF/views/reviews.jsp").forward(request, response);
        }
    }

    private Integer parseRating(String rating) {
        try {
            return Integer.valueOf(rating);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
