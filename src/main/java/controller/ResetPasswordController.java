package controller;

import java.io.IOException;
import java.time.Instant;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import model.User;
import service.UserService;
import service.UserServiceImpl;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = {"/reset"})
public class ResetPasswordController extends HttpServlet {

    private final UserService userService = new UserServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String token = req.getParameter("token");
        if (token == null || token.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        User u = userService.getByResetToken(token);
        if (u == null || u.getResetExpiry() == null || u.getResetExpiry().toInstant().isBefore(Instant.now())) {
            req.setAttribute("alert", "Liên kết đặt lại mật khẩu không hợp lệ hoặc đã hết hạn.");
            req.getRequestDispatcher("/views/forgot.jsp").forward(req, resp);
            return;
        }

        req.setAttribute("token", token);
        req.getRequestDispatcher("/views/reset.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");

        String token = req.getParameter("token");
        String pass1 = req.getParameter("password");
        String pass2 = req.getParameter("confirmPassword");

        if (token == null || token.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        if (pass1 == null || pass2 == null || pass1.isEmpty() || pass2.isEmpty()) {
            req.setAttribute("alert", "Vui lòng nhập đầy đủ mật khẩu.");
            req.setAttribute("token", token);
            req.getRequestDispatcher("/views/reset.jsp").forward(req, resp);
            return;
        }

        if (!pass1.equals(pass2)) {
            req.setAttribute("alert", "Xác nhận mật khẩu không khớp.");
            req.setAttribute("token", token);
            req.getRequestDispatcher("/views/reset.jsp").forward(req, resp);
            return;
        }

        if (pass1.length() < 6) {
            req.setAttribute("alert", "Mật khẩu phải có ít nhất 6 ký tự.");
            req.setAttribute("token", token);
            req.getRequestDispatcher("/views/reset.jsp").forward(req, resp);
            return;
        }

        boolean ok = userService.resetPassword(token, pass1);
        if (!ok) {
            req.setAttribute("alert", "Liên kết không hợp lệ hoặc đã hết hạn.");
            req.getRequestDispatcher("/views/forgot.jsp").forward(req, resp);
            return;
        }

        req.setAttribute("success", "Đặt lại mật khẩu thành công. Vui lòng đăng nhập.");
        req.getRequestDispatcher("/views/login.jsp").forward(req, resp);
    }
}