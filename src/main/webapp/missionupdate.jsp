<jsp:include page="header.jsp"/>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="row">
  <div class="col-md-1"></div>
  <div class="col-md-10">
    <form class="form-horizontal" action="${pageContext.request.contextPath}/missions/update?id=${missionId}&doUpdate=true" method="post">
      <fieldset>
        <legend>New Mission</legend>
        <div class="form-group">
          <label for="startDate" class="col-md-1 control-label">${web_mission_StartDateColumnTitle}</label>

          <div class="col-md-5">
            <input type="date" class="form-control" id="startDate" name="startDate"
                   value="<c:out value='${startDate}'/>"/>
          </div>
        </div>
        <div class="form-group">
          <label for="endDate" class="col-md-1 control-label">${web_mission_EndDateColumnTitle}</label>

          <div class="col-md-5">
            <input type="date" class="form-control" id="endDate" name="endDate"
                   value="<c:out value='${endDate}'/>"/>
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
        <%--  <div class="form-group">
          <label for="select" class="col-md-1 control-label">${web_mission_TypeColumnTitle}</label>
            <input type=""
          <div class="col-md-5" id="select">
              <datalist id="missionTypes">
                <option value="Assassination"></option>
                <option value="Abduction"></option>
                <option value="Surveillance"></option>
                <option value="Sabotage"></option>
                <option value="Unspecified"></option>
              </datalist>
              <td>
                <input list="missionTypes" name="missionType" value="<c:out value='${missionType.toLowerCase()}'/>"/>
              </td>
          </div>
        </div>--%>
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
