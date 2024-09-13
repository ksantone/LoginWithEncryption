<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
	    <meta charset="UTF-8">
	    <title>Register</title>
	    <link rel="stylesheet" href="css/styles.css">
	</head>
	<body>
	    <div class="login-container">
	        <h2>Register</h2>
	        <form action="register" method="post">
	            <div class="form-group">
	                <label for="username">Username:</label>
	                <input type="text" name="username" required/>
	            </div>
	            
	            <div class="form-group">
	                <label for="password">Password:</label>
	                <input type="password" name="password" required/>
	            </div>
	            
	            <div class="form-group">
	                <label for="firstName">First Name:</label>
	                <input type="text" name="firstName" required/>
	            </div>
	            
	            <div class="form-group">
	                <label for="lastName">Last Name:</label>
	                <input type="text" name="lastName" required/>
	            </div>
	            
	            <input type="submit" class="btn" value="Register" />
	            
	            <!-- Error message display -->
	            <c:if test="${not empty errorMessage}">
	                <p style="color:red;">${errorMessage}</p>
	            </c:if>
	        </form>
	    </div>
	</body>
</html>