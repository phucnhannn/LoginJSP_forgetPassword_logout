<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<html>
<head>
    <title>Edit Category</title>
</head>
<body>
<h2>Edit Category</h2>
<c:if test="${not empty error}">
    <div style="color:red">${error}</div>
</c:if>
<form action="${pageContext.request.contextPath}/admin/category/edit" method="post" enctype="multipart/form-data">
    <input type="hidden" name="id" value="${category.id}"/>

    <label>Name:</label>
    <input type="text" name="name" value="${category.name}" required/><br/>

    <label>Current Icon:</label>
    <c:choose>
        <c:when test="${not empty category.icon and fn:startsWith(category.icon, 'uploads/')}">
            <img src="${pageContext.request.contextPath}/${category.icon}" alt="${category.name}" style="max-width:100px; max-height:80px;" /><br/>
        </c:when>
        <c:when test="${not empty category.icon and fn:startsWith(category.icon, '/uploads/')}">
            <img src="${pageContext.request.contextPath}${category.icon}" alt="${category.name}" style="max-width:100px; max-height:80px;" /><br/>
        </c:when>
        <c:otherwise>
            ${category.icon}
        </c:otherwise>
    </c:choose>

    <label>Replace Icon (file):</label>
    <input type="file" name="iconFile" accept="image/png,image/jpeg,image/webp,image/svg+xml" /><br/>
    <small>Allowed: PNG, JPG/JPEG, WEBP, SVG. Max size: 5 MB. If no file selected the current icon remains.</small>
    <br/>

    <label>Icon (text fallback):</label>
    <input type="text" name="icon" value="${category.icon}"/><br/>

    <button type="submit">Update</button>
    <a href="${pageContext.request.contextPath}/admin/category/list">Cancel</a>
    <a href="${pageContext.request.contextPath}/admin/home">⟵ Quay lại trang chủ</a>
</form>
</body>
</html>