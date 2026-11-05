package ru.itis.karimov.servlet.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ru.itis.karimov.entity.DormitoryEntity;
import ru.itis.karimov.exception.NotFoundException;
import ru.itis.karimov.exception.ValidationException;
import ru.itis.karimov.service.DormitoryAdminService;
import ru.itis.karimov.service.UniversityAdminService;

import java.io.IOException;
import java.util.List;

@WebServlet("/admin/dormitories")
public class DormitoryAdminServlet extends HttpServlet {

    private DormitoryAdminService dormitoryAdminService;
    private UniversityAdminService universityAdminService;

    @Override
    public void init() {
        dormitoryAdminService = (DormitoryAdminService) getServletContext().getAttribute("dormitoryAdminService");
        universityAdminService = (UniversityAdminService) getServletContext().getAttribute("universityAdminService");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if("create".equals(action)) {
            request.setAttribute("universities", universityAdminService.findAll());
            request.getRequestDispatcher("/WEB-INF/views/admin/dormitory-form.jsp").forward(request, response);
            return;
        }

        if("edit".equals(action)) {
            try {
                Integer id = Integer.parseInt(request.getParameter("id"));
                DormitoryEntity dormitory = dormitoryAdminService.findById(id);
                request.setAttribute("dormitory", dormitory);
                request.setAttribute("universities", universityAdminService.findAll());
                request.getRequestDispatcher("/WEB-INF/views/admin/dormitory-form.jsp").forward(request, response);
                return;
            } catch (NumberFormatException | NotFoundException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
        }

        List<DormitoryEntity> list = dormitoryAdminService.findAll();
        request.setAttribute("dormitories", list);
        request.getRequestDispatcher("/WEB-INF/views/admin/dormitories.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

        String action = request.getParameter("action");
        String name = request.getParameter("name");
        String address = request.getParameter("address");
        String universityIdPar = request.getParameter("university_id");

        if ("delete".equals(action)) {
            try {
                Integer id = Integer.parseInt(request.getParameter("id"));
                dormitoryAdminService.delete(id);
                session.setAttribute("flashSuccess", "Общежитие успешно удалено");
                response.sendRedirect(request.getContextPath() + "/admin/dormitories");
                return;
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            } catch (ValidationException e) {
                session.setAttribute("flashError", e.getError().get("dormitory"));
                response.sendRedirect(request.getContextPath() + "/admin/dormitories");
                return;
            }
        }

        if (!"create".equals(action) && !"update".equals(action)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        Integer universityId;

        try {
            universityId = Integer.parseInt(universityIdPar);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        Integer id = null;

        if ("update".equals(action)) {
            try {
                id = Integer.parseInt(request.getParameter("id"));
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
        }

        DormitoryEntity dormitory = DormitoryEntity.builder()
                .id(id)
                .universityId(universityId)
                .address(address)
                .name(name)
                .build();

        try {
            if("create".equals(action)) {
                dormitoryAdminService.save(dormitory);
                session.setAttribute("flashSuccess", "Общежитие успешно добавлено");
            } else {
                dormitoryAdminService.update(dormitory);
                session.setAttribute("flashSuccess", "Общежитие успешно изменено");
            }
            response.sendRedirect(request.getContextPath() + "/admin/dormitories");
        } catch (NotFoundException e) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        } catch (ValidationException e) {
            request.setAttribute("errors", e.getError());
            request.setAttribute("dormitory", dormitory);
            request.setAttribute("universities", universityAdminService.findAll());
            request.getRequestDispatcher( "/WEB-INF/views/admin/dormitory-form.jsp").forward(request, response);
        }
    }
}
