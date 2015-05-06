<jsp:include page="header.jsp"/>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="row">
    <div class="col-md-1"></div>
    <div class="col-md-5">
        <form action="${pageContext.request.contextPath}/spies/search" method="POST">
            <input class="form-control" type="search" name="s">
            <input type="submit" class="btn-link" value="${web_SearchButton}">
        </form>
    </div>
</div>
<div class="row">
    <div class="col-md-1"></div>
    <div class="col-md-10">
        <c:if test="${not empty assignedSpies}">
            <h3>${web_spy_OnMissionTitle}</h3>
            <table class="table table-striped table-hover ">
                <thead>
                <tr>
                    <th>${web_IdColumnTitle}</th>
                    <th>${web_spy_FirstNameColumnTitle}</th>
                    <th>${web_spy_LastNameColumnTitle}</th>
                    <th>${web_spy_DateOfBirthColumnTitle}</th>
                    <th>${web_spy_CodenameColumnTitle}</th>
                    <th>${web_spy_ActionsColumnTitle}</th>
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
                                <input type="submit" value="${web_UpdateButtonText}" class="btn-link"></form>
                            |
                            <form style="display:inline" method="post"
                                  action="${pageContext.request.contextPath}/spies/delete?id=${spy.spyId}">
                                <input type="submit" value="${web_DeleteButtonText}" class="btn-link"></form>
                            |
                            <form style="display:inline" method="post"
                                  action="${pageContext.request.contextPath}/spies/removeFromMission?spyId=${spy.spyId}">
                                <input type="submit" value="${web_CancelMissionButtonText}" class="btn-link"></form>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </c:if>
        <c:if test="${not empty unassignedSpies}"><h3>${web_spy_UnassignedTitle}</h3>
            <table class="table table-striped table-hover ">
                <thead>
                <tr>
                    <th>${web_IdColumnTitle}</th>
                    <th>${web_spy_FirstNameColumnTitle}</th>
                    <th>${web_spy_LastNameColumnTitle}</th>
                    <th>${web_spy_DateOfBirthColumnTitle}</th>
                    <th>${web_spy_CodenameColumnTitle}</th>
                    <th>${web_spy_ActionsColumnTitle}</th>
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
                                <input type="submit" value="${web_UpdateButtonText}" class="btn-link"></form>
                            |
                            <form style="display:inline" method="post"
                                  action="${pageContext.request.contextPath}/spies/delete?id=${spy.spyId}">
                                <input type="submit" value="${web_DeleteButtonText}" class="btn-link"></form>
                            |
                            <form style="display:inline" method="post"
                                  action="${pageContext.request.contextPath}/spies/addToMission?spyId=${spy.spyId}">
                                <input type="submit" value="${web_AddToMissionButtonText}" class="btn-link"></form>
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
                <legend>${web_spy_NewText}</legend>
                <div class="form-group">
                    <label for="firstName" class="col-md-1 control-label">${web_spy_FirstNameColumnTitle}</label>

                    <div class="col-md-5">
                        <input type="text" class="form-control" id="firstName" name="firstName" value="<c:out value='${param.firstName}'/>"/>
                    </div>
                </div>
                <div class="form-group">
                    <label for="lastName" class="col-md-1 control-label">${web_spy_LastNameColumnTitle}</label>

                    <div class="col-md-5">
                        <input type="text" class="form-control" id="lastName" name="lastName" value="<c:out value='${param.lastName}'/>"/>
                    </div>
                </div>
                <div class="form-group">
                    <label for="codename" class="col-md-1 control-label">${web_spy_CodenameColumnTitle}</label>

                    <div class="col-md-5">
                        <input type="text" class="form-control" id="codename" name="codename" value="<c:out value='${param.codename}'/>"/>
                    </div>
                </div>
                <div class="form-group">
                    <label for="dateOfBirth" class="col-md-1 control-label">${web_spy_DateOfBirthColumnTitle}</label>

                    <div class="col-md-5">
                        <input type="date" class="form-control" id="dateOfBirth" name="dateOfBirth" value="<c:out value='${param.dateOfBirth}'/>"/>
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-md-1">
                        <button type="submit" class="btn btn-primary">${web_SubmitButtonText}</button>
                    </div>
                </div>
            </fieldset>
        </form>
    </div>
</div>
</body>
</html>
