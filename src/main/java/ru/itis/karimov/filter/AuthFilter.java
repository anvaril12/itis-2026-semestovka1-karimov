package ru.itis.karimov.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ru.itis.karimov.entity.UserEntity;
import ru.itis.karimov.exception.AuthenticationException;
import ru.itis.karimov.exception.NotFoundException;
import ru.itis.karimov.service.UserService;

import java.io.IOException;

@WebFilter("/*")
public class AuthFilter implements Filter {

    private UserService userService;

    @Override
    public void init(FilterConfig filterConfig) {
        userService = (UserService) filterConfig.getServletContext().getAttribute("userService");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse resp = (HttpServletResponse) response;
        HttpServletRequest req = (HttpServletRequest) request;

        String path = req.getServletPath();

        if(isPublic(path)) {
            chain.doFilter(request, response);
            return;
        }

        resp.setHeader("Cache-Control", "no-store");
        HttpSession session = req.getSession(false);
        UserEntity sessionUser = session == null ? null : (UserEntity) session.getAttribute("user");

        if(sessionUser == null) {
            resp.sendRedirect(req.getContextPath() + "/signin");
            return;
        }

        UserEntity activeUser;
        try {
            activeUser = userService.getActiveUser(sessionUser.getId());
        } catch (AuthenticationException | NotFoundException e) {
            session.invalidate();
            resp.sendRedirect(req.getContextPath() + "/signin");
            return;
        }

        session.setAttribute("user", activeUser);
        if (!activeUser.isConfirmed() && !path.equals("/confirm") && !path.equals("/signout")) {
            resp.sendRedirect(req.getContextPath() + "/confirm");
            return;
        }
        chain.doFilter(request, response);
    }

    private boolean isPublic(String path) {
        return path.equals("/signup") || path.equals("/signin") || path.startsWith("/js/") || path.startsWith("/css/");
    }
}
