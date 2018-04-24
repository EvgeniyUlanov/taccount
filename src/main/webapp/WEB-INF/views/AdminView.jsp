<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Admin page</title>
</head>
<body>

<form action="${pageContext.servletContext.contextPath}/add" method='post'>
    <label>User name: <input name="name"></label>
    <label>User password: <input name="password"></label>
    <br/>
    <input type="submit" value="add new user">
</form>

<form action="${pageContext.servletContext.contextPath}/signout" method='post'>
    <input type="submit" value="sign out"><br/>
</form>

<table style="border: 1px solid black;" cellpadding="1" cellspacing="1" border="1">
    <tr>
        <th>name</th>
        <th>password</th>
        <th>time on work</th>
        <th>status</th>
        <th>start time</th>
        <th>finish time</th>
    </tr>
    <c:forEach var="user" items="${users}">
        <tr>
            <td><c:out value="${user.name}"/></td>
            <td><c:out value="${user.password}"/></td>
            <td><c:out value="${user.workTimeString}"/></td>
            <td><c:out value="${user.status}"/></td>
            <td><c:out value="${user.startDate}"/></td>
            <td><c:out value="${user.finishDate}"/></td>
        </tr>
    </c:forEach>
</table>

</body>
</html>
