package com.LoginWebApp;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/home")
public class LoginServlet extends HttpServlet {
	
	RateLimiter rateLimiter = new RateLimiter();

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.getRequestDispatcher("welcome.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {		
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		
		// Check if the user is rate-limited
	    if (rateLimiter.isBlocked(username)) {
	        // Set the error message
	        String errorMessage = rateLimiter.getBlockTimeMessage(username);
	        request.setAttribute("errorMessage", errorMessage);

	        // Forward the request to the login page with the error message
	        request.getRequestDispatcher("login.jsp").forward(request, response);
	        return;
	    }

		// Validate user against the database
		User user = DatabaseValidator.getUserByUsername(username);

		if (user != null && PasswordUtil.checkPassword(password, user.getPassword())) {
			// Successful login, reset attempts
	        rateLimiter.loginSucceeded(username);
			
			// Retrieve additional data from the database
			String fullName = user.getFullName();

			// Create session and set the user as logged in
			HttpSession session = request.getSession();
			session.setAttribute("username", username);
			session.setAttribute("fullName", fullName);

			// Set user data in the request scope and forward to the welcome page
			request.setAttribute("fullName", fullName);
			request.getRequestDispatcher("welcome.jsp").forward(request, response);
		} else {
			// Failed login, increase attempts
	        rateLimiter.loginFailed(username);
			
			// Set an error message in the request scope and forward back to the login page
			request.setAttribute("errorMessage", "Invalid username or password");
			request.getRequestDispatcher("login.jsp").forward(request, response);
		}
	}
}