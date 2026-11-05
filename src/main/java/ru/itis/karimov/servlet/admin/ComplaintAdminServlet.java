package ru.itis.karimov.servlet.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ru.itis.karimov.entity.ComplaintEntity;
import ru.itis.karimov.entity.enums.ComplaintStatus;
import ru.itis.karimov.exception.NotFoundException;
import ru.itis.karimov.exception.ValidationException;
import ru.itis.karimov.service.ComplaintAdminService;

import java.io.IOException;
import java.util.List;

@WebServlet("/admin/complaints")
public class ComplaintAdminServlet extends HttpServlet {

    private ComplaintAdminService complaintAdminService;

    @Override
    public void init() {
        complaintAdminService = (ComplaintAdminService) getServletContext().getAttribute("complaintAdminService");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String status = request.getParameter("status");
        try {
            List<ComplaintEntity> list;
            if(status == null || status.isBlank()) {
                list = complaintAdminService.findAll();
            } else {
                list = complaintAdminService.findByStatus(ComplaintStatus.valueOf(status));
            }
            request.setAttribute("complaints", list);
            request.setAttribute("selectedStatus", status);
            request.getRequestDispatcher("/WEB-INF/views/admin/complaints.jsp").forward(request, response);

        } catch(IllegalArgumentException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String statusParam = request.getParameter("status");

        if(statusParam == null || statusParam.isBlank()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        try {
            Long id = Long.parseLong(request.getParameter("id"));
            ComplaintStatus status = ComplaintStatus.valueOf(statusParam);

            complaintAdminService.updateStatus(id, status);
            request.getSession().setAttribute("flashSuccess", "Статус успешно изменен!");
        } catch (ValidationException e) {
            request.getSession().setAttribute("flashError", String.join("; ", e.getError().values()));
        } catch (NotFoundException e) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        } catch (IllegalArgumentException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        response.sendRedirect(request.getContextPath() + "/admin/complaints");
    }
}
