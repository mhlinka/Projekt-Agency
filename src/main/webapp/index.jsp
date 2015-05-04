<%@ page import="cz.muni.fi.pv168.web.Utility" %>
<%@ page import="java.util.Locale" %>
<%@ page import="java.util.ResourceBundle" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:include page="header.jsp" />
<%
    ResourceBundle bundle = ResourceBundle.getBundle("Strings",Locale.getDefault());
%>
<div class="jumbotron">
	<div class="container">
		<h1><% out.println(bundle.getString("web_index_Welcome")); %></h1>
		<p><% out.println(bundle.getString("web_index_SiteDescription"));%></p>
	</div>
</div>

</body>
</html>