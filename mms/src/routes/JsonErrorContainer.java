package routes;

public class JsonErrorContainer {
	private JsonError error;
	
	/**
	 * constructor
	 * @param error
	 */
	public JsonErrorContainer(JsonError error) {
		this.error = error;
	}

	public JsonError getError() {
		return error;
	}

	public void setError(JsonError error) {
		this.error = error;
	}
}
