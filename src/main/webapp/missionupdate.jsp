<%--
  Created by IntelliJ IDEA.
  User: FH
  Date: 21.4.2015
  Time: 12:34
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
  <link rel="stylesheet" href="style.css">
  <title>Missions</title>
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
    <h2>Update mission</h2>
    <c:if test="${not empty error}">
      <div class="errorMsg">
        <c:out value="${error}"/>
      </div>
    </c:if>
    <form action="${pageContext.request.contextPath}/missions/update?id=${missionId}&doUpdate=true" method="post">
      <table>
        <tr>
          <th>Starts:</th>
          <td><input type="date" name="startDate" value="<c:out value='${startDate}'/>"/></td>
        </tr>
        <tr>
          <th>Ends:</th>
          <td><input type="date" name="endDate" value="<c:out value='${endDate}'/>"/></td>
        </tr>
        <tr>
          <th>Type:</th>
          <datalist id="missionTypes">
            <option value="Assassination"></option>
            <option value="Abduction"></option>
            <option value="Surveillance"></option>
            <option value="Sabotage"></option>
            <option value="Unspecified"></option>
          </datalist>
          <td>
            <input list="missionTypes" name="missionType" value="<c:out value='${missionType}'/>"/>
          </td>
        </tr>
      </table>
      <input type="Submit" value="Update" />
    </form>

  </div>
</div>
</body>
</html>
