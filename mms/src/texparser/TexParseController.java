package texparser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import controller.GlobalVarDbController;

import model.content.Module;
import model.content.ModuleField;

public class TexParseController {

	private String modifier_email;
	private String exportPath;

	public TexParseController(String modifier_email) {
		this.modifier_email = modifier_email;
		// get export path
		GlobalVarDbController db = new GlobalVarDbController();
		exportPath = db.getGlobalVar("fileExportPath");
		db.closeConnection();
	}

	public ArrayList<Module> parseFiles(ArrayList<File> files)
			throws IOException {

		System.out
				.println("....................................................");
		System.out.println("[texparser] Starting to parse modules from files "
				+ files);
		System.out
				.println("....................................................");

		double startTime = System.currentTimeMillis();
		int numberOfModules = 0;

		ArrayList<Module> modules = new ArrayList<Module>();

		for (File file : files) {
			String name = file.getName();
			if (name.substring(name.lastIndexOf('.')).equals(".tex")) {
				Module module = parseTexFile(file);
				if (module != null) {
					modules.add(module);
					numberOfModules += 1;
				} else {
					System.out.println("[texparser] error while parsing file "
							+ name);
				}
			} else {
				System.out.println("[texparser] ignoring non-tex file " + name);
			}
		}

		double endTime = System.currentTimeMillis();
		double parseTime = (endTime - startTime) / 1000;

		System.out
				.println("....................................................");
		System.out.println("[texparser] " + numberOfModules
				+ " Modules parsed in " + parseTime + " seconds.");
		System.out
				.println("....................................................");

		return modules;
	}

	/**
	 * @param path
	 *            to the file (or directory) on the server that will be parsed
	 * @return a list containing the parsed modules
	 * @throws IOException
	 */
	public ArrayList<Module> parseFile(String path) throws IOException {

		System.out
				.println("....................................................");
		System.out.println("[texparser] Starting to parse modules at " + path);
		System.out
				.println("....................................................");

		double startTime = System.currentTimeMillis();
		int numberOfModules = 0;

		ArrayList<Module> modules = new ArrayList<Module>();

		File file = new File(path);

		if (file.isDirectory()) {
			ArrayList<File> listOfFiles = getAllFiles(file);
			System.out.println("[texparser] listOfFiles: " + listOfFiles);
			for (File f : listOfFiles) {
				String name = f.getName();
				if (name.substring(name.lastIndexOf('.')).equals(".tex")) {
					modules.add(parseTexFile(f));
					numberOfModules += 1;
				} else {
					System.out.println("[texparser] ignoring non-tex file "
							+ name);
				}
			}
		} else {
			// single file
			String name = file.getName();
			if (name.substring(name.lastIndexOf('.')).equals(".tex")) {
				modules.add(parseTexFile(file));
				numberOfModules = 1;
			}
		}
		double endTime = System.currentTimeMillis();
		double parseTime = (endTime - startTime) / 1000;

		System.out
				.println("....................................................");
		System.out.println("[texparser] " + numberOfModules
				+ " Modules parsed in " + parseTime + " seconds.");
		System.out
				.println("....................................................");

		return modules;
	}

	/**
	 * @param file
	 * @return a list of all files in the directory and its subdirectories
	 */
	private ArrayList<File> getAllFiles(File file) {
		ArrayList<File> files = new ArrayList<File>();
		File[] listOfFiles = file.listFiles();
		for (File f : listOfFiles) {
			if (f.isFile()) {
				files.add(f);
			} else if (f.isDirectory()) {
				// recursively add all files in subdirectories
				files.addAll(getAllFiles(f));
			}
		}
		return files;
	}

	/**
	 * @param file
	 * @return the parsed module
	 * @throws IOException
	 */
	private Module parseTexFile(File file) throws IOException {

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

		Module module = tp.convertToModuleAndDumpInDatabase(texNodes,
				modifier_email);

		System.out.println("[texparser] " + module);

		return module;
	}

	public File parseModule(Module module) {
		
		if(!checkModuleCompatibility(module)) {
			throw new IllegalArgumentException("the passed module is not compatible");
		}
		
		ModuleParser mp = new ModuleParser();
		String texString = mp.convertToTex(module);

		System.out.println(texString);

		String filePath = exportPath + "/" + toValidFilename(module.getName())
				+ ".tex";

		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(filePath));
			out.write(texString);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new File(filePath);
	}

	private String toValidFilename(String name) {
		name = name.replace("ä", "ae");
		name = name.replace("ü", "ue");
		name = name.replace("ö", "oe");
		name = name.replace("Ä", "Ae");
		name = name.replace("Ü", "Ue");
		name = name.replace("Ö", "Oe");
		return name;
	}

	/**
	 * checks if the passed Module has all required fields to be parsed to a
	 * texfile
	 * 
	 * @param module
	 * @return true if the module is compatible, else false
	 */
	private boolean checkModuleCompatibility(Module module) {
		// TODO check if all required ModuleFields are specified
		ArrayList<ModuleField> requiredFields = getRequiredFields();
		ArrayList<ModuleField> moduleFields = module.getModuleFields();

		// for each required field check if there is
		// a ModuleField with the same name and type
		for (ModuleField requiredField : requiredFields) {
			boolean found = false;
			String fieldName = requiredField.getFieldName();
			int fieldType = requiredField.getFieldType();
			for (ModuleField moduleField : moduleFields) {
				if (moduleField.getFieldName().equals(fieldName)
						&& moduleField.getFieldType() == fieldType) {
					found = true;
					break;
				}
			}
			// if no ModuleField with same name was found, return false
			if (!found) {
				System.out
						.println("[texparser] the passed module has no field with name="
								+ fieldName + " and type=" + fieldType);
				return false;
			}
		}
		// all fields were found
		return true;
	}

	/**
	 * @return a list of all required ModuleFields
	 */
	private ArrayList<ModuleField> getRequiredFields() {
		ArrayList<ModuleField> requiredFields = new ArrayList<ModuleField>();
		requiredFields.add(new ModuleField(-1, 3, "Englischer Titel"));
		requiredFields.add(new ModuleField(-1, 3, "Kürzel"));
		requiredFields.add(new ModuleField(-1, 3, "Sprache"));
		requiredFields.add(new ModuleField(-1, 1, "SWS"));
		requiredFields.add(new ModuleField(-1, 1, "ECTS"));
		requiredFields.add(new ModuleField(-1, 1, "Dauer"));
		requiredFields.add(new ModuleField(-1, 3, "Turnus"));
		requiredFields.add(new ModuleField(-1, 6, "periodisch"));
		requiredFields.add(new ModuleField(-1, 4,
				"Voraussetzungen (inhaltlich)"));
		requiredFields.add(new ModuleField(-1, 4, "Voraussetzungen (formal)"));
		requiredFields.add(new ModuleField(-1, 4, "Lernziele"));
		requiredFields.add(new ModuleField(-1, 4, "Inhalt"));
		requiredFields.add(new ModuleField(-1, 4, "Literatur"));
		requiredFields.add(new ModuleField(-1, 1, "Präsenzzeit"));
		requiredFields.add(new ModuleField(-1, 1, "Vor- und Nachbereitung"));
		requiredFields.add(new ModuleField(-1, 4, "Leistungsnachweis"));
		requiredFields.add(new ModuleField(-1, 4, "Notenbildung"));
		return requiredFields;
	}

}
