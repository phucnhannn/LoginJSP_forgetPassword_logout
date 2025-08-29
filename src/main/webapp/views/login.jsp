<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Đăng nhập</title>
</head>
<body>

    <h2>Đăng nhập</h2>

    <form action="login" method="post">
        <p>
            <label>Tài khoản:</label>
            <input type="text" name="username" required />
        </p>
        <p>
            <label>Mật khẩu:</label>
            <input type="password" name="password" required />
        </p>
        <p>
            <input type="submit" value="Đăng nhập" />
        </p>
    </form>

    <p>
        Bạn chưa có tài khoản? <a href="register.jsp">Hãy đăng ký</a>
    </p>

</body>
</html>
