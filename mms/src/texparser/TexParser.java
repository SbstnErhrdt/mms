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
import controller.DbController;
import controller.GlobalVarDbController;
import controller.UserDbController;

import model.content.Event;
import model.content.Module;
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
		
		while((line=reader.readLine()) != null) {
			line = removeComment(line);
			if(line.startsWith("\\")) {
				curlyCounter = 0;
				if(line.indexOf("{", 2) != -1) {
					String name = line.substring(1, line.indexOf("{", 2));
				
					String content = "";
					
					// add the first line if it has no closing tag
					if(indexOfClosingTag(line.substring(line.indexOf("{", 2))) == -1) {
						content += line.substring(line.indexOf("{", 2)+1) + " ";
						// add lines which have no closing tag
						if((line=reader.readLine()) != null) {
							line = removeComment(line);
							while(indexOfClosingTag(line) == -1) {
								content += line + " ";
								if((line=reader.readLine()) == null) {
									break;
								}
								line = removeComment(line);
							}
						} 
						// finish the String if there is a closing tag (add nothing if it is at the start of the line)
						if(indexOfClosingTag(line) != 0) {
							content += line.substring(0, line.indexOf("}"));
						}
					} else {
						// finish the String if there is a closing tag in the first line
						content += line.substring(line.indexOf("{", 2)+1, line.indexOf("}", line.indexOf("{", 2)+1));
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
	 * @return the line without '%' and everything after it (but checks if the % is countered as '\%')
	 */
	private String removeComment(String line) {
		int index = line.indexOf("%");
		if(index != -1) {
			if(index == 0) { // '%' at line beginning
				return line.substring(0, index);
			} else {
				if(line.charAt(index-1) == '\\') { // '\%'
					// go on searching for '%'s recursive 
					return line.substring(0, index+1) + removeComment(line.substring(index+1));
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
	 * @return the index of the closing tag ('}') that belongs to the first opening tag ('{')
	 */
	private int indexOfClosingTag(String line) {
		char c; 
		for (int i = 0, n = line.length(); i < n; i++) {
		    c = line.charAt(i);
		    // add 1 to curlyCounter if there is an opening tag
		    if(c == '{') curlyCounter++;
		    else if(c == '}') {
		    	// remove 1 from curlyCounter os there is a closing tag
		    	// if curlyCounter = 0, it is the last closing tag
		    	if((curlyCounter=curlyCounter-1) == 0) {
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
		for(TexNode tn : texNodes) {
			if(tn.getContent() != null) {
				String contentString = tn.getContent();
				
				// replace tabs with spaces
				contentString = contentString.replace("\t", " ");
	
				// replace multiple whitespaces by one
				contentString = contentString.replaceAll("\\s+", " ");
				
				contentString = contentString.trim();
			
				tn.setContent(contentString);
			}
		}
		return texNodes;
	}
	
	/**
	 * converts a list of texnodes to a Module object snd dumps it in the database
	 * @param texNodes
	 * @param modifier_email
	 * @return the converted module
	 */
	public Module convertToModuleAndDumpInDatabase(ArrayList<TexNode> texNodes, String modifier_email) {
		Module module = new Module();
		String teachingForms = "";
		
		for(TexNode tn : texNodes) {
			String tnName = tn.getName();
			
			if(tnName.equals("Modultitel")) {
				module.setName(tn.getContent());
			} else if(tnName.equals("EnglischerTitel")) {
				module.setEnglishTitle(tn.getContent());
			} else if(tnName.equals("Modulkuerzel")) {
				module.setToken(tn.getContent());
			} else if(tnName.equals("Sprache")) {
				module.setLanguage(tn.getContent());
			} else if(tnName.equals("SWS")) {
				module.setSws(tn.getContent());
			} else if(tnName.equals("ECTS")) {
				module.setLp(tn.getContent());
			} else if(tnName.equals("Moduldauer")) {
				module.setDuration(Integer.parseInt(tn.getContent()));
			} else if(tnName.equals("Turnus")) {
				String[] rotations = adaptRotation(tn.getContent());
				module.setRotation(rotations[0]);
				module.setPeriodicalRotation(Boolean.parseBoolean(rotations[1]));
			} else if(tnName.equals("Modulverantwortlicher")) {
				module.setDirector_email(adaptDirector(tn.getContent()));
			} else if(tnName.equals("Dozenten")) {
				ArrayList<String> lecturers = adaptLecturers(tn.getContent());
				module.setLecturers(lecturers);
			} else if(tnName.equals("Einordnung")) {
				module.setSubjectIDs(adaptSubjectIDs(tn.getContent()));
			} else if(tnName.equals("VoraussetzungenInhaltlich")) {
				module.setRequirement_content(tn.getContent());
			} else if(tnName.equals("VoraussetzungenFormal")) {
				module.setRequirement_formal(tn.getContent());
			} else if(tnName.equals("Lernziele")) {
				module.setLearningTarget(adaptLearningTargetAndContent(tn.getContent()));
			} else if(tnName.equals("Inhalt")) {
				module.setContent(adaptLearningTargetAndContent(tn.getContent()));
			} else if(tnName.equals("Literatur")) {
				module.setLiterature(adaptLiterature(tn.getContent()));
			} else if(tnName.equals("Lehrformen")) {
				// handle later
				teachingForms = tn.getContent();				
			} else if(tnName.equals("Arbeitsaufwand")) {
				int[] efforts = adaptEffort(tn.getContent());
				module.setEffort_presenceTime(efforts[0]);
				module.setEffort_preAndPost(efforts[1]);
			} else if(tnName.equals("Leistungsnachweis")) {
				module.setPerformanceRecord(tn.getContent());
			} else if(tnName.equals("Notenbildung")) {
				module.setGradeFormation(tn.getContent());
			} else if(tnName.equals("Grundlagen")) {
				module.setBasisFor(tn.getContent());
			} else if(tnName.equals("Ilias")) {
				module.setIlias(tn.getContent());
			}
		}
	
		if(modifier_email == null) {
			module.setModifier_email(systemEmail);
		} else {
			module.setModifier_email(modifier_email);
		}
		
		// at least the name must not be null
		if(module.getName() != null) {
			ContentDbController db = new ContentDbController();
			
			db.createModule(module);
			
			createChildEvents(teachingForms, module.getID());
				
			db.closeConnection();
		} else {
			System.out.println("[texparser] no module has been created because there was no name found.");
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
		while(matcher.find()) {
			// get email of prof
			String email = getEmailByName(matcher.group(2));
			if(email == null) {
				// no email found => insert the name
				lecturers.add(matcher.group(1) + ". " +  matcher.group(2));
			} else {
				lecturers.add(email);
			}	
		}
		return lecturers;
	}


	/**
	 * tries to find an email that belongs to the passed title and name
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
		
		Pattern pattern = Pattern.compile("((Dr.(-Ing.)?)\\s)?(\\w+)\\s(\\w+)");
		Matcher matcher = pattern.matcher(name);
		
		if(matcher.find()) {
			fields[0] = matcher.group(2);
			fields[1] = matcher.group(4);
			fields[2] = matcher.group(5);
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
		if(matcher.find()) {
			rotations[0] = matcher.group(1);
			rotations[1] = "false";
		} else {
			pattern = Pattern.compile("\\\\periodisch\\{(.*?)\\}");
			matcher = pattern.matcher(content);
			if(matcher.find()) {
				rotations[0] = matcher.group(1).replace("\\", "");
				rotations[1] = "true";
			}
		}
		return rotations;
	}

	/**
	 * @param content
	 * @return the presence time and preAndPost effort in an array with two entries
	 */
	private int[] adaptEffort(String content) {
		int[] efforts = new int[2];
		// match Praesenzzeit, VorNachbereitung
		Pattern pattern = Pattern.compile("\\\\Praesenzzeit\\{([0-9]+)\\}");
		Matcher matcher = pattern.matcher(content);
		if(matcher.find()) {
			efforts[0] = Integer.parseInt(matcher.group(1));
		}
		pattern = Pattern.compile("\\\\VorNachbereitung\\{([0-9]+)\\}");
		matcher = pattern.matcher(content);
		if(matcher.find()) {
			efforts[1] = Integer.parseInt(matcher.group(1));
		}
		return efforts;
	}

	/**
	 * creates the child events found in the content string
	 * @param content
	 * @param moduleID
	 * @return true if successfull
	 */
	private boolean createChildEvents(String content, int moduleID) {
	
		// find \Prj, Vlg, Ubg etc
		Pattern pattern = Pattern.compile("\\\\(.*?)\\{(.*?)\\}\\{(.*?)\\}");
		Matcher matcher = pattern.matcher(content);
		
		while(matcher.find()) {
			String teachingForm = matcher.group(1);
			if(teachingForm.equals("Vlg")) {
				teachingForm = "Vorlesung";
			} else if(teachingForm.equals("Ubg")) {
				teachingForm = "Übung";
			} else if(teachingForm.equals("Prj")) {
				teachingForm = "Projekt";
			} else if(teachingForm.equals("Tut")) {
				teachingForm = "Tutorium";
			} else if(teachingForm.equals("Lab")) {
				teachingForm = "Laborpraktikum";
			} else if(teachingForm.equals("Sem")) {
				teachingForm = "Seminar";
			} else if(teachingForm.equals("ProSem")) {
				teachingForm = "Proseminar";
			}
			Event event = new Event();
			event.setName(teachingForm + " " + matcher.group(2));
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
	 * updates the passed event in the database if it already exists, if not creates a new one
	 * @param event
	 * @param moduleID
	 */
	private void createOrUpdateIfExists(Event event, int moduleID) {
		ContentDbController db = new ContentDbController();
		Event existingEvent = db.getEvent(event.getName(), event.getLecturer_email());
		if(existingEvent == null) {
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
		// replace \skript, \buch etc
		contentString = contentString.replaceAll("\\\\buch\\s?\\{(.*?)\\}", "Buch: $1");
		contentString = contentString.replaceAll("\\\\skript\\s?\\{(.*?)\\}", "Skript: $1");
		contentString = contentString.replaceAll("\\\\aufsatz\\s?\\{(.*?)\\}", "Aufsatz: $1");
		return contentString;
	}

	/**
	 * @param content
	 * @return the adapted content string
	 */
	private String adaptLearningTargetAndContent(String content) {
		// replace \spiegelstrich with <ul> <li><\li> ... </ul>		
		Pattern pattern = Pattern.compile("((\\\\spiegelstrich\\s?\\{.*?\\}\\s?)+)");
		Matcher matcher = pattern.matcher(content);
		
		while(matcher.find()) {
			content = matcher.replaceAll("<ul>"+replaceBulletPoints(matcher.group(1))+"</ul>");
		}
		
		return content;
	}

	/**
	 * @param string
	 * @return the string with \spiegelstrich{...} replaced by <li>...</li>
	 */
	private String replaceBulletPoints(String string) {
		string = string.replaceAll("\\\\spiegelstrich\\s?\\{(.*?)\\}", "<li>$1</li>");
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
		for(int i=0; i<tags.size(); i++) {
			String[] graduationAndName = {tags.get(i)[0], tags.get(i)[1]};
			graduationsAndNames.add(graduationAndName);
		}
		
		ArrayList<Integer> studycourseIDs = getStudyourseIDs(graduationsAndNames);
		
		ContentDbController db = new ContentDbController();
	
		for(int i=0; i<tags.size(); i++) {
			String[] tag = tags.get(i);
			int studycourseID = studycourseIDs.get(i);
			int subjectID = db.getSubjectID(tag[2], tag[3], studycourseID);
			if(subjectID != -1) {
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
	 * @param subjectIDs
	 * @return the ArrayList with removed duplicates
	 */
	private ArrayList<Integer> removeDuplicates(ArrayList<Integer> subjectIDs) {
		Set<Integer> set = new HashSet<Integer>(subjectIDs);
		return new ArrayList<Integer>(set);
	}

	/**
	 * creates nonexistent studycourses
	 * @param graduationsAndNames
	 * @return the studycourseIDs that belong to the committed names
	 */
	private ArrayList<Integer> getStudyourseIDs(ArrayList<String[]> graduationsAndNames) {
		ArrayList<Integer> studycourseIDs = new ArrayList<Integer>();
		
		ContentDbController db = new ContentDbController();
		
		for(int i=0; i<graduationsAndNames.size(); i++) {
			String graduation = graduationsAndNames.get(i)[0];
			String name = graduationsAndNames.get(i)[1];
			int studycourseID = db.getStudycourseID(graduation, name);
			if(studycourseID != -1) {
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
		
		Pattern pattern = Pattern.compile("\\\\(.*?)\\{\\\\(.*?)\\}\\{\\\\(.*?)\\}\\{(.*?)\\}");
		Matcher matcher = pattern.matcher(content);
		
		while(matcher.find()) {
			String tag1 = matcher.group(1);
			tag1 = tag1.replace("Inf", "Informatik");
			tag1 = tag1.replace("MedInf", "Medieninformatik");
			tag1 = tag1.replace("SwEng", "Software Engineering");
			tag1 = tag1.replace("IST", "Informationssystemtechnik");
			tag1 = tag1.replace("ET", "Elektrotechnik");
			tag1 = tag1.replace("Comm", "Communications Technology");
			
			String tag2 = matcher.group(2);
			tag2 = tag2.replace("Ba", "Bachelor");
			tag2 = tag2.replace("Ma", "Master");
			tag2 = tag2.replace("La", "Lehramt");
				
			String tag4 = matcher.group(4);
			tag4 = tag4.replace("\\MEI", "Mediale Informatik");
			tag4 = tag4.replace("\\PAI", "Praktische und Angewandte Informatik");
			
			String[] tags = {tag2, tag1, matcher.group(3), tag4};
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
		
		if(director.contains("\\StudiendekanInf")) {
			GlobalVarDbController db = new GlobalVarDbController();
			email = db.getGlobalVar("StudiendekanInf");
		} else {
			Pattern pattern = Pattern.compile("\\\\(.*?)\\{(.*?)\\}");
			Matcher matcher = pattern.matcher(director);
			if(matcher.find()) {
				// get email of prof
				email = getEmailByName(matcher.group(2));
				if(email == null) {
					// no email found => set the name as email
					email = matcher.group(2);
				}
			} else {
				// patttern failed => set original string
				email = director;
			}
		}

		return email;
	}
}

