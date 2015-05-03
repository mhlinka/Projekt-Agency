<jsp:include page="header.jsp"/>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="row">
    <div class="col-md-1"></div>
    <div class="col-md-10">
        <c:if test="${not empty assignedSpies}">
            <h3>Assigned</h3>
            <table class="table table-striped table-hover ">
                <thead>
                <tr>
                    <th>#</th>
                    <th>First Name</th>
                    <th>Last Name</th>
                    <th>Date of birth</th>
                    <th>Codename</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${assignedSpies}" var="spy">
                    <tr>
                        <td><c:out value="${spy.spyId}"/></td>
                        <td>
                            <c:out value="${spy.firstName}"/>
                        </td>
                        <td><c:out value="${spy.lastName}"/></td>
                        <td><c:out value="${spy.dateOfBirth}"/></td>
                        <td><c:out value="${spy.codename}"/></td>
                        <td>
                            <form style="display:inline" method="post"
                                  action="${pageContext.request.contextPath}/spies/update?id=${spy.spyId}">
                                <input type="submit" value="Update" class="btn-link"></form>
                            |
                            <form style="display:inline" method="post"
                                  action="${pageContext.request.contextPath}/spies/delete?id=${spy.spyId}">
                                <input type="submit" value="Delete" class="btn-link"></form>
                            |
                            <form style="display:inline" method="post"
                                  action="${pageContext.request.contextPath}/spies/removeFromMission?spyId=${spy.spyId}">
                                <input type="submit" value="Cancel mission" class="btn-link"></form>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </c:if>
        <c:if test="${not empty unassignedSpies}"><h3>Chillin' at base</h3>
            <table class="table table-striped table-hover ">
                <thead>
                <tr>
                    <th>#</th>
                    <th>First Name</th>
                    <th>Last Name</th>
                    <th>Date of birth</th>
                    <th>Codename</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${unassignedSpies}" var="spy">
                    <tr>
                        <td><c:out value="${spy.spyId}"/></td>
                        <td>
                            <c:out value="${spy.firstName}"/>
                        </td>
                        <td><c:out value="${spy.lastName}"/></td>
                        <td><c:out value="${spy.dateOfBirth}"/></td>
                        <td><c:out value="${spy.codename}"/></td>
                        <td>
                            <form style="display:inline" method="post"
                                  action="${pageContext.request.contextPath}/spies/update?id=${spy.spyId}">
                                <input type="submit" value="Update" class="btn-link"></form>
                            |
                            <form style="display:inline" method="post"
                                  action="${pageContext.request.contextPath}/spies/delete?id=${spy.spyId}">
                                <input type="submit" value="Delete" class="btn-link"></form>
                            |
                            <form style="display:inline" method="post"
                                  action="${pageContext.request.contextPath}/spies/addToMission?spyId=${spy.spyId}">
                                <input type="submit" value="Add to mission" class="btn-link"></form>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </c:if>
    </div>
</div>


<div class="row">
    <div class="col-md-1"></div>
    <div class="col-md-10">
        <form class="form-horizontal" action="${pageContext.request.contextPath}/spies/add" method="post">
            <fieldset>
                <legend>New recruit</legend>
                <div class="form-group">
                    <label for="firstName" class="col-md-1 control-label">First Name</label>

                    <div class="col-md-5">
                        <input type="text" class="form-control" id="firstName" name="firstName">
                    </div>
                </div>
                <div class="form-group">
                    <label for="lastName" class="col-md-1 control-label">Last Name</label>

                    <div class="col-md-5">
                        <input type="text" class="form-control" id="lastName" name="lastName">
                    </div>
                </div>
                <div class="form-group">
                    <label for="codename" class="col-md-1 control-label">Codename</label>

                    <div class="col-md-5">
                        <input type="text" class="form-control" id="codename" name="codename">
                    </div>
                </div>
                <div class="form-group">
                    <label for="dateOfBirth" class="col-md-1 control-label">Date of Birth</label>

                    <div class="col-md-5">
                        <input type="date" class="form-control" id="dateOfBirth" placeholder="Date" name="dateOfBirth">
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-md-1">
                        <button type="submit" class="btn btn-primary">Submit</button>
                    </div>
                </div>
            </fieldset>
        </form>
    </div>
</div>
</body>
</html>
