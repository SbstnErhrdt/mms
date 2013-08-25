package texparser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import model.content.Module;

public class TexParseController {

	
	public void parse(String path) throws IOException {
		
		System.out.println("...................................");
		System.out.println("Starting to parse modules at " + path);
		System.out.println("...................................");
		
		double startTime = System.currentTimeMillis();
		int numberOfModules = 0;
		
		File file = new File(path);
		
		if(file.isDirectory()) {
			File[] listOfFiles = file.listFiles();
			for(File f : listOfFiles) {
				if(f.isFile()) {
					parseTexFile(path + "/" + f.getName());
					numberOfModules += 1;
				}
			}
		} else {
			// single file
			parseTexFile(file.getName());
			numberOfModules = 1;
		}
		double endTime = System.currentTimeMillis();
		double parseTime = (endTime-startTime)/1000;
		
		System.out.println("...................................");
		System.out.println(numberOfModules+ " Modules parsed in "+parseTime+" seconds.");
		System.out.println("...................................");
	}
	
	private void parseTexFile(String filename) throws IOException {
		
		BufferedReader br = new BufferedReader(new FileReader(filename));
		
		String everything = "";
		
		try {
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			while (line != null) {
				sb.append(line);
				sb.append('\n');
				line = br.readLine();
			}
			everything = sb.toString();
			// DEBUG System.out.println(everything);
		} finally {
			br.close();
		}
		
		TexParser tp = new TexParser();
		
		ArrayList<TexNode> texNodes = tp.parseTexNodes(everything);
		
		// DEBGUG System.out.println(texNodes);
		
		Module module = tp.convertToModuleAndDumpInDatabase(texNodes);
		
		System.out.println(module);
		
		// System.out.println(module.getLearningTarget());
		// System.out.println(module.getLiterature());
	}
}
