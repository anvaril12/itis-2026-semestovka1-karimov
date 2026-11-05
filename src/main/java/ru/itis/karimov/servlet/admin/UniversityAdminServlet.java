package ru.itis.karimov.servlet.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ru.itis.karimov.entity.UniversityEntity;
import ru.itis.karimov.exception.DuplicateException;
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

        if("edit".equals(action)) {
            Integer id = Integer.parseInt(request.getParameter("id"));
            UniversityEntity universityEntity = universityAdminService.findById(id);

            request.setAttribute("university", universityEntity);
            request.setAttribute("city", universityEntity.getCity());
            request.setAttribute("name", universityEntity.getName());

            request.getRequestDispatcher( "/WEB-INF/views/admin/university-form.jsp").forward(request, response);
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

        if (!"create".equals(action) && !"update".equals(action) && !"delete".equals(action)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        UniversityEntity universityEntity;

        try {
            if ("create".equals(action)) {
                universityEntity = UniversityEntity.builder()
                        .name(name)
                        .city(city)
                        .build();

                universityAdminService.save(universityEntity);
                session.setAttribute("flashSuccess", "Вуз успешно добавлен!");
            } else if ("update".equals(action)) {
                Integer id = Integer.parseInt(request.getParameter("id"));

                universityEntity = UniversityEntity.builder()
                        .id(id)
                        .name(name)
                        .city(city)
                        .build();

                universityAdminService.update(universityEntity);
                session.setAttribute("flashSuccess", "Вуз успешно обновлён!");
            } else {
                Integer id = Integer.parseInt(request.getParameter("id"));

                universityAdminService.delete(id);
                session.setAttribute("flashSuccess", "Вуз успешно удалён!");
            }

            response.sendRedirect(request.getContextPath() + "/admin/universities");
            return;
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        } catch (DuplicateException e) {
            request.setAttribute("duplicateError", e.getMessage());
        } catch (ValidationException e) {
            if ("delete".equals(action)) {
                session.setAttribute("flashError", e.getError().get("university"));
                response.sendRedirect(request.getContextPath() + "/admin/universities");
                return;
            }
            request.setAttribute("errors", e.getError());
        }

        request.setAttribute("name", name == null ? "" : name);
        request.setAttribute("city", city == null ? "" : city);

        if ("update".equals(action)) {
            try {
                Integer id = Integer.parseInt(request.getParameter("id"));
                UniversityEntity university = universityAdminService.findById(id);
                request.setAttribute("university", university);
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
        }

        request.getRequestDispatcher("/WEB-INF/views/admin/university-form.jsp").forward(request, response);
    }
}
