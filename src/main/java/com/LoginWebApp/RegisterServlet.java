package com.LoginWebApp;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		
		// Validate user input
		String validationError = ValidationUtil.validateUserDetails(username, firstName, lastName, password);
		if (validationError != null) {
			request.setAttribute("errorMessage", validationError);
			request.getRequestDispatcher("register.jsp").forward(request, response);
			return;
		}

		// Check if the username is already taken
		User existingUser = DatabaseValidator.getUserByUsername(username);
		if (existingUser != null) {
			request.setAttribute("errorMessage", "Username is already taken.");
			request.getRequestDispatcher("register.jsp").forward(request, response);
			return;
		}

		// Hash the password using BCrypt
		String hashedPassword = PasswordUtil.hashPassword(password);

		// Add the new user to the database
		boolean isRegistered = DatabaseValidator.addNewUser(username, hashedPassword, firstName, lastName);
		if (isRegistered) {
			response.sendRedirect("login.jsp");
		} else {
			request.setAttribute("errorMessage", "Failed to register user.");
			request.getRequestDispatcher("register.jsp").forward(request, response);
		}
	}
}