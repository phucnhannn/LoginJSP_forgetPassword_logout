<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Đặt lại mật khẩu</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
</head>
<body class="bg-light">
<div class="container" style="max-width: 500px; margin-top: 60px;">
    <div class="card">
        <div class="card-body">
            <h4 class="card-title text-center mb-4">Đặt lại mật khẩu</h4>

            <%
                Object alert = request.getAttribute("alert");
                if (alert != null) {
            %>
                <div class="alert alert-danger"><%= alert.toString() %></div>
            <% } %>

            <form action="<%=request.getContextPath()%>/reset" method="post">
                <input type="hidden" name="token" value="<%= request.getAttribute("token") %>">
                <div class="form-group">
                    <label for="password">Mật khẩu mới</label>
                    <input type="password" class="form-control" id="password" name="password" required minlength="6">
                </div>
                <div class="form-group">
                    <label for="confirmPassword">Xác nhận mật khẩu</label>
                    <input type="password" class="form-control" id="confirmPassword" name="confirmPassword" required minlength="6">
                </div>
                <button type="submit" class="btn btn-success btn-block">Cập nhật mật khẩu</button>
            </form>

            <div class="mt-3 text-center">
                <a href="<%=request.getContextPath()%>/login">Quay lại đăng nhập</a>
            </div>
        </div>
    </div>
</div>
</body>
</html>