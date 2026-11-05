package ru.itis.karimov.servlet.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.itis.karimov.entity.UniversityEntity;
import ru.itis.karimov.service.UniversityAdminService;

import java.io.IOException;
import java.util.List;

@WebServlet("/admin/universities")
public class UniversityAdminServlet extends HttpServlet {

    private UniversityAdminService universityAdminService;

    @Override
    public void init() {
        universityAdminService = (UniversityAdminService) getServletContext().getAttribute("universityAdminService");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<UniversityEntity> universities = universityAdminService.findAll();
        request.setAttribute("universities", universities);
        request.getRequestDispatcher("/WEB-INF/views/admin/universities.jsp").forward(request, response);
    }
}
