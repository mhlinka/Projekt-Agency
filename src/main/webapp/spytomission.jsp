<jsp:include page="header.jsp"/>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<div class="row">
    <div class="col-md-1"></div>
    <div class="col-md-6"><form class="form-horizontal" action="${pageContext.request.contextPath}/spies/addToMission?spyId=${spyId}&addingSpyToMission=true" method="post">
  <input class="form-control" type="text" value="" placeholder=${web_spy_MissionIdPlaceholder} name="missionId" id="missionId"/>
  <input class="btn btn-primary" type="submit" value=${web_AddButtonText}  style="margin-top:10px;"/>
</form>
</div></div>
<c:if test="${not empty missions}">
    <div class="row">
    <div class="col-md-1"></div>
    <div class="col-md-10">
        <h3>${web_MissionsTableTitle}</h3>
        <table class="table table-striped table-hover ">
            <thead>
            <tr>
                <th>${web_IdColumnTitle}</th>
                <th>${web_mission_TypeColumnTitle}</th>
                <th>${web_mission_StartDateColumnTitle}</th>
                <th>${web_mission_EndDateColumnTitle}</th>
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
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</div></c:if>

</body>
</html>
