package ru.itis.karimov.servlet.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ru.itis.karimov.entity.UniversityEntity;
import ru.itis.karimov.exception.ValidationException;
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
        String action = request.getParameter("action");
        if("create".equals(action)) {
            request.getRequestDispatcher("/WEB-INF/views/admin/university-form.jsp").forward(request, response);
            return;
        }

        List<UniversityEntity> universities = universityAdminService.findAll();
        request.setAttribute("universities", universities);
        request.getRequestDispatcher("/WEB-INF/views/admin/universities.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        String name = request.getParameter("name");
        String city = request.getParameter("city");
        HttpSession session = request.getSession();

        if(!"create".equals(action)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        UniversityEntity universityEntity = UniversityEntity.builder()
                .city(city)
                .name(name)
                .build();
        try {
            universityAdminService.save(universityEntity);
            session.setAttribute("flashSuccess", "Вуз успешно добавлен!");
            response.sendRedirect(request.getContextPath() + "/admin/universities");
        } catch (ValidationException e) {
            request.setAttribute("errors", e.getError());
            request.setAttribute("name", name == null ? "" : name);
            request.setAttribute("city", city == null ? "" : city);
            request.getRequestDispatcher("/WEB-INF/views/admin/university-form.jsp").forward(request, response);
        }
    }
}
