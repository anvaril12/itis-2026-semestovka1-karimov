package ru.itis.karimov.servlet.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ru.itis.karimov.exception.NotFoundException;
import ru.itis.karimov.service.DormReviewService;

import java.io.IOException;

@WebServlet("/admin/reviews")
public class DormReviewAdminServlet extends HttpServlet {
    private DormReviewService dormReviewService;

    @Override
    public void init() {
        dormReviewService = (DormReviewService) getServletContext().getAttribute("dormReviewService");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("reviews", dormReviewService.findAll());
        request.getRequestDispatcher("/WEB-INF/views/admin/reviews.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            dormReviewService.deleteByAdmin(Long.parseLong(request.getParameter("id")));
            HttpSession session = request.getSession();
            session.setAttribute("flashSuccess", "Отзыв удалён");
            response.sendRedirect(request.getContextPath() + "/admin/reviews");
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        } catch (NotFoundException e) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}
