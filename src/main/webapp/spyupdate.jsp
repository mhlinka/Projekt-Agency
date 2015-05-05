<jsp:include page="header.jsp"/>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:if test="${not empty error}">
    <div class="alert-danger">
        <c:out value="${error}"/>
    </div>
    <br/>
</c:if>

<div class="row">
    <div class="col-md-1"></div>
    <div class="col-md-10">
        <form  class="form-horizontal" action="${pageContext.request.contextPath}/spies/update?id=${spyId}&doUpdate=true" method="post">
            <fieldset>
                <legend>${web_spy_UpdateTableTitle}</legend>
                <c:if test="${not empty error}">
                    <div class="alert-danger">
                        <c:out value="${error}"/>
                    </div>
                    <br/>
                </c:if>
                <div class="form-group">
                    <label for="firstName" class="col-md-1 control-label">${web_spy_FirstNameColumnTitle}</label>

                    <div class="col-md-5">
                        <input type="text" class="form-control" id="firstName" name="firstName" value="<c:out value='${firstName}'/>"/>
                    </div>
                </div>
                <div class="form-group">
                    <label for="lastName" class="col-md-1 control-label">${web_spy_LastNameColumnTitle}</label>

                    <div class="col-md-5">
                        <input type="text" class="form-control" id="lastName" name="lastName" value="<c:out value='${lastName}'/>"/>
                    </div>
                </div>
                <div class="form-group">
                    <label for="codename" class="col-md-1 control-label">${web_spy_CodenameColumnTitle}</label>
                    <div class="col-md-5">
                        <input type="text" class="form-control" id="codename" name="codename" value="<c:out value='${codename}'/>"/>
                    </div>
                </div>
                <div class="form-group">
                    <label for="dateOfBirth" class="col-md-1 control-label">${web_spy_DateOfBirthColumnTitle}</label>
                    <div class="col-md-5">
                        <input type="date" class="form-control" id="dateOfBirth" placeholder="Date" name="dateOfBirth" value="<c:out value='${dateOfBirth}'/>"/>
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
