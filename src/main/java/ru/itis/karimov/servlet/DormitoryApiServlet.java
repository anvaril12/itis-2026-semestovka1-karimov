package ru.itis.karimov.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.itis.karimov.entity.DormitoryEntity;
import ru.itis.karimov.service.DormitoryService;

import java.io.IOException;
import java.util.List;

@WebServlet("/api/dormitories")
public class DormitoryApiServlet extends HttpServlet {
    private DormitoryService dormitoryService;
    private ObjectMapper objectMapper;

    @Override
    public void init() {
        dormitoryService = (DormitoryService) getServletContext().getAttribute("dormitoryService");
        objectMapper = new ObjectMapper();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String universityId = request.getParameter("universityId");
        int id;
        try {
            id = Integer.parseInt(universityId);
        } catch(NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        List<DormitoryEntity> dormitoryEntityList = dormitoryService.getDormitoriesByUniversityId(id);
        response.setContentType("application/json");
        objectMapper.writeValue(response.getWriter(), dormitoryEntityList);
    }
}
