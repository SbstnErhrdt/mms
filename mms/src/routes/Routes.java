package routes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public abstract class Routes {
	public Routes() {
		
	}
	
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
	
	protected String getCookieEmail(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		
		String email = "";
		
		if(cookies != null) {
			for(Cookie c : cookies) {
				if(c.getName().equals("email")){
					email = c.getValue();
				}
			}
		} else {
			System.out.println("request.getCookies() == null");
		}
		
		return email;
		
	}
}
