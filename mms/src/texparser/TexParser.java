package texparser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import controller.ContentDbController;
import controller.GlobalVarDbController;
import controller.UserDbController;

import model.content.Event;
import model.content.Module;
import model.content.ModuleField;
import model.content.Studycourse;
import model.content.Subject;

public class TexParser {

	private int curlyCounter = 0;
	private final String systemEmail = "sopra@ex-studios.net";

	/**
	 * @param tex
	 * @return an ArrayList with the splitted TexNodes
	 * @throws IOException
	 */
	public ArrayList<TexNode> parseTexNodes(String tex) throws IOException {
		BufferedReader reader = new BufferedReader(new StringReader(tex));
		String line = reader.readLine();

		ArrayList<TexNode> texNodes = new ArrayList<TexNode>();

		while ((line = reader.readLine()) != null) {
			line = removeComment(line);
			if (line.startsWith("\\")) {
				curlyCounter = 0;
				if (line.indexOf("{", 2) != -1) {
					String name = line.substring(1, line.indexOf("{", 2));

					String content = "";

					// add the first line if it has no closing tag
					if (indexOfClosingTag(line.substring(line.indexOf("{", 2))) == -1) {
						content += line.substring(line.indexOf("{", 2) + 1)
								+ " ";
						// add lines which have no closing tag
						if ((line = reader.readLine()) != null) {
							line = removeComment(line);
							while (indexOfClosingTag(line) == -1) {
								content += line + " ";
								if ((line = reader.readLine()) == null) {
									break;
								}
								line = removeComment(line);
							}
						}
						// finish the String if there is a closing tag (add
						// nothing if it is at the start of the line)
						if (indexOfClosingTag(line) != 0) {
							content += line.substring(0, line.indexOf("}"));
						}
					} else {
						// finish the String if there is a closing tag in the
						// first line
						content += line.substring(line.indexOf("{", 2) + 1,
								line.indexOf("}", line.indexOf("{", 2) + 1));
					}
					texNodes.add(new TexNode(name, content));
				}
			}
		}

		texNodes = cleanContent(texNodes);

		return texNodes;

	}

	/**
	 * @param line
	 * @return the line without '%' and everything after it (but checks if the %
	 *         is countered as '\%')
	 */
	private String removeComment(String line) {
		int index = line.indexOf("%");
		if (index != -1) {
			if (index == 0) { // '%' at line beginning (of substring)
				return "";
			} else {
				if (line.charAt(index - 1) == '\\') { // '\%'
					// go on searching for '%'s recursive
					return line.substring(0, index + 1)
							+ removeComment(line.substring(index + 1));
				} else {
					return line.substring(0, index);
				}
			}
		} else {
			// no '%'s found
			return line;
		}
	}

	/**
	 * @param line
	 * @return the index of the closing tag ('}') that belongs to the first
	 *         opening tag ('{')
	 */
	private int indexOfClosingTag(String line) {
		char c;
		for (int i = 0, n = line.length(); i < n; i++) {
			c = line.charAt(i);
			// add 1 to curlyCounter if there is an opening tag
			if (c == '{')
				curlyCounter++;
			else if (c == '}') {
				// remove 1 from curlyCounter if there is a closing tag
				// if curlyCounter = 0, it is the last closing tag
				if ((curlyCounter = curlyCounter - 1) == 0) {
					return i;
				}
			}
		}
		return -1;
	}

	/**
	 * @param texNodes
	 * @return the tex nodes, with adapted content strings
	 */
	private ArrayList<TexNode> cleanContent(ArrayList<TexNode> texNodes) {
		for (TexNode tn : texNodes) {
			if (tn.getContent() != null) {
				String content = tn.getContent();

				// replace tabs with spaces
				content = content.replace("\t", " ");

				// replace multiple whitespaces by one
				content = content.replaceAll("\\s+", " ");

				// replace \S by §
				content = content.replaceAll("\\\\\\S(\\d+)", "§$1");

				// replace \"a by ä etc.
				content = content.replace("\\\"a", "ä");
				content = content.replace("\\\"o", "ö");
				content = content.replace("\\\"u", "ü");
				content = content.replace("\\\"A", "Ä");
				content = content.replace("\\\"O", "Ö");
				content = content.replace("\\\"U", "Ü");

				// replace \'a etc by á etc.
				content = content.replace("\\'a", "á");
				content = content.replace("\\'o", "ó");
				content = content.replace("\\'u", "ú");

				// replace \& by &
				content = content.replace("\\&", "&");

				// replace \@ by @
				content = content.replace("\\@", "@");

				// replace `` by \"
				content = content.replace("``", "\"");

				tn.setContent(content.trim());
			}
		}
		return texNodes;
	}

	/**
	 * converts a list of texnodes to a Module object snd dumps it in the
	 * database
	 * 
	 * @param texNodes
	 * @param modifier_email
	 * @return the converted module
	 */
	public Module convertToModuleAndDumpInDatabase(ArrayList<TexNode> texNodes,
			String modifier_email) {
		Module module = new Module();
		String teachingForms = "";
		ArrayList<ModuleField> moduleFields = new ArrayList<ModuleField>();

		for (TexNode tn : texNodes) {
			String tnName = tn.getName();

			if (tnName.equals("Modultitel")) {
				module.setName(tn.getContent());
			} else if (tnName.equals("EnglischerTitel")) {
				moduleFields.add(new ModuleField(-1, 3, "Englischer Titel", tn
						.getContent()));
			} else if (tnName.equals("Modulkuerzel")) {
				moduleFields.add(new ModuleField(-1, 3, "Kürzel", tn
						.getContent()));
			} else if (tnName.equals("Sprache")) {
				moduleFields.add(new ModuleField(-1, 3, "Sprache", tn
						.getContent()));
			} else if (tnName.equals("SWS")) {
				moduleFields
						.add(new ModuleField(-1, 1, "SWS", tn.getContent()));
			} else if (tnName.equals("ECTS")) {
				moduleFields
						.add(new ModuleField(-1, 1, "ECTS", tn.getContent()));
			} else if (tnName.equals("Moduldauer")) {
				moduleFields.add(new ModuleField(-1, 1, "Dauer", tn
						.getContent()));
			} else if (tnName.equals("Turnus")) {
				String[] rotations = adaptRotation(tn.getContent());
				moduleFields
						.add(new ModuleField(-1, 3, "Turnus", rotations[0]));
				moduleFields.add(new ModuleField(-1, 6, "periodisch",
						rotations[1]));
			} else if (tnName.equals("Modulverantwortlicher")) {
				module.setDirector_email(adaptDirector(tn.getContent()));
			} else if (tnName.equals("Dozenten")) {
				ArrayList<String> lecturers = adaptLecturers(tn.getContent());
				module.setLecturers(lecturers);
			} else if (tnName.equals("Einordnung")) {
				module.setSubjectIDs(adaptSubjectIDs(tn.getContent()));
			} else if (tnName.equals("VoraussetzungenInhaltlich")) {
				moduleFields.add(new ModuleField(-1, 4,
						"Voraussetzungen (inhaltlich)", adaptLists(tn
								.getContent())));
			} else if (tnName.equals("VoraussetzungenFormal")) {
				moduleFields
						.add(new ModuleField(-1, 4, "Voraussetzungen (formal)",
								adaptLists(tn.getContent())));
			} else if (tnName.equals("Lernziele")) {
				moduleFields.add(new ModuleField(-1, 4, "Lernziele",
						adaptLearningTargetAndContent(tn.getContent())));
			} else if (tnName.equals("Inhalt")) {
				moduleFields.add(new ModuleField(-1, 4, "Inhalt",
						adaptLearningTargetAndContent(tn.getContent())));
			} else if (tnName.equals("Literatur")) {
				moduleFields.add(new ModuleField(-1, 4, "Literatur",
						adaptLiterature(tn.getContent())));
			} else if (tnName.equals("Lehrformen")) {
				// handle later
				teachingForms = tn.getContent();
			} else if (tnName.equals("Arbeitsaufwand")) {
				int[] efforts = adaptEffort(tn.getContent());
				moduleFields.add(new ModuleField(-1, 1, "Präsenzzeit", ""
						+ efforts[0]));
				moduleFields.add(new ModuleField(-1, 1,
						"Vor- und Nachbereitung", "" + efforts[1]));
			} else if (tnName.equals("Leistungsnachweis")) {
				moduleFields.add(new ModuleField(-1, 4, "Leistungsnachweis", tn
						.getContent()));
			} else if (tnName.equals("Notenbildung")) {
				moduleFields.add(new ModuleField(-1, 4, "Notenbildung", tn
						.getContent()));
			} else if (tnName.equals("Grundlagen")) {
				moduleFields.add(new ModuleField(-1, 4, "Grundlage für", tn
						.getContent()));
			} else if (tnName.equals("Ilias")) {
				moduleFields.add(new ModuleField(-1, 3, "Ilias", tn
						.getContent()));
			}
		}

		if (modifier_email == null) {
			module.setModifier_email(systemEmail);
		} else {
			module.setModifier_email(modifier_email);
		}

		// at least the name must not be null
		if (module.getName() != null) {
			ContentDbController db = new ContentDbController();

			module.setModuleFields(moduleFields);

			db.createModule(module);

			createChildEvents(teachingForms, module.getID());

			db.closeConnection();
		} else {
			System.out
					.println("[texparser] no module has been created because there was no name found.");
		}

		return module;
	}

	/**
	 * @param content
	 * @return the adapted content string
	 */
	private ArrayList<String> adaptLecturers(String content) {
		ArrayList<String> lecturers = new ArrayList<String>();
		// match multiple lecturers
		Pattern pattern = Pattern.compile("\\\\(.*?)\\{(.*?)\\}");
		Matcher matcher = pattern.matcher(content);
		while (matcher.find()) {
			// get email of prof
			String email = getEmailByName(matcher.group(2));
			if (email == null) {
				// no email found => insert the name
				lecturers.add(matcher.group(2));
			} else {
				lecturers.add(email);
			}
		}
		return lecturers;
	}

	/**
	 * tries to find an email that belongs to the passed title and name
	 * 
	 * @param name
	 * @return the email of the user with the passed title and name (if exists)
	 */
	private String getEmailByName(String name) {
		String[] fields = extractTitleFirstNameLastName(name);
		UserDbController db = new UserDbController();

		String email = db.getUserEmail(fields[1], fields[2]);

		return email;
	}

	/**
	 * @param name
	 * @return {title, firstName, lastName}
	 */
	private String[] extractTitleFirstNameLastName(String name) {
		String[] fields = new String[3];

		Pattern pattern = Pattern
				.compile("(((Dr|Dipl).(-Ing.)?)\\s)?(\\w+)\\s(\\w+)");
		Matcher matcher = pattern.matcher(name);

		if (matcher.find()) {
			fields[0] = matcher.group(2);
			fields[1] = matcher.group(5);
			fields[2] = matcher.group(6);
		}

		return fields;
	}

	/**
	 * @param content
	 * @return the adapted content string
	 */
	private String[] adaptRotation(String content) {
		String[] rotations = new String[2];
		// match \sporadisch
		Pattern pattern = Pattern.compile("\\\\sporadisch\\{(.*?)\\}");
		Matcher matcher = pattern.matcher(content);
		if (matcher.find()) {
			rotations[0] = matcher.group(1);
			rotations[1] = "false";
		} else {
			pattern = Pattern.compile("\\\\periodisch\\{(.*?)\\}");
			matcher = pattern.matcher(content);
			if (matcher.find()) {
				rotations[0] = matcher.group(1).replace("\\", "");
				rotations[1] = "true";
			}
		}
		return rotations;
	}

	/**
	 * @param content
	 * @return the presence time and preAndPost effort in an array with two
	 *         entries
	 */
	private int[] adaptEffort(String content) {
		int[] efforts = new int[2];
		// match Praesenzzeit, VorNachbereitung
		Pattern pattern = Pattern.compile("\\\\Praesenzzeit\\{([0-9]+)\\}");
		Matcher matcher = pattern.matcher(content);
		if (matcher.find()) {
			efforts[0] = Integer.parseInt(matcher.group(1));
		}
		pattern = Pattern.compile("\\\\VorNachbereitung\\{([0-9]+)\\}");
		matcher = pattern.matcher(content);
		if (matcher.find()) {
			efforts[1] = Integer.parseInt(matcher.group(1));
		}
		return efforts;
	}

	/**
	 * creates the child events found in the content string
	 * 
	 * @param content
	 * @param moduleID
	 * @return true if successfull
	 */
	private boolean createChildEvents(String content, int moduleID) {

		// find \Prj, Vlg, Ubg etc
		Pattern pattern = Pattern.compile("\\\\(.*?)\\{(.*?)\\}\\{(.*?)\\}");
		Matcher matcher = pattern.matcher(content);

		while (matcher.find()) {
			String teachingForm = matcher.group(1);
			if (teachingForm.equals("Vlg")) {
				teachingForm = "Vorlesung";
			} else if (teachingForm.equals("Ubg")) {
				teachingForm = "Übung";
			} else if (teachingForm.equals("Prj")) {
				teachingForm = "Projekt";
			} else if (teachingForm.equals("Tut")) {
				teachingForm = "Tutorium";
			} else if (teachingForm.equals("Lab")) {
				teachingForm = "Labor";
			} else if (teachingForm.equals("Sem")) {
				teachingForm = "Seminar";
			} else if (teachingForm.equals("ProSem")) {
				teachingForm = "Proseminar";
			} else if (teachingForm.equals("Pra")) {
				teachingForm = "Praktikum";
			} else if (teachingForm.equals("PrjSem")) {
				teachingForm = "Projektseminar";
			} else if (teachingForm.equals("BaArb")) {
				teachingForm = "Bachelorarbeit";
			} else if (teachingForm.equals("MaArb")) {
				teachingForm = "Masterarbeit";
			}
			Event event = new Event();
			event.setName(matcher.group(2));
			event.setLecturer_email(matcher.group(3));
			ArrayList<Integer> moduleIDs = new ArrayList<Integer>();
			moduleIDs.add(moduleID);
			event.setModuleIDs(moduleIDs);
			event.setType(teachingForm);
			createOrUpdateIfExists(event, moduleID);
		}

		return true;
	}

	/**
	 * updates the passed event in the database if it already exists, if not
	 * creates a new one
	 * 
	 * @param event
	 * @param moduleID
	 */
	private void createOrUpdateIfExists(Event event, int moduleID) {
		ContentDbController db = new ContentDbController();
		Event existingEvent = db.getEvent(event.getName(), event.getType(),
				event.getLecturer_email());
		if (existingEvent == null) {
			// create new Event
			db.createEvent(event);
		} else {
			// update existing event
			// add moduleID to its list
			ArrayList<Integer> moduleIDs = existingEvent.getModuleIDs();
			moduleIDs.add(moduleID);
			existingEvent.setModuleIDs(moduleIDs);
			db.updateEvent(existingEvent);
		}

		db.closeConnection();
	}

	/**
	 * @param contentString
	 * @return the adapted content string
	 */
	private String adaptLiterature(String contentString) {
		if (contentString != null) {
			if (!contentString.equals("")) {
				contentString = "<ul>" + contentString + "</ul>";
				// replace \skript, \buch etc
				contentString = contentString.replaceAll(
						"\\\\textit\\s?\\{(.*?)\\}", "<li>$1</li>");
				contentString = contentString.replaceAll(
						"\\\\buch\\s?\\{(.*?)\\}", "<li>Buch: $1</li>");
				contentString = contentString.replaceAll(
						"\\\\skript\\s?\\{(.*?)\\}", "<li>Skript: $1</li>");
				contentString = contentString.replaceAll(
						"\\\\aufsatz\\s?\\{(.*?)\\}", "<li>Aufsatz: $1</li>");
			}
		}
		return contentString;
	}

	/**
	 * @param content
	 * @return the adapted content string
	 */
	private String adaptLearningTargetAndContent(String content) {
		return adaptLists(content);
	}

	/**
	 * replaces tex lists with HTML lists
	 * 
	 * @param content
	 * @return the adapted content
	 */
	private String adaptLists(String content) {
		// replace \spiegelstrich with <ul> <li><\li> ... </ul>
		Pattern pattern = Pattern
				.compile("((\\\\spiegelstrich\\s?\\{.*?\\}\\s?)+)");
		Matcher matcher = pattern.matcher(content);

		while (matcher.find()) {
			content = matcher.replaceAll("<ul>"
					+ replaceBulletPoints(matcher.group(1)) + "</ul>");
		}
		return content.trim();
	}

	/**
	 * @param string
	 * @return the string with \spiegelstrich{...} replaced by <li>...</li>
	 */
	private String replaceBulletPoints(String string) {
		string = string.replaceAll("\\\\spiegelstrich\\s?\\{(.*?)\\}",
				"<li>$1</li>");
		return string;
	}

	/**
	 * @param content
	 * @return the subjectIDs that belong to the content string
	 */
	private ArrayList<Integer> adaptSubjectIDs(String content) {

		ArrayList<String[]> tags = getClassificationTags(content);
		ArrayList<Integer> subjectIDs = new ArrayList<Integer>();

		ArrayList<String[]> graduationsAndNames = new ArrayList<String[]>();
		for (int i = 0; i < tags.size(); i++) {
			String[] graduationAndName = { tags.get(i)[0], tags.get(i)[1] };
			graduationsAndNames.add(graduationAndName);
		}

		ArrayList<Integer> studycourseIDs = getStudyourseIDs(graduationsAndNames);

		ContentDbController db = new ContentDbController();

		for (int i = 0; i < tags.size(); i++) {
			String[] tag = tags.get(i);
			int studycourseID = studycourseIDs.get(i);
			int subjectID = db.getSubjectID(tag[2], tag[3], studycourseID);
			if (subjectID != -1) {
				// Subject does exist
				subjectIDs.add(subjectID);
			} else {
				// Subject does not exist
				Subject subject = new Subject();
				subject.setType(tag[2]);
				subject.setName(tag[3]);
				subject.setStudycourses_studycourseID(studycourseID);
				subject.setModifier_email(systemEmail);
				db.createSubject(subject);
				subjectIDs.add(subject.getID());
			}
		}

		System.out.println("[texparser] ascertained subjectIDs: " + subjectIDs);

		db.closeConnection();

		subjectIDs = removeDuplicates(subjectIDs);

		return subjectIDs;
	}

	/**
	 * uses a HashSet to remove duplicates
	 * 
	 * @param subjectIDs
	 * @return the ArrayList with removed duplicates
	 */
	private ArrayList<Integer> removeDuplicates(ArrayList<Integer> subjectIDs) {
		Set<Integer> set = new HashSet<Integer>(subjectIDs);
		return new ArrayList<Integer>(set);
	}

	/**
	 * creates nonexistent studycourses
	 * 
	 * @param graduationsAndNames
	 * @return the studycourseIDs that belong to the committed names
	 */
	private ArrayList<Integer> getStudyourseIDs(
			ArrayList<String[]> graduationsAndNames) {
		ArrayList<Integer> studycourseIDs = new ArrayList<Integer>();

		ContentDbController db = new ContentDbController();

		for (int i = 0; i < graduationsAndNames.size(); i++) {
			String graduation = graduationsAndNames.get(i)[0];
			String name = graduationsAndNames.get(i)[1];
			int studycourseID = db.getStudycourseID(graduation, name);
			if (studycourseID != -1) {
				// studycourse exists
				studycourseIDs.add(studycourseID);
			} else {
				// stuycourse does not exist => create it
				Studycourse studycourse = new Studycourse();
				studycourse.setGraduation(graduation);
				studycourse.setName(name);
				studycourse.setModifier_email(systemEmail);
				db.createStudycourse(studycourse);
				studycourseIDs.add(studycourse.getID());
			}
		}

		db.closeConnection();

		return studycourseIDs;
	}

	/**
	 * @param content
	 * @return the extracted tags in a list of string arrays
	 */
	private ArrayList<String[]> getClassificationTags(String content) {
		ArrayList<String[]> cTags = new ArrayList<String[]>();

		Pattern pattern = Pattern
				.compile("\\\\(.*?)\\{\\\\(.*?)\\}\\{\\\\(.*?)\\}\\{(.*?)\\}");
		Matcher matcher = pattern.matcher(content);

		while (matcher.find()) {
			String tag1 = matcher.group(1);
			if (tag1.equals("Inf"))
				tag1 = "Informatik";
			else if (tag1.equals("MedInf"))
				tag1 = "Medieninformatik";
			else if (tag1.equals("SwEng"))
				tag1 = "Software Engineering";
			else if (tag1.equals("IST"))
				tag1 = "Informationssystemtechnik";
			else if (tag1.equals("ET"))
				tag1 = "Elektrotechnik";
			else if (tag1.equals("Comm"))
				tag1 = "Communications Technology";
			else if (tag1.equals("AdvMat"))
				tag1 = "Advanced Materials";
			else if (tag1.equals("Math"))
				tag1 = "Mathematik";
			else if (tag1.equals("ChemIng"))
				tag1 = "Chemieingenieurwesen";

			String tag2 = matcher.group(2);
			if (tag2.equals("Ba"))
				tag2 = "Bachelor";
			else if (tag2.equals("Ma"))
				tag2 = "Master";
			else if (tag2.equals("La"))
				tag2 = "Lehramt";

			String tag4 = matcher.group(4);
			if (tag4.equals("\\MEI"))
				tag4 = "Mediale Informatik";
			else if (tag4.equals("\\PAI"))
				tag4 = "Praktische und Angewandte Informatik";
			else if (tag4.equals("\\TSI"))
				tag4 = "Technische und Systemnahe Informatik";
			else if (tag4.equals("\\TMI"))
				tag4 = "Theoretische und Mathematische Methoden der Informatik";
			else if (tag4.equals("\\Medieninformatik"))
				tag4 = "Medieninformatik";
			else if (tag4.equals("\\Mathematik"))
				tag4 = "Mathematik";
			else if (tag4.equals("\\AngewandteMathematik"))
				tag4 = "Angewandte Mathematik";
			else if (tag4.equals("\\SoftwareEngineering"))
				tag4 = "Software-Engineering";
			else if (tag4.equals("\\AET"))
				tag4 = "Allgemeine Elektrotechnik";
			else if (tag4.equals("\\Ingwi"))
				tag4 = "Ingenieurwissenschaften";
			else if (tag4.equals("\\AUT"))
				tag4 = "Automatisierungs- und Energietechnik";
			else if (tag4.equals("\\CE"))
				tag4 = "Communications Engineering";
			else if (tag4.equals("\\KUS"))
				tag4 = "Komunikations- und Systemtechnik";
			else if (tag4.equals("\\Mikro"))
				tag4 = "Mikroelektronik";
			String[] tags = { tag2, tag1, matcher.group(3), tag4 };
			cTags.add(tags);
		}

		return cTags;
	}

	/**
	 * @param director
	 * @return the adapted content string
	 */
	private String adaptDirector(String director) {
		String email;

		if (director.contains("\\StudiendekanInf")
				|| director.contains("\\StudienDekanInf")) {
			GlobalVarDbController db = new GlobalVarDbController();
			email = db.getGlobalVar("StudiendekanInf");
			db.closeConnection();
		} else if (director.contains("StudiendekanET")
				|| director.contains("StudienDekanET")) {
			GlobalVarDbController db = new GlobalVarDbController();
			email = db.getGlobalVar("StudiendekanET");
			db.closeConnection();
		} else if (director.contains("StudiendekanIST")
				|| director.contains("StudienDekanIST")) {
			GlobalVarDbController db = new GlobalVarDbController();
			email = db.getGlobalVar("StudiendekanIST");
			db.closeConnection();
		} else if (director.contains("StudiendekanComm")
				|| director.contains("StudienDekanComm")) {
			GlobalVarDbController db = new GlobalVarDbController();
			email = db.getGlobalVar("StudiendekanComm");
			db.closeConnection();
		} else {
			Pattern pattern = Pattern.compile("\\\\(.*?)\\{(.*?)\\}");
			Matcher matcher = pattern.matcher(director);
			if (matcher.find()) {
				// get email of prof
				email = getEmailByName(matcher.group(2));
				if (email == null) {
					// no email found => set the name as email
					email = matcher.group(2);
				}
			} else {
				// pattern failed => set original string
				email = director;
			}
		}
		if (email != null)
			return email;
		else
			return director;
	}
}
