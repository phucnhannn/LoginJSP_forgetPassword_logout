<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<html>
<head>
    <title>Category List</title>
</head>
<body>
<h2>Category List</h2>
<div style="margin-bottom:10px;">
    <a href="${pageContext.request.contextPath}/admin/home" class="btn">⟵ Quay lại trang chủ</a>
    <a href="${pageContext.request.contextPath}/admin/category/add">➕ Add New Category</a>
</div>
<table border="1" width="70%">
    <tr>
        <th>ID</th>
        <th>Name</th>
        <th>Icon</th>
        <th>Action</th>
    </tr>
    <c:forEach items="${cateList}" var="cate">
        <tr>
            <td>${cate.id}</td>
            <td>${cate.name}</td>
            <td>
                <%-- If icon looks like an uploaded path (starts with uploads/), show image --%>
                <c:choose>
                    <c:when test="${not empty cate.icon and fn:startsWith(cate.icon, 'uploads/')}">
                        <img src="${pageContext.request.contextPath}/${cate.icon}" alt="${cate.name}" style="max-width:100px; max-height:80px;" />
                    </c:when>
                    <c:when test="${not empty cate.icon and fn:startsWith(cate.icon, '/uploads/')}">
                        <img src="${pageContext.request.contextPath}${cate.icon}" alt="${cate.name}" style="max-width:100px; max-height:80px;" />
                    </c:when>
                    <c:otherwise>
                        ${cate.icon}
                    </c:otherwise>
                </c:choose>
            </td>
            <td>
                <a href="${pageContext.request.contextPath}/admin/category/edit?id=${cate.id}">Edit</a> |
                <a href="${pageContext.request.contextPath}/admin/category/delete?id=${cate.id}"
                   onclick="return confirm('Are you sure?')">Delete</a>
            </td>
        </tr>
    </c:forEach>
</table>
</body>
</html>