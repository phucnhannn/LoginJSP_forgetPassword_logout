<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Add Category</title>
</head>
<body>
<h2>Add Category</h2>
<c:if test="${not empty error}">
    <div style="color:red">${error}</div>
</c:if>
<form action="${pageContext.request.contextPath}/admin/category/add" method="post" enctype="multipart/form-data">
    <label>Name:</label>
    <input type="text" name="name" required/><br/>

    <label>Icon (file):</label>
    <input type="file" name="iconFile" accept="image/png,image/jpeg,image/webp,image/svg+xml" /><br/>
    <small>Allowed: PNG, JPG/JPEG, WEBP, SVG. Max size: 5 MB. If no file selected you can use icon text below.</small>
    <br/>
    <label>Icon (text fallback):</label>
    <input type="text" name="icon" placeholder="icon path or text"/><br/>

    <button type="submit">Save</button>
    <a href="${pageContext.request.contextPath}/admin/category/list">Cancel</a>
</form>
</body>
</html>