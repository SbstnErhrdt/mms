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
	
	public Module convertToModuleAndDumpInDatabase(ArrayList<TexNode> texNodes) {
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
				// TODO adapt to model
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
				module.setLearningTarget(adaptLearningTargetAndContent(tn.getContent()));
			} else if(tnName.equals("Literatur")) {
				module.setLiterature(adaptLiterature(tn.getContent()));
			} else if(tnName.equals("Lehrformen")) {
				// handle later
				teachingForms = tn.getContent();				
			} else if(tnName.equals("Arbeitsaufwand")) {
				// TODO adapt to model
				int[] efforts = adaptEffort(tn.getContent());
				module.setEffort_presenceTime(efforts[0]);
				module.setEffort_preAndPost(efforts[1]);
			} else if(tnName.equals("Leistungsnachweis")) {
				// TODO adapt to model
				module.setPerformanceRecord(tn.getContent());
			} else if(tnName.equals("Notenbildung")) {
				// TODO adapt to model
				module.setGradeFormation(tn.getContent());
			} else if(tnName.equals("Grundlagen")) {
				// TODO adapt to model
				module.setBasisFor(tn.getContent());
			} else if(tnName.equals("Ilias")) {
				// TODO adapt to model	
				module.setIlias(tn.getContent());
			}
		}
	
		module.setModifier_email(systemEmail);
		
		ContentDbController db = new ContentDbController();
	
		System.out.println(module);
		
		db.createModule(module);
		
		createChildEvents(teachingForms, module.getID());
			
		db.closeConnection();
		
		return module;
	}

	private ArrayList<String> adaptLecturers(String content) {
		ArrayList<String> lecturers = new ArrayList<String>();
		// match multiple lecturers
		Pattern pattern = Pattern.compile("\\\\(.*?)\\{(.*?)\\}");
		Matcher matcher = pattern.matcher(content);
		while(matcher.find()) {
			// TODO get email of prof
			lecturers.add(matcher.group(1) + ". " +  matcher.group(2));
		}
		return lecturers;
	}

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

	private void createOrUpdateIfExists(Event event, int moduleID) {
		ContentDbController db = new ContentDbController();
		Event existingEvent = db.getEvent(event.getName(), event.getLecturer_email());
		if(existingEvent == null) {
			// create new Event
			db.createEvent(event);
		} else {
			// update existing event
			// add moduleID to the list
			ArrayList<Integer> moduleIDs = existingEvent.getModuleIDs();
			moduleIDs.add(moduleID);
			existingEvent.setModuleIDs(moduleIDs);
			db.updateEvent(existingEvent);
		}
		
		db.closeConnection();
	}

	private String adaptLiterature(String contentString) {
		// replace \skript, \buch etc
		contentString = contentString.replaceAll("\\\\buch\\{(.*?)\\}", "Buch: $1");
		contentString = contentString.replaceAll("\\\\buch \\{(.*?)\\}", "Buch: $1");
		contentString = contentString.replaceAll("\\\\skript\\{(.*?)\\}", "Skript: $1");
		contentString = contentString.replaceAll("\\\\skript \\{(.*?)\\}", "Skript: $1");
		contentString = contentString.replaceAll("\\\\aufsatz\\{(.*?)\\}", "Aufsatz: $1");
		contentString = contentString.replaceAll("\\\\aufsatz \\{(.*?)\\}", "Aufsatz: $1");
		return contentString;
	}

	private String adaptLearningTargetAndContent(String contentString) {
		String content = "";
		
		// replace "\spiegelstrich" with " - "
		int index =	contentString.indexOf("\\spiegelstrich");
		if(index != -1) {
			curlyCounter = 0;
			content += contentString.substring(0, index);
			content += " - " + contentString.substring(contentString.indexOf("{", index)+1, 
					contentString.indexOf("{", index)+indexOfClosingTag(contentString.substring(contentString.indexOf("{", index))));
			index =	contentString.indexOf("\\spiegelstrich", index+15);
			while(index != -1 && contentString.indexOf("{", index) != -1) {
				curlyCounter = 0;
				content += " - " + contentString.substring(contentString.indexOf("{", index)+1, 
						contentString.indexOf("{", index) + 
						indexOfClosingTag(contentString.substring(contentString.indexOf("{", index))));
				index =	contentString.indexOf("\\spiegelstrich", index+15);
			}
			// add the rest
			if(contentString.indexOf("{", contentString.lastIndexOf("\\spiegelstrich")) != -1) {
				content += contentString.substring(contentString.indexOf("{", contentString.lastIndexOf("\\spiegelstrich"))+
						indexOfClosingTag(contentString.substring(contentString.indexOf("{", contentString.lastIndexOf("\\spiegelstrich"))-1)));
			}
		}
		
		return content;
	}

	/**
	 * @param content
	 * @return the subjectIDs that belong to the content string
	 */
	private ArrayList<Integer> adaptSubjectIDs(String content) {
		
		ArrayList<String[]> tags = getClassificationTags(content);
		ArrayList<Integer> subjectIDs = new ArrayList<Integer>();
		
		ArrayList<String> names = new ArrayList<String>();
		for(int i=0; i<tags.size(); i++) {
			names.add(tags.get(i)[0]);
		}
		
		ArrayList<Integer> studycourseIDs = getStudyourseIDs(names);
		
		
		// TODO get IDs of the subjects in the contentString
		ContentDbController db = new ContentDbController();
	
		for(int i=0; i<tags.size(); i++) {
			String[] tag = tags.get(i);
			int studycourseID = studycourseIDs.get(i);
			int subjectID = db.getSubjectID(tag[1], studycourseID);
			if(subjectID != -1) {
				// Subject does exist
				subjectIDs.add(subjectID);
			} else {
				// Subject does not exist
				Subject subject = new Subject();
				subject.setName(tag[1]);
				subject.setStudycourses_studycourseID(studycourseID);
				subject.setModifier_email(systemEmail);
				db.createSubject(subject);
				subjectIDs.add(subject.getID());
			}
		}
		
		System.out.println("ascertained subjectIDs: " + subjectIDs);
		
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
	 * @param names
	 * @return the studycourseIDs that belong to the committed names
	 */
	private ArrayList<Integer> getStudyourseIDs(ArrayList<String> names) {
		ArrayList<Integer> studycourseIDs = new ArrayList<Integer>();
		
		ContentDbController db = new ContentDbController();
		
		for(int i=0; i<names.size(); i++) {
			int studycourseID = db.getStudycourseID(names.get(i));
			if(studycourseID != -1) {
				// studycourse exists
				studycourseIDs.add(studycourseID);
			} else {
				// stuycourse does not exist => create it
				Studycourse studycourse = new Studycourse();
				studycourse.setName(names.get(i));
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
	 * @return each extracted String between every third curly braces 
	 */
	private ArrayList<String[]> getClassificationTags(String content) {
		ArrayList<String[]> cTags = new ArrayList<String[]>();
		
		Pattern pattern = Pattern.compile("(\\\\.*?\\{.*?\\})\\{\\\\(.*?)\\}\\{(.*?)\\}");
		Matcher matcher = pattern.matcher(content);
		
		while(matcher.find()) {
			String tag1 = matcher.group(1);
			tag1 = tag1.replace("\\Inf{\\Ba}", "Bachelor Informatik");
			tag1 = tag1.replace("\\Inf{\\Ma}", "Master Informatik");
			tag1 = tag1.replace("\\MedInf{\\Ba}", "Bachelor Medieninformatik");
			tag1 = tag1.replace("\\MedInf{\\Ma}", "Master Medieninformatik");
			tag1 = tag1.replace("\\SwEng{\\Ba}", "Bachelor Software Engineering");
			tag1 = tag1.replace("\\SwEng{\\Ma}", "Master Software Engineering");	
			tag1 = tag1.replace("\\IST{\\Ba}", "Bachelor Informationssystemtechnik");
			tag1 = tag1.replace("\\IST{\\Ma}", "Master Informationssystemtechnik");
			tag1 = tag1.replace("\\ET{\\Ba}", "Bachelor Elektrotechnik");
			tag1 = tag1.replace("\\ET{\\Ma}", "Master Elektrotechnik");
			tag1 = tag1.replace("\\Comm{\\Ma}", "Master Communications Technology");
			tag1 = tag1.replace("\\Inf{\\La}", "Lehramt Informatik");
			
			String tag3 = matcher.group(3);
			tag3 = tag3.replace("\\MEI", "Mediale Informatik");
			tag3 = tag3.replace("\\PAI", "Praktische und Angewandte Informatik");
			
			String[] tags = {tag1, matcher.group(2) + " " + tag3};
			cTags.add(tags);
		}
		
		return cTags;
	}

	private String adaptDirector(String director) {
		/* TODO
		 * replace \StudiendekanInf etc correctly and get their email?
		 */
		
		return "todo@ex-studios.net";
	}
}

