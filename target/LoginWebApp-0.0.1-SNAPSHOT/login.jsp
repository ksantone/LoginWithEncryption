<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
    if (session.getAttribute("username") != null) {
        System.out.println("Redirecting from login.jsp to welcome.jsp because session username is set.");
        response.sendRedirect("welcome.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html>
	<head>
	    <meta charset="UTF-8">
	    <title>Login to Web App</title>
	    
	    <!-- Link to the external CSS file -->
	    <link rel="stylesheet" href="css/styles.css">
	</head>
	<body>
	    <div class="login-container">
	        <h2>Login to Web App</h2>
	        
	        <!-- Check if there's an error message -->
	        <c:if test="${not empty errorMessage}">
	            <div class="error-message" style="color: red;">
	                ${errorMessage}
	            </div>
	        </c:if>
	        
	        <form action="home" method="post">
	            <div class="form-group">
	                <label for="username">Username</label>
	                <input type="text" name="username" id="username" />
	            </div>
	            <div class="form-group">
	                <label for="password">Password</label>
	                <input type="password" name="password" id="password" />
	            </div>
	            <input type="submit" class="btn" value="Login" />
	        </form>
	        
	        <div class="register-button">
	            <input type="button" class="btn" value="Register" onclick="window.location.href='register.jsp';" />
	        </div>
	    </div>
	</body>
</html>