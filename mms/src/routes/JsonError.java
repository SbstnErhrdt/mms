package routes;

public class JsonError {
	private String message, method;
	
	/**
	 * constructor
	 * @param message
	 * @param method
	 */
	public JsonError(String message, String method) {
		this.message = message;
		this.method = method;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}	
}
