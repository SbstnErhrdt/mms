package routes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import controller.UserDbController;

import model.User;

public abstract class Routes {
	
	private UserDbController db;
	
	/**
	 * constructor
	 */
	public Routes() {
		db = new UserDbController();
	}
	
	/**
	 * @param request
	 * @return the body-payload of the passed HttpServletRequest
	 */
	protected String getRequestBody(HttpServletRequest request) {
		StringBuilder stringBuilder = new StringBuilder();
		BufferedReader bufferedReader = null;
		try {
			InputStream inputStream = request.getInputStream();
			if (inputStream != null) {
				bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
				char[] charBuffer = new char[128];
				int bytesRead = -1;
				while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
					stringBuilder.append(charBuffer, 0, bytesRead);
				}
			} else {
				stringBuilder.append("");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return stringBuilder.toString();
	}
	
	/**
	 * @param request
	 * @return the email that belongs to the current session
	 */
	protected User getActorUser(HttpServletRequest request) {
		String actorEmail = (String) request.getSession().getAttribute("email");
		User actorUser = db.getUser(new User(actorEmail));
		System.out.println("actor: "+actorUser);
		return actorUser;
	}
	
	/**
	 * writes the passed string into the response
	 * @param response
	 * @param content
	 */
	protected void respond(HttpServletResponse response, String content) {
		try {
			response.getWriter().write(content);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
