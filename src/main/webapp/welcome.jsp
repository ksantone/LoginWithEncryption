<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
    if (session.getAttribute("username") == null) {
        System.out.println("Redirecting from welcome.jsp to login.jsp because session username is not set.");
        response.sendRedirect("login.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html lang="en">
	<head>
	    <meta charset="UTF-8">
	    <title>Welcome</title>
	    
	    <!-- Link to the external CSS file -->
	    <link rel="stylesheet" href="css/styles.css">
	</head>
	<body>
		<%@ include file="navbar.jsp" %>
	    
	    <div class="welcome-message">Welcome, ${firstName} ${lastName}!</div>
	</body>
</html>