<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/bootstrap.css"/>
</head>
<title>
  Welcome to the agency!
</title>
<body>
<nav class="navbar navbar-default">
  <div class="container-fluid">
    <div class="navbar-header">
      <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
        <span class="sr-only">Toggle navigation</span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
      </button>
      <a class="navbar-brand" href="#" data-ytta-id="-">ISS</a>
    </div>

    <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
      <ul class="nav navbar-nav">
        <li><a href="${pageContext.request.contextPath}/index.jsp" data-ytta-id="-">Index<span class="sr-only">(current)</span></a></li>
        <li><a href="/spies/" data-ytta-id="-">Spies</a></li>
        <li><a href="/missions/" data-ytta-id="-">Missions</a></li>
      </ul>
    </div>
  </div>
</nav>
UTF-8 BROKEN STATUS: äľščťžýáíéздравствуйте
<div class="row">
    <div class="col-md-1"></div>
<div class="col-md-10"><c:if test="${not empty error}">
  <div class="alert alert-dismissable alert-danger">
    <c:out value="${error}"/>
  </div>
  <br/>
</c:if>
<c:if test="${not empty success}">
  <div class="alert alert-dismissable alert-success">
    <c:out value="${success}"/>
  </div>
  <br/>
</c:if></div>
</div>