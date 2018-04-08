<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Sign in page</title>
</head>
<body>
<form action="${pageContext.servletContext.contextPath}/sign" method="post">
    Login : <input name="login"/>
    Password : <input type="password" name="password"/>
    <input type="submit" value="Sign in">
</form>
</body>
</html>
