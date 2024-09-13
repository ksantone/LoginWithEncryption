package com.LoginWebApp;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/updateAccount")
public class AccountSettingsServlet extends HttpServlet {
	
	RateLimiter rateLimiter = new RateLimiter();
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	System.out.println("In do get of account settings servlet.");
        // Get the current session
        HttpSession session = request.getSession(false);

        // Redirect to login if the user is not logged in
        if (session == null || session.getAttribute("username") == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        
        // Fetch user details to display (assuming DatabaseValidator has getUserDetailsByUsername)
        String username = (String) session.getAttribute("username");
        User user = DatabaseValidator.getUserByUsername(username);
        
        // Set user details in the request scope to display in the JSP
        request.setAttribute("user", user);
        
        // Forward to account-settings.jsp
        request.getRequestDispatcher("account-settings.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	System.out.println("In doPost method of account settings page.");
    	HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");
        String password = (String) session.getAttribute("password");
        
        // Check if the user is rate-limited for updates
        if (rateLimiter.isBlocked(username, "account_update")) {
            String errorMessage = rateLimiter.getBlockTimeMessage(username, "account_update");
            request.setAttribute("errorMessage", errorMessage);
            request.getRequestDispatcher("account-settings.jsp").forward(request, response);
            return;
        }
        
        // Retrieve updated details from the form
        String newFirstName = request.getParameter("firstName");
        String newLastName = request.getParameter("lastName");
        
        // Use ValidationUtil to validate user details
        String validationError = ValidationUtil.validateUserDetails(username, newFirstName, newLastName, password);
        if (validationError != null) {
            request.setAttribute("errorMessage", validationError);
            request.getRequestDispatcher("account-settings.jsp").forward(request, response);
            return;
        }
        
        // Assume updateUserDetails updates the user in the database
        boolean updateSuccess = DatabaseValidator.updateUserDetails(username, newFirstName, newLastName);
        System.out.println("Update success: " + updateSuccess);
        
        if (updateSuccess) {
            // Success, reset the rate limiter
            rateLimiter.loginSucceeded(username, "account_update");
            
            // Update the session with the new values
            session.setAttribute("firstName", newFirstName);
            session.setAttribute("lastName", newLastName);
            
            request.setAttribute("successMessage", "Account updated successfully!");
        } else {
            // Failed attempt, apply rate limiting
            rateLimiter.loginFailed(username, "account_update");
            request.setAttribute("errorMessage", "Failed to update account. Please try again.");
        }
        
        System.out.println("About to dispatch to account settings JSP.");
        
        request.getRequestDispatcher("account-settings.jsp").forward(request, response);
    }
}
