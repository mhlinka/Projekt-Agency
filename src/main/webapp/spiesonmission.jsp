<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="header.jsp"/>

<div class="row">
    <div class="col-md-1"></div>
    <div class="col-md-10">
        <c:if test="${not empty spies}">
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
            <c:forEach items="${spies}" var="spy">
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
        <c:if test="${empty spies}">
            <h3>${web_spiesOnMissionEmpty}</h3>
        </c:if>
</div>
    </div>

        </body>
    </div>
