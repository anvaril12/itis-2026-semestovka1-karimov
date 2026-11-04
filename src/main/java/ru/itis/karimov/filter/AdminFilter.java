package ru.itis.karimov.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ru.itis.karimov.entity.UserEntity;
import ru.itis.karimov.entity.enums.Role;

import java.io.IOException;

@WebFilter("/admin/*")
public class AdminFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        HttpSession session = httpServletRequest.getSession(false);

        UserEntity user = null;
        if(session != null) {
            user = (UserEntity) session.getAttribute("user");
        }

        if(user == null) {
            httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + "/signin");
            return;
        }

        if(user.getRole() != Role.ADMIN) {
            httpServletResponse.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        chain.doFilter(httpServletRequest, httpServletResponse);
    }
}
