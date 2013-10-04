package texparser;

import java.io.IOException;

import model.content.Module;

import com.google.gson.Gson;

import controller.ContentDbController;

public class Test {
	
	public static void main(String[] args) throws IOException {
		
		Gson gson = new Gson();
		
		System.out.println(gson.toJson(new TexParseController(null).parseFile("./texfiles/test")));	
		
		//Module module = new ContentDbController().getModule(1149);
		
		//System.out.println(new TexParseController(null).parseModule(module).getName());
		
	}
}
