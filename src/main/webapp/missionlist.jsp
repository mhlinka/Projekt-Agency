<jsp:include page="header.jsp"/>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="row">
    <div class="col-md-1"></div>
    <div class="col-md-5">
        <form action="${pageContext.request.contextPath}/missions/search" method="POST">
            <input class="form-control" type="search" name="s">
            <input type="submit" class="btn-link" value="${web_SearchButton}">
        </form>
    </div>
</div>
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
                <th>${web_ActionsColumnTitle}</th>
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
                            <input type="submit" value="${web_UpdateButtonText}" class="btn-link"></form>
                        |
                        <form style="display:inline" method="post"
                              action="${pageContext.request.contextPath}/missions/delete?id=${mission.missionId}">
                            <input type="submit" value="${web_DeleteButtonText}" class="btn-link"></form>
                        |
                        <form style="display:inline" method="post"
                              action="${pageContext.request.contextPath}/missions/listspies?missionId=${mission.missionId}">
                            <input type="submit" value="${web_mission_ShowSpiesButtonText}" class="btn-link"></form>

                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</div>


<div class="row">
    <div class="col-md-1"></div>
    <div class="col-md-10">
        <form class="form-horizontal" action="${pageContext.request.contextPath}/missions/add" method="post">
    <fieldset>
        <legend>${web_mission_NewMissionFormTitle}</legend>
        <div class="form-group">
            <label for="startDate" class="col-md-1 control-label">${web_mission_StartDateColumnTitle}</label>

            <div class="col-md-5">
                <input type="date" class="form-control" id="startDate" name="startDate"
                       value="<c:out value='${param.startDate}'/>"/>
            </div>
        </div>
        <div class="form-group">
            <label for="endDate" class="col-md-1 control-label">${web_mission_EndDateColumnTitle}</label>

            <div class="col-md-5">
                <input type="date" class="form-control" id="endDate" name="endDate"
                       value="<c:out value='${param.endDate}'/>"/>
            </div>
        </div>
        <div class="form-group">
            <label for="select" class="col-md-1 control-label">${web_mission_TypeColumnTitle}</label>

            <div class="col-md-5" id="select">
                <select class="form-control" id="missionType" name="missionType">
                    <option>${ABDUCTION}</option>
                    <option>${ASSASSINATION}</option>
                    <option>${SURVEILLANCE}</option>
                    <option>${SABOTAGE}</option>
                    <option>${UNSPECIFIED}</option>
                </select>
            </div>
        </div>
        <div class="form-group">
            <div class="col-md-1">
                <button type="submit" class="btn btn-primary col-md-offset-1">${web_SubmitButtonText}</button>
            </div>
        </div>
    </fieldset>
</form>
</div></div>
</body>
</html>
