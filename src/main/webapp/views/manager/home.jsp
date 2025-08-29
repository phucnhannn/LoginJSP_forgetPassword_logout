<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.User" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Manager Home</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
</head>
<body class="bg-light">
<%
    User user = (User) session.getAttribute("account");
    if (user == null || user.getRoleid() != 2) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }
%>
<div class="container mt-5">
    <div class="card shadow p-4">
        <h3 class="text-success text-center">
            Welcome Manager: <%= user.getUserName() %>!
        </h3>
        <h4 class="text-info text-center mt-2">
            You are logged in as <b>Manager</b> (roleid: 2)
        </h4>
        <div class="text-center mt-4">
            <a href="<%=request.getContextPath()%>/logout" class="btn btn-danger">Logout</a>
        </div>
    </div>
</div>
</body>
</html>
