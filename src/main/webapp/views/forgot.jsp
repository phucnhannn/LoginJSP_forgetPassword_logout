<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Quên mật khẩu</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
</head>
<body class="bg-light">
<div class="container" style="max-width: 500px; margin-top: 60px;">
    <div class="card">
        <div class="card-body">
            <h4 class="card-title text-center mb-4">Quên mật khẩu</h4>

            <%
                Object alert = request.getAttribute("alert");
                Object success = request.getAttribute("success");
            %>
            <% if (alert != null) { %>
                <div class="alert alert-danger"><%= alert.toString() %></div>
            <% } %>
            <% if (success != null) { %>
                <div class="alert alert-success"><%= success.toString() %></div>
            <% } %>

            <form action="<%=request.getContextPath()%>/forgot" method="post">
                <div class="form-group">
                    <label for="usernameOrEmail">Tên đăng nhập hoặc Email</label>
                    <input type="text" class="form-control" id="usernameOrEmail" name="usernameOrEmail" required>
                </div>
                <button type="submit" class="btn btn-primary btn-block">Gửi liên kết đặt lại</button>
            </form>

            <div class="mt-3 text-center">
                <a href="<%=request.getContextPath()%>/login">Quay lại đăng nhập</a>
            </div>
        </div>
    </div>
</div>
</body>
</html>