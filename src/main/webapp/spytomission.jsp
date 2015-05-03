<jsp:include page="header.jsp"/>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<form class="form-horizontal" action="${pageContext.request.contextPath}/spies/addToMission?spyId=${spyId}&addingSpyToMission=true" method="post">
  <input type="text" value="" placeholder="enter mission id here" name="missionId" id="missionId"/>
  <input type="submit" value="Add"/>
</form>

<c:if test="${not empty missions}">
    <div class="row">
    <div class="col-md-1"></div>
    <div class="col-md-10">
        <h3>Listing</h3>
        <table class="table table-striped table-hover ">
            <thead>
            <tr>
                <th>ID</th>
                <th>Type</th>
                <th>Start Date</th>
                <th>End Date</th>
                <th>Actions</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${missions}" var="mission">
                <tr>
                    <td><c:out value="${mission.missionId}"/></td>
                    <td>
                        <c:out value="${mission.type.toString().substring(0, 1).toUpperCase()}"/><c:out value="${mission.type.toString().substring(1).toLowerCase()}"/>
                    </td>
                    <td><c:out value="${mission.startDate}"/></td>
                    <td><c:out value="${mission.endDate}"/></td>
                    <td>
                        <form style="display:inline" method="post"
                              action="${pageContext.request.contextPath}/missions/update?id=${mission.missionId}">
                            <input type="submit" value="Update" class="btn-link"></form>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</div></c:if>

</body>
</html>
