package controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import model.User;
import service.UserService;
import service.UserServiceImpl;

@SuppressWarnings("serial")
// CHỈ map "/login" nếu đã có RootRedirectController map "/"
@WebServlet(urlPatterns = {"/login"})
public class LoginController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute("account") != null) {
            resp.sendRedirect(req.getContextPath() + "/waiting");
            return;
        }

        // Auto login từ cookie "username"
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("username".equals(cookie.getName())) {
                    String username = cookie.getValue();
                    UserService service = new UserServiceImpl();
                    User user = service.get(username);
                    if (user != null) {
                        session = req.getSession(true);
                        session.setAttribute("account", user);
                        resp.sendRedirect(req.getContextPath() + "/waiting");
                        return;
                    }
                }
            }
        }

        // login.jsp hiện nằm trong /views
        req.getRequestDispatcher("/views/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("text/html; charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");
        req.setCharacterEncoding("UTF-8");

        String username = req.getParameter("username");
        String password = req.getParameter("password");
        boolean isRememberMe = "on".equals(req.getParameter("remember"));
        String alertMsg = "";

        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            alertMsg = "Tài khoản hoặc mật khẩu không được rỗng";
            req.setAttribute("alert", alertMsg);
            req.getRequestDispatcher("/views/login.jsp").forward(req, resp);
            return;
        }

        UserService service = new UserServiceImpl();
        User user = service.login(username, password);

        System.out.println("[DEBUG] Đăng nhập cho: " + username);
        System.out.println("[DEBUG] Đối tượng User sau khi đăng nhập: " + (user == null ? "null" : "Tìm thấy với vai trò " + user.getRoleid()));

        if (user != null) {
            HttpSession session = req.getSession(true);
            session.setAttribute("account", user);
            System.out.println("[DEBUG] LOGIN OK, JSESSIONID=" + session.getId());

            if (isRememberMe) {
                saveRememberMe(req, resp, username);
            }
            resp.sendRedirect(req.getContextPath() + "/waiting");
        } else {
            User userCheck = service.get(username);
            if (userCheck == null) {
                alertMsg = "Tài khoản không tồn tại";
            } else {
                alertMsg = "Tài khoản hoặc mật khẩu không đúng";
            }
            req.setAttribute("alert", alertMsg);
            // Forward về /views/login.jsp vì file nằm trong /views
            req.getRequestDispatcher("/views/login.jsp").forward(req, resp);
        }
    }

    private void saveRememberMe(HttpServletRequest req, HttpServletResponse resp, String username) {
        Cookie cookie = new Cookie("username", username);
        cookie.setMaxAge(30 * 60); // 30 phút
        String ctx = req.getContextPath();
        cookie.setPath((ctx == null || ctx.isEmpty()) ? "/" : ctx);
        cookie.setHttpOnly(true);
        cookie.setSecure(req.isSecure());
        resp.addCookie(cookie);
    }
}