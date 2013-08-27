package texparser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import model.content.Module;

public class TexParseController {

	private String modifier_email;
	
	public TexParseController(String modifier_email) {
		this.modifier_email = modifier_email;
	}
	
	public ArrayList<String> parse(String path) throws IOException {
		
		System.out.println("....................................................");
		System.out.println("[texparser] Starting to parse modules at " + path);
		System.out.println("....................................................");
		
		double startTime = System.currentTimeMillis();
		int numberOfModules = 0;
		
		ArrayList<String> names = new ArrayList<String>();
		
		File file = new File(path);
		
		if(file.isDirectory()) {
			ArrayList<File> listOfFiles = getAllFiles(file);
			System.out.println("[texparser] listOfFiles: " + listOfFiles);
			for(File f : listOfFiles) {
				String name = f.getName();
				if(name.substring(name.lastIndexOf('.')).equals(".tex")) {
					names.add(parseTexFile(f));
					numberOfModules += 1;
				} else {
					System.out.println("[texparser] ignoring non-tex file " + name);
				}
			}
		} else {
			// single file
			String name = file.getName();
			if(name.substring(name.lastIndexOf('.')).equals(".tex")) {
				names.add(parseTexFile(file));
				numberOfModules = 1;
			} 
		}
		double endTime = System.currentTimeMillis();
		double parseTime = (endTime-startTime)/1000;
		
		System.out.println("....................................................");
		System.out.println("[texparser] "+numberOfModules+" Modules parsed in "+parseTime+" seconds.");
		System.out.println("....................................................");
		
		return names;
	}
	
	/**
	 * @param file
	 * @return a list of all files in the directory and its subdirectories
	 */
	private ArrayList<File> getAllFiles(File file) {
		ArrayList<File> files = new ArrayList<File>();
		File[] listOfFiles = file.listFiles();
		for(File f : listOfFiles) {
			if(f.isFile()) {
				files.add(f);
			} else if(f.isDirectory()) {
				// recursively add all files in subdirectories
				files.addAll(getAllFiles(f));
			}
		}
		return files;
	}

	private String parseTexFile(File file) throws IOException {
		
		BufferedReader br = new BufferedReader(new FileReader(file));
		
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
		
		Module module = tp.convertToModuleAndDumpInDatabase(texNodes, modifier_email);
		
		System.out.println("[texparser] "+module);
		
		return module.getName();
	}
}
