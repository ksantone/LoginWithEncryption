<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	if (session.getAttribute("username") != null) {
	    // Redirect to welcome page if user is already logged in
	    response.sendRedirect("welcome.jsp");
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