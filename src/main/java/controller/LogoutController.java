package controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = {"/logout"})
public class LogoutController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        // Xóa cookie remember me
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            String ctx = req.getContextPath();
            String path = (ctx == null || ctx.isEmpty()) ? "/" : ctx;
            for (Cookie c : cookies) {
                if ("username".equals(c.getName())) {
                    Cookie del = new Cookie("username", "");
                    del.setPath(path);
                    del.setMaxAge(0);
                    del.setHttpOnly(true);
                    del.setSecure(req.isSecure());
                    resp.addCookie(del);
                }
            }
        }

        resp.sendRedirect(req.getContextPath() + "/login");
    }
}