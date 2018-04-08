<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Учет рабочего времени</title>
</head>
<body>
    <form action="${pageContext.servletContext.contextPath}/start" method='post'>
        <input type='submit' value='start work'>
    </form>

    <form action="${pageContext.servletContext.contextPath}/pause" method='post'>
        <input type='submit' value='pause'>
    </form>

    <form action="${pageContext.servletContext.contextPath}/finish" method='post'>
        <input type='submit' value='finish work'>
    </form>

    <form action="${pageContext.servletContext.contextPath}/signout" method='post'>
        <input type="submit" value="sign out"><br/>
    </form>
</body>
</html>
