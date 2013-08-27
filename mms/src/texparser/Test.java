package texparser;

import java.io.IOException;

import com.google.gson.Gson;

public class Test {
	
	public static void main(String[] args) throws IOException {
		
		Gson gson = new Gson();
		
		System.out.println(gson.toJson(new TexParseController(null).parse("./texfiles")));		
	}
}
