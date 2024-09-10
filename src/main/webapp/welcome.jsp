<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String fullName = (String) session.getAttribute("fullName");
	if (fullName == null) {
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
		<div class="navbar">
	        <a href="account-settings">Account Settings</a>
	        <a href="logout">Logout</a>
	    </div>
	    
	    <div class="welcome-message">Welcome, ${fullName}!</div>
	</body>
</html>