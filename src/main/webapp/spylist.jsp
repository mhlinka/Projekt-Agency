<%--
  Created by IntelliJ IDEA.
  User: Michal
  Date: 4/23/2015
  Time: 13:44
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <link rel="stylesheet" href="style.css">
    <title>Spies</title>
</head>
<body>
<div id="site">
    <div id="left">
        <ul>
            <li><a href="index.jsp">Index</a></li>
            <li><a href="spylist.jsp">Spies</a></li>
            <li><a href="missionlist.jsp">Missions</a></li>
        </ul>
        <br/>
        <br/>
        <br/>
        <br/>
        <br/>
    </div>
    <div id="content">
        <table border="1">
            <thead>
            <tr>
                <th>Meno</th>
                <th>Priezvisko</th>
                <th>Dátum narodenia</th>
                <th>Alias</th>
            </tr>
            </thead>
            <c:forEach items="${spies}" var="spy">
                <tr>
                    <td><c:out value="${spy.firstName}"/></td>
                    <td><c:out value="${spy.lastName}"/></td>
                    <td><c:out value="${spy.dateOfBirth}"/></td>
                    <td><c:out value="${spy.codename}"/></td>
                    <td>
                        <form method="post" action="${pageContext.request.contextPath}/spies/delete?id=${spy.spyId}">
                            <input type="submit" value="Delete"></form>
                    </td>
                    <td>
                        <form method="post" action="${pageContext.request.contextPath}/spies/update?id=${spy.spyId}">
                            <input type="submit" value="Update"></form>
                    </td>
                </tr>
            </c:forEach>
        </table>

        <h2>New Spy</h2>
        <c:if test="${not empty error}">
            <div class="errorMsg">
                <c:out value="${error}"/>
            </div>
        </c:if>
        <form action="${pageContext.request.contextPath}/spies/add" method="post">
            <table>
                <tr>
                    <th>First Name:</th>
                    <td><input type="text" name="firstName" value="<c:out value='${param.firstName}'/>"/></td>
                </tr>
                <tr>
                    <th>Last Name:</th>
                    <td><input type="text" name="lastName" value="<c:out value='${param.lastName}'/>"/></td>
                </tr>
                <tr>
                    <th>Date of birth:</th>
                    <td><input type="date" name="dateOfBirth" value="<c:out value='${param.dateOfBirth}'/>"/></td>
                </tr>
                <tr>
                    <th>Code Name:</th>
                    <td><input type="text" name="codename" value="<c:out value='${param.codename}'/>"/></td>
                </tr>

            </table>
            <input type="Submit" value="Zadat" />
        </form>

    </div>
</div>
</body>
</html>
