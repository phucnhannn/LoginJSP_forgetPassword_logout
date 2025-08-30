<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="model.User" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Trang Chủ</title>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
</head>
<body class="bg-light">

<%
    User user = (User) session.getAttribute("account");
    // Kiểm tra nếu không có session thì trả về trang login
    if (user == null) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }

    // Lấy tên vai trò từ roleid
    String roleName = "User";
    if (user.getRoleid() == 1) {
        roleName = "Admin";
    } else if (user.getRoleid() == 2) {
        roleName = "Manager";
    }
%>

<div class="container mt-5">
    <div class="card shadow p-4">
        <h3 class="text-success text-center">
            Chào mừng <%= user.getUserName() %> đã đăng nhập thành công!
        </h3>
        <h4 class="text-info text-center mt-2">
            Vai trò của bạn: <%= roleName %>
        </h4>
        <div class="text-center mt-4">
            <%-- Nếu là Admin thì hiện nút Quản lý category --%>
            <% if ("Admin".equals(roleName)) { %>
                <a href="<%=request.getContextPath()%>/admin/category/list" class="btn btn-primary mr-2">Quản lý category</a>
            <% } %>
            <a href="<%=request.getContextPath()%>/logout" class="btn btn-danger">Đăng xuất</a>
        </div>
    </div>
</div>

</body>
</html>