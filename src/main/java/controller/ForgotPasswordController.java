package controller;

import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import model.User;
import service.UserService;
import service.UserServiceImpl;
import utils.MailUtil;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = {"/forgot"})
public class ForgotPasswordController extends HttpServlet {

    private final UserService userService = new UserServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/views/forgot.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");

        String term = req.getParameter("usernameOrEmail");
        if (term == null || term.trim().isEmpty()) {
            req.setAttribute("alert", "Vui lòng nhập tên đăng nhập hoặc email.");
            req.getRequestDispatcher("/views/forgot.jsp").forward(req, resp);
            return;
        }
        term = term.trim();

        // Tạo token (15 phút)
        Instant expireAt = Instant.now().plus(15, ChronoUnit.MINUTES);
        String token = userService.createResetToken(term, expireAt);

        // Luôn hiển thị thông điệp "đã gửi" để tránh lộ thông tin tài khoản
        req.setAttribute("success", "Nếu tài khoản tồn tại, hệ thống đã tạo liên kết đặt lại mật khẩu.");

        // Chỉ gửi mail nếu tìm thấy user và có email
        User user = userService.getByUsernameOrEmail(term);
        boolean mailSent = false;
        if (token != null && user != null && user.getEmail() != null && !user.getEmail().isEmpty()) {
            String baseUrl = req.getScheme() + "://" + req.getServerName()
                    + ((req.getServerPort() == 80 || req.getServerPort() == 443) ? "" : ":" + req.getServerPort())
                    + req.getContextPath();
            String resetLink = baseUrl + "/reset?token=" + token;

            String subject = "Đặt lại mật khẩu";
            String html = "<div style='font-family:Arial,Helvetica,sans-serif;font-size:14px;color:#333'>"
                    + "<p>Chào " + (user.getFullName() != null ? user.getFullName() : user.getUserName()) + ",</p>"
                    + "<p>Bạn vừa yêu cầu đặt lại mật khẩu. Nhấp vào nút bên dưới để đặt lại (hiệu lực trong 15 phút):</p>"
                    + "<p style='margin:16px 0'><a href='" + resetLink + "' "
                    + "style='background:#0069d9;color:#fff;padding:10px 16px;text-decoration:none;border-radius:4px' "
                    + "target='_blank' rel='noopener'>Đặt lại mật khẩu</a></p>"
                    + "<p>Nếu nút không hiển thị, hãy dán liên kết sau vào trình duyệt:</p>"
                    + "<p><a href='" + resetLink + "'>" + resetLink + "</a></p>"
                    + "<p>Nếu không phải bạn yêu cầu, vui lòng bỏ qua email này.</p>"
                    + "</div>";

            mailSent = MailUtil.sendHtml(user.getEmail(), subject, html);

            // Tuỳ ý: vẫn in ra console để dev debug
            System.out.println("[RESET LINK] " + resetLink);
        }

        req.setAttribute("mailSent", mailSent);
        // Có thể bỏ hiển thị resetLink dev nếu muốn bảo mật tuyệt đối
        // req.setAttribute("resetLink", resetLink);

        req.getRequestDispatcher("/views/forgot.jsp").forward(req, resp);
    }
}