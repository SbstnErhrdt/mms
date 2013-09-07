package routes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
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
	
	/**
	 * writes the passed file into the response
	 * @param response
	 * @param file
	 * @throws IOException
	 */
	protected void respond(HttpServletResponse response, HttpServlet servlet, File file) throws IOException {
		// set headers
		String mimeType = servlet.getServletContext().getMimeType(file.getAbsolutePath());
		response.setContentType(mimeType != null? mimeType:"application/octet-stream");
		response.setHeader("Content-Disposition", "attachment;filename="
				+ file.getName());
		response.setContentLength((int) file.length());
		
		// get file stream
		FileInputStream fileIn = new FileInputStream(file);
		ServletOutputStream out = response.getOutputStream();
		byte[] outputByte = new byte[4096];
		// copy binary contect to output stream
		while (fileIn.read(outputByte, 0, 4096) != -1) {
			out.write(outputByte, 0, 4096);
		}
		fileIn.close();
		out.flush();
		out.close();
	}	
}
