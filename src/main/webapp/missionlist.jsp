<jsp:include page="header.jsp"/>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="row">
    <div class="col-md-1"></div>
    <div class="col-md-10">
        <h3>Listing</h3>
        <table class="table table-striped table-hover ">
            <thead>
            <tr>
                <th>#</th>
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
                        |
                        <form style="display:inline" method="post"
                              action="${pageContext.request.contextPath}/missions/delete?id=${mission.missionId}">
                            <input type="submit" value="Delete" class="btn-link"></form>
                        |
                        <form style="display:inline" method="post"
                              action="${pageContext.request.contextPath}/missions/listspies?missionId=${mission.missionId}">
                            <input type="submit" value="Show spies" class="btn-link"></form>

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
        <legend>New Mission</legend>
        <div class="form-group">
            <label for="startDate" class="col-md-1 control-label">Start date</label>

            <div class="col-md-5">
                <input type="date" class="form-control" id="startDate" placeholder="Date" name="startDate"
                       value="<c:out value='${param.startDate}'/>"/>
            </div>
        </div>
        <div class="form-group">
            <label for="endDate" class="col-md-1 control-label">End date</label>

            <div class="col-md-5">
                <input type="date" class="form-control" id="endDate" placeholder="Date" name="endDate"
                       value="<c:out value='${param.endDate}'/>"/>
            </div>
        </div>
        <div class="form-group">
            <label for="select" class="col-md-1 control-label">Type</label>

            <div class="col-md-5" id="select">
                <select class="form-control" id="missionType" name="missionType">
                    <option>Assassination</option>
                    <option>Abduction</option>
                    <option>Surveillance</option>
                    <option>Sabotage</option>
                    <option>Unspecified</option>
                </select>
            </div>
        </div>
        <div class="form-group">
            <div class="col-md-1">
                <button type="submit" class="btn btn-primary col-md-offset-1">Submit</button>
            </div>
        </div>
    </fieldset>
</form>
</div></div>
</body>
</html>
