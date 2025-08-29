<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Đăng nhập</title>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
<style>
    body { background: #f9f9f9; }
    .login-box { max-width: 400px; margin: 80px auto; background: #fff; padding: 30px; border-radius: 8px; box-shadow: 0 2px 10px rgba(0,0,0,.1); }
    .login-box h3 { text-align: center; margin-bottom: 25px; font-weight: bold; }
    .input-group-text { background: #f1f1f1; }
    .btn-login { width: 100%; }
    .extra-links { margin-top: 15px; text-align: center; }
</style>
</head>
<body>
<div class="login-box">
    <h3>Đăng Nhập Vào Hệ Thống</h3>

    <%
        Object alert = request.getAttribute("alert");
        Object success = request.getAttribute("success");
        if (alert != null) {
    %>
        <div class="alert alert-danger" role="alert"><%= alert.toString() %></div>
    <%
        }
        if (success != null) {
    %>
        <div class="alert alert-success" role="alert"><%= success.toString() %></div>
    <%
        }
    %>

    <form action="<%=request.getContextPath()%>/login" method="post">
        <div class="input-group mb-3">
            <div class="input-group-prepend"><span class="input-group-text"><i class="fa fa-user"></i></span></div>
            <input type="text" class="form-control" name="username" placeholder="Tên đăng nhập" required>
        </div>
        <div class="input-group mb-3">
            <div class="input-group-prepend"><span class="input-group-text"><i class="fa fa-lock"></i></span></div>
            <input type="password" class="form-control" name="password" placeholder="Mật khẩu" required>
        </div>
        <div class="form-group form-check d-flex justify-content-between">
            <div>
                <input type="checkbox" class="form-check-input" name="remember" id="remember">
                <label class="form-check-label" for="remember">Nhớ tôi</label>
            </div>
            <a href="<%=request.getContextPath()%>/forgot">Quên mật khẩu?</a>
        </div>
        <button type="submit" class="btn btn-primary btn-login">Đăng nhập</button>
    </form>

    <div class="extra-links">
        <p>Nếu bạn chưa có tài khoản trên hệ thống, thì hãy 
           <a href="<%=request.getContextPath()%>/register.jsp">Đăng ký</a>
        </p>
    </div>
</div>
<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>