package texparser;

import java.io.IOException;

import model.content.Module;

import com.google.gson.Gson;

import controller.ContentDbController;

public class Test {
	
	public static void main(String[] args) throws IOException {
		
		Gson gson = new Gson();
		
		//System.out.println(gson.toJson(new TexParseController(null).parseFile("./texfiles")));	
		
		Module module = new ContentDbController().getModule(628);
		
		new TexParseController(null).parseModule(module);
		
	}
}
