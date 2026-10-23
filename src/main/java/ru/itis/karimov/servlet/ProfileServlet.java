package ru.itis.karimov.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.itis.karimov.service.DormitoryService;
import java.io.IOException;

@WebServlet("/profile")
public class ProfileServlet extends HttpServlet {
    private DormitoryService dormitoryService;

    @Override
    public void init() {
        dormitoryService = (DormitoryService) getServletContext().getAttribute("dormitoryService");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("universities", dormitoryService.getAllUniversities());
        request.getRequestDispatcher("/WEB-INF/views/profile.jsp").forward(request, response);
    }

}
