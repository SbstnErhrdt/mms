package texparser;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import controller.ContentDbController;
import controller.UserDbController;

import model.User;
import model.content.Event;
import model.content.Module;
import model.content.Studycourse;
import model.content.Subject;

public class ModuleParser {
	
	private String newLine = System.getProperty("line.separator");
	
	/**
	 * @param module
	 * @return a string representation of the module in tex syntax
	 */
	public String convertToTex(Module module) {
		String texString = "";
		
		// svnid
		texString+="\\svnid{$Id$}"+newLine;
		texString+=newLine;
		
		// name
		texString+="\\Modultitel{"+module.getName()+"}"+newLine;
		texString+=newLine;
		
		// token
		texString+="\\Modulkuerzel{"+module.getToken()+"}"+newLine;
		texString+=newLine;
		
		// englishTitle
		texString+="\\EnglischerTitel{"+module.getEnglishTitle()+"}"+newLine;
		texString+=newLine;
		
		// sws
		texString+="\\SWS{"+module.getSws()+"}"+newLine;
		texString+=newLine;
		
		// lp
		texString+="\\ECTS{"+module.getLp()+"}"+newLine;
		texString+=newLine;
		
		// language
		texString+="\\Sprache{"+module.getLanguage()+"}"+newLine;
		texString+=newLine;
		
		// duration
		texString+="\\Moduldauer{"+module.getDuration()+"}"+newLine;
		texString+=newLine;
		
		// rotation
		texString+="\\Turnus{"+newLine;
		if(module.isPeriodicalRotation()) texString+="\\periodisch{";
		else texString+="\\sporadisch{";
		texString+=module.getRotation()+"}"+newLine;
		texString+="}"+newLine;
		texString+=newLine;
		
		// lecturers
		texString+="\\Dozenten{"+newLine;
		// TODO adapt to model: Prof, PD, LB
		for(String lecturer : module.getLecturers()) {
			texString+="\\Prof{"+lecturer+"}"+newLine;
		}
		texString+="}"+newLine;
		texString+=newLine;
		
		// director
		// TODO adapt to model: get Name oe variable name
		texString+="\\Modulverantwortlicher{"+adaptDirector(module.getDirector_email())+"}"+newLine;
		texString+=newLine;
		
		// subjectIDs
		texString+="\\Einordnung{"+newLine;
		for(int subjectID : module.getSubjectIDs()) {
			texString+=adaptSubjectID(subjectID)+newLine;
		}
		texString+="}"+newLine;
		texString+=newLine;
		
		// requirement_content
		texString+="\\VoraussetzungenInhaltlich{"+newLine;
		texString+=module.getRequirement_content();
		texString+="}"+newLine;
		texString+=newLine;
		
		// requirement_formal
		texString+="\\VoraussetzungenFormal{"+newLine;
		texString+=module.getRequirement_formal();
		texString+="}"+newLine;
		texString+=newLine;
		
		// learningTarget
		texString+="\\Lernziele{"+newLine;
		texString+=adaptLearningTarget(module.getLearningTarget())+newLine;
		texString+="}"+newLine;
		texString+=newLine;
		
		// content
		texString+="\\Inhalt{"+newLine;
		texString+=adaptContent(module.getContent())+newLine;
		texString+="}"+newLine;
		texString+=newLine;
		
		// literature
		texString+="\\Literatur{"+newLine;
		texString+=adaptLiterature(module.getLiterature())+newLine;
		texString+="}"+newLine;
		texString+=newLine;
		
		// Lehrformen
		texString+="\\Lehrformen{"+newLine;
		texString+=adaptTeachingForm(module.getID())+newLine;
		texString+="}"+newLine;
		texString+=newLine;
		
		// effort
		texString+="\\Arbeitsaufwand{"+newLine;
		// effort_presenceTime
		int effort_presenceTime = module.getEffort_presenceTime();
		texString+="\\Praesenzzeit{"+effort_presenceTime+"}"+newLine;
		// effort_preAndPost
		int effort_preAndPost = module.getEffort_preAndPost();
		texString+="\\VorNachbereitung{"+effort_preAndPost+"}"+newLine;
		// sum
		texString+="\\Summe{"+(effort_presenceTime+effort_preAndPost)+"}"+newLine;
		texString+="}"+newLine;
		texString+=newLine;
		
		// performance_record
		texString+="\\Leistungsnachweis{"+newLine;
		texString+=module.getPerformanceRecord()+newLine;
		texString+="}"+newLine;
		texString+=newLine;
		
		// gradeFormation
		texString+="\\Notenbildung{"+newLine;
		texString+=module.getGradeFormation()+newLine;
		texString+="}"+newLine;
		texString+=newLine;
		
		// basisFor
		if(module.getBasisFor() != null) {
			texString+="\\Grundlagen{"+newLine;
			texString+=module.getBasisFor()+newLine;
			texString+="}"+newLine;
			texString+=newLine;
		}

		// ilias
		if(module.getIlias() != null) {
			texString+="\\Ilias{"+newLine;
			texString+=module.getIlias()+newLine;
			texString+="}"+newLine;
			texString+=newLine;
		}
		
		return texString;
	}

	private String adaptDirector(String director_email) {
		UserDbController db = new UserDbController();
		User user = db.getUser(new User(director_email));
		db.closeConnection();
		if(user != null) {
			return user.getTitle()+" "+user.getFirstName()+" "+user.getLastName();
		} else {
			return director_email;
		}
		
	}

	private String adaptTeachingForm(int moduleID) {
		// TODO get Event names and types
		ContentDbController db = new ContentDbController();
		
		// TODO getOnlyEnabled
		ArrayList<Event> events = db.getModuleEvents(moduleID, false);
		
		db.closeConnection();
		
		String teachingForm = "";
		
		for(Event e : events) {
			String type = e.getType();
			if(type.equals("Vorlesung")) type = "Vlg";
			else if(type.equals("Übung")) type = "Ubg";
			else if(type.equals("Projekt")) type = "Prj";
			else if(type.equals("Tutorium")) type = "Tut";
			else if(type.equals("Laborpraktikum")) type = "Lab";
			else if(type.equals("Seminar")) type = "Sem";
			else if(type.equals("Proseminar")) type = "ProSem";
			
			teachingForm += newLine+"\\"+type+"{"+e.getName()+"}{"+adaptDirector(e.getLecturer_email())+"}";
		}
			
		return teachingForm.trim();
	}


	private String adaptLiterature(String literature) {
		// TODO replace \buch etc
		return literature;
	}

	private String adaptContent(String content) {
		
		// remove <ul> and </ul>
		content = content.replace("<ul>", "");
		content = content.replace("</ul>", "");
		
		content = content.replaceAll("<li>(.*?)</li>\\s?", newLine+"\\\\spiegelstrich{$1}");
		
		return content.trim();
	}

	private String adaptLearningTarget(String learningTarget) {
		learningTarget = adaptContent(learningTarget);
		return learningTarget;
	}

	private String adaptSubjectID(int subjectID) {
		// TODO adapt to model: \Inf{\Ba} ...
		ContentDbController db = new ContentDbController();
		
		Subject subject = db.getSubject(subjectID);
		
		Studycourse studycourse = db.getStudycourse(subject.getStudycourses_studycourseID());
		
		db.closeConnection();
		
		String studycourseName = studycourse.getName();
				
		if(studycourseName.equals("Informatik")) studycourseName = "Inf";
		else if(studycourseName.equals("Medieninformatik")) studycourseName = "MedInf";
		else if(studycourseName.equals("Software Engineering")) studycourseName = "SwEng";
		else if(studycourseName.equals("Informationssystemtechnik")) studycourseName = "IST";
		else if(studycourseName.equals("Elektrotechnik")) studycourseName = "ET";
		else if(studycourseName.equals("Communications Technology")) studycourseName = "Comm";
		
		String studycourseGraduation = studycourse.getGraduation();
		
		if(studycourseGraduation.equals("Bachelor")) studycourseGraduation = "Ba";
		else if(studycourseGraduation.equals("Master")) studycourseGraduation = "Ma";
		else if(studycourseGraduation.equals("Lehramt")) studycourseGraduation = "La";
		
		String subjectName = subject.getName();
		subjectName = subjectName.replace("Mediale Informatik", "\\MEI");
		subjectName = subjectName.replace("Praktische und Angewandte Informatik", "\\PAI");
		
		return "\\"+studycourseName+"{\\"+studycourseGraduation+"}{\\"+subject.getType()+"}{"+subjectName+"}";
	}
}
