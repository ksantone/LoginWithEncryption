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
		
		// Validate the input
		String validationError = ValidationUtil.validateLoginInput(username, password);
		if (validationError != null) {
			request.setAttribute("errorMessage", validationError);
			request.getRequestDispatcher("login.jsp").forward(request, response);
			return;
		}
		
		// Check if the user is rate-limited
	    if (rateLimiter.isBlocked(username, "login")) {
	        // Set the error message
	        String errorMessage = rateLimiter.getBlockTimeMessage(username, "login");
	        request.setAttribute("errorMessage", errorMessage);

	        // Forward the request to the login page with the error message
	        request.getRequestDispatcher("login.jsp").forward(request, response);
	        return;
	    }

		// Validate user against the database
		User user = DatabaseValidator.getUserByUsername(username);

		if (user != null && PasswordUtil.checkPassword(password, user.getPassword())) {
			// Successful login, reset attempts
	        rateLimiter.loginSucceeded(username, "login");
			
			// Retrieve additional data from the database
			String firstName = user.getFirstName();
			String lastName = user.getLastName();

			// Create session and set the user as logged in
			HttpSession session = request.getSession();
			session.setAttribute("username", username);
			session.setAttribute("firstName", firstName);
			session.setAttribute("lastName", lastName);

			// Set user data in the request scope and forward to the welcome page
			request.setAttribute("firstName", firstName);
			request.setAttribute("lastName", lastName);
			request.getRequestDispatcher("welcome.jsp").forward(request, response);
		} else {
			// Failed login, increase attempts
	        rateLimiter.loginFailed(username, "login");
			
			// Set an error message in the request scope and forward back to the login page
			request.setAttribute("errorMessage", "Invalid username or password");
			request.getRequestDispatcher("login.jsp").forward(request, response);
		}
	}
}