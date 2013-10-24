package texparser;

import java.io.IOException;
import java.util.ArrayList;

import model.content.*;

import com.google.gson.Gson;

import controller.ContentDbController;

public class Test {
	
	public static void main(String[] args) throws IOException {
		
		Gson gson = new Gson();
		
		System.out.println(gson.toJson(new TexParseController(null).parseFile("./texfiles/test")));	
		
		//Module module = new ContentDbController().getModule(1254);
		/*
		ArrayList<ModuleField> mfs = new ArrayList<ModuleField>();
		
		mfs.add(new ModuleField(-1, 1, "Falsch1"));
		mfs.add(new ModuleField(-1, 2, "Falsch2"));
		
		Module module = new Module(-1, "Test", false, null, null, mfs);
		
		System.out.println(gson.toJson(module));
		try {
			System.out.println(new TexParseController(null).parseModule(module).getName());
		} catch (IncompatibleModuleException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
	}
}
