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
        <h2>Update spy</h2>
        <c:if test="${not empty error}">
            <div class="errorMsg">
                <c:out value="${error}"/>
            </div>
        </c:if>
        <form action="${pageContext.request.contextPath}/spies/update?id=${spyId}&doUpdate=true" method="post">
            <table>
                <tr>
                    <th>First Name:</th>
                    <td><input type="text" name="firstName" value="<c:out value='${firstName}'/>"/></td>
                </tr>
                <tr>
                    <th>Last Name:</th>
                    <td><input type="text" name="lastName" value="<c:out value='${lastName}'/>"/></td>
                </tr>
                <tr>
                    <th>Date of birth:</th>
                    <td><input type="date" name="dateOfBirth" value="<c:out value='${dateOfBirth}'/>"/></td>
                </tr>
                <tr>
                    <th>Code Name:</th>
                    <td><input type="text" name="codename" value="<c:out value='${codename}'/>"/></td>
                </tr>
            </table>
            <input type="Submit" value="Update" />
        </form>

    </div>
</div>
</body>
</html>
