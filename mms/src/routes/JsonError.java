package routes;

public class JsonError {
	private String message, method;
	
	public JsonError(String message, String method) {
		this.message = message;
		this.method = method;
	}
}
