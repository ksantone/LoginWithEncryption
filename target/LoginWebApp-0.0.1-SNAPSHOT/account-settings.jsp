<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
    // Check if the user is logged in
    if (session.getAttribute("username") == null) {
    	System.out.println("Username appears to be null in account settings JSP.");
        response.sendRedirect("login.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>Account Settings</title>
        <link rel="stylesheet" href="css/styles.css">
    </head>
    <body>
	    <%@ include file="navbar.jsp" %>
	
	    <div class="account-settings-container">
	        <h2>Account Settings</h2>
	        
	        <!-- Form for account settings -->
	        <form action="updateAccount" method="post">
	            
	            <div class="form-group">
	                <label for="firstName">First Name:</label>
	                <input type="text" name="firstName" id="firstName" value="${firstName}" required/>
	            </div>
	            
	            <div class="form-group">
	                <label for="lastName">Last Name:</label>
	                <input type="text" name="lastName" id="lastName" value="${lastName}" required/>
	            </div>
	            
	            <input type="submit" value="Save Changes" class="btn-primary"/>
	        </form>
	        
	        <!-- Success message display -->
			<c:if test="${not empty successMessage}">
			    <p class="success-message">${successMessage}</p>
			</c:if>
	        
	        <!-- Error message display -->
	        <c:if test="${not empty errorMessage}">
	            <p class="error-message">${errorMessage}</p>
	        </c:if>
	    </div>
    </body>
</html>