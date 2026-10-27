package ru.itis.karimov.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.itis.karimov.entity.UserEntity;
import ru.itis.karimov.service.RoommateRequestService;
import java.io.IOException;

@WebServlet("/requests")
public class RequestServlet extends HttpServlet {
    private RoommateRequestService roommateRequestService;

    @Override
    public void init() {
        roommateRequestService = (RoommateRequestService) getServletContext().getAttribute("roommateRequestService");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserEntity user = (UserEntity) request.getSession().getAttribute("user");
        request.setAttribute("incoming", roommateRequestService.getIncoming(user.getId()));
        request.setAttribute("outgoing", roommateRequestService.getOutgoing(user.getId()));
        request.getRequestDispatcher("/WEB-INF/views/requests.jsp").forward(request, response);
    }
}
