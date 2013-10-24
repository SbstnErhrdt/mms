package texparser;

import java.util.ArrayList;

import controller.ContentDbController;
import controller.GlobalVarDbController;
import controller.UserDbController;

import model.User;
import model.content.Event;
import model.content.Module;
import model.content.Studycourse;
import model.content.Subject;

public class ModuleParser {
	
	private final String newLine = System.getProperty("line.separator");
	
	/**
	 * converts a Module object to a string representation of a tex file
	 * 
	 * @param module
	 * @return a string representation of the module in tex syntax
	 */
	public String convertToTex(Module module) throws IllegalArgumentException {
		
		if(module == null) throw new IllegalArgumentException("module must not be null");
		
		String texString = "";
		
		// svnid
		texString+="\\svnid{$Id$}"+newLine;
		texString+=newLine;
		
		// name
		texString+="\\Modultitel{"+module.getName()+"}"+newLine;
		texString+=newLine;
		
		// token
		texString+="\\Modulkuerzel{"+module.getModuleFieldValue("Kürzel")+"}"+newLine;
		texString+=newLine;
		
		// englishTitle
		texString+="\\EnglischerTitel{"+module.getModuleFieldValue("Englischer Titel")+"}"+newLine;
		texString+=newLine;
		
		// sws
		texString+="\\SWS{"+module.getModuleFieldValue("SWS")+"}"+newLine;
		texString+=newLine;
		
		// lp
		texString+="\\ECTS{"+module.getModuleFieldValue("ECTS")+"}"+newLine;
		texString+=newLine;
		
		// language
		texString+="\\Sprache{"+module.getModuleFieldValue("Sprache")+"}"+newLine;
		texString+=newLine;
		
		// duration
		texString+="\\Moduldauer{"+module.getModuleFieldValue("Dauer")+"}"+newLine;
		texString+=newLine;
		
		// rotation
		texString+="\\Turnus{"+newLine;
		if((Boolean) module.getModuleFieldValue("periodisch")) texString+="\\periodisch{";
		else texString+="\\sporadisch{";
		texString+=module.getModuleFieldValue("Turnus")+"}"+newLine;
		texString+="}"+newLine;
		texString+=newLine;
		
		// lecturers
		texString+="\\Dozenten{"+newLine;
		for(String lecturer : module.getLecturers()) {
			texString+=adaptLecturer(lecturer)+newLine;
		}
		texString+="}"+newLine;
		texString+=newLine;
		
		// director
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
		texString+=adaptLists((String) module.getModuleFieldValue("Voraussetzungen (inhaltlich)"))+newLine;
		texString+="}"+newLine;
		texString+=newLine;
		
		// requirement_formal
		texString+="\\VoraussetzungenFormal{"+newLine;
		texString+=adaptLists((String) module.getModuleFieldValue("Voraussetzungen (formal)"))+newLine;
		texString+="}"+newLine;
		texString+=newLine;
		
		// learningTarget
		texString+="\\Lernziele{"+newLine;
		texString+=adaptLearningTarget((String) module.getModuleFieldValue("Lernziele"))+newLine;
		texString+="}"+newLine;
		texString+=newLine;
		
		// content
		texString+="\\Inhalt{"+newLine;
		texString+=adaptContent((String) module.getModuleFieldValue("Inhalt"))+newLine;
		texString+="}"+newLine;
		texString+=newLine;
		
		// literature
		texString+="\\Literatur{"+newLine;
		texString+=adaptLiterature((String) module.getModuleFieldValue("Literatur"))+newLine;
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
		int effort_presenceTime = (Integer) module.getModuleFieldValue("Präsenzzeit");
		texString+="\\Praesenzzeit{"+effort_presenceTime+"}"+newLine;
		// effort_preAndPost
		int effort_preAndPost = (Integer) module.getModuleFieldValue("Vor- und Nachbereitung");
		texString+="\\VorNachbereitung{"+effort_preAndPost+"}"+newLine;
		// sum
		texString+="\\Summe{"+(effort_presenceTime+effort_preAndPost)+"}"+newLine;
		texString+="}"+newLine;
		texString+=newLine;
		
		// performance_record
		texString+="\\Leistungsnachweis{"+newLine;
		texString+=module.getModuleFieldValue("Leistungsnachweis")+newLine;
		texString+="}"+newLine;
		texString+=newLine;
		
		// gradeFormation
		texString+="\\Notenbildung{"+newLine;
		texString+=module.getModuleFieldValue("Notenbildung")+newLine;
		texString+="}"+newLine;
		texString+=newLine;
		
		// basisFor
		if(module.getModuleFieldValue("Grundlage für") != null) {
			texString+="\\Grundlagen{"+newLine;
			texString+=module.getModuleFieldValue("Grundlage für")+newLine;
			texString+="}"+newLine;
			texString+=newLine;
		}

		// ilias
		if(module.getModuleFieldValue("Ilias") != null) {
			texString+="\\Ilias{"+newLine;
			texString+=module.getModuleFieldValue("Ilias")+newLine;
			texString+="}"+newLine;
			texString+=newLine;
		}
		
		return texString.trim();
	}

	/**
	 * @param lecturer
	 * @return the adapted string
	 */
	private String adaptLecturer(String lecturer) {
		UserDbController db = new UserDbController();
		User user = db.getUser(new User(lecturer));
		db.closeConnection();
		if(user != null) {
			String profession = user.getProfession();
			if(profession != null && !profession.equals("")) {
				// TODO Hochschullehrer, Honorarprofessor, Privatdozent, Gastprofessor oder Lehrbeauftragter
				if(profession.equals("Prof.")) profession = "Prof";
				else if(profession.equals("Jun.-Prof.")) profession = "JunProf";
				else if(profession.equals("Priv.-Doz.")) profession = "PD";
				else if(profession.equals("Lehrbeauftragter")) profession = "LB";
				else if(profession.equals("apl. Prof.")) profession = "aplProf";
			} else {
				// default profession is Prof
				profession = "Prof";
			}
			String title = user.getTitle();
			if(title != null) {
				return "\\"+profession+"{"+title+" "+user.getFirstName()+" "+user.getLastName()+"}";
			} else {
				return "\\"+profession+"{"+user.getFirstName()+" "+user.getLastName()+"}";
			}
		}
		return "\\Prof{"+lecturer+"}";
	}

	/**
	 * @param director_email
	 * @return the adapted string
	 */
	private String adaptDirector(String director_email) {
		GlobalVarDbController db = new GlobalVarDbController();
		String dekan;
		if((dekan = db.getGlobalVar("StudiendekanInf")) != null) {
			if(dekan.equals(director_email)) {
				db.closeConnection();
				return "\\StudiendekanInf";
			}
		}
		if((dekan = db.getGlobalVar("StudiendekanET")) != null) {
			if(dekan.equals(director_email)) {
				db.closeConnection();
				return "\\StudiendekanET";
			}
		}
		if((dekan = db.getGlobalVar("StudiendekanIST")) != null) {
			if(dekan.equals(director_email)) {
				db.closeConnection();
				return "\\StudiendekanIST";
			}
		}
		if((dekan = db.getGlobalVar("StudiendekanComm")) != null) {
			if(dekan.equals(director_email)) {
				db.closeConnection();
				return "\\StudiendekanComm";
			}
		}
		// no dekan
		db.closeConnection();
		return adaptLecturer(director_email);
	}

	/**
	 * @param moduleID
	 * @return the adapted string
	 */
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
			else if(type.equals("Pra"))	type = "Praktikum";
			else if(type.equals("PrjSem")) type = "Projektseminar";
			else if(type.equals("BaArb")) type = "Bachelorarbeit";
			else if(type.equals("MaArb")) type = "Masterarbeit";			
			
			teachingForm += newLine+"\\"+type+"{"+e.getName()+"}{"+adaptDirector(e.getLecturer_email())+"}";
		}
			
		return teachingForm.trim();
	}


	/**
	 * @param literature
	 * @return the adapted string
	 */
	private String adaptLiterature(String literature) {
		// TODO replace \buch etc
		return adaptLists(literature);
	}

	/**
	 * @param content
	 * @return the adapted string
	 */
	private String adaptContent(String content) {
		return adaptLists(content);
	}
	
	/**
	 * converts html list tags to valid tex list items
	 * 
	 * @param content
	 * @return the adapted string
	 */
	private String adaptLists(String content) {
		// remove <ul> and </ul>
		content = content.replace("<ul>", "");
		content = content.replace("</ul>", "");
		
		content = content.replaceAll("<li>(.*?)</li>\\s?", newLine+"\\\\spiegelstrich{$1}");
		
		return content.trim();
	}

	/**
	 * @param learningTarget
	 * @return the adapted string
	 */
	private String adaptLearningTarget(String learningTarget) {
		return adaptLists(learningTarget);
	}

	/**
	 * @param subjectID
	 * @return the adapted string
	 */
	private String adaptSubjectID(int subjectID) {
		ContentDbController db = new ContentDbController();
		
		// get Subject
		Subject subject = db.getSubject(subjectID);
		
		// get Studycourse
		Studycourse studycourse = db.getStudycourse(subject.getStudycourses_studycourseID());
		
		db.closeConnection();
		
		String studycourseName = studycourse.getName();
				
		if(studycourseName.equals("Informatik")) studycourseName = "Inf";
		else if(studycourseName.equals("Medieninformatik")) studycourseName = "MedInf";
		else if(studycourseName.equals("Software Engineering")) studycourseName = "SwEng";
		else if(studycourseName.equals("Informationssystemtechnik")) studycourseName = "IST";
		else if(studycourseName.equals("Elektrotechnik")) studycourseName = "ET";
		else if(studycourseName.equals("Communications Technology")) studycourseName = "Comm";
		else if(studycourseName.equals("Advanced Materials")) studycourseName = "AdvMat";
		else if(studycourseName.equals("Mathematik")) studycourseName = "Math";
		else if(studycourseName.equals("Chemieingenieurwesen")) studycourseName = "ChemIng";
		
		String studycourseGraduation = studycourse.getGraduation();
		
		if(studycourseGraduation.equals("Bachelor")) studycourseGraduation = "Ba";
		else if(studycourseGraduation.equals("Master")) studycourseGraduation = "Ma";
		else if(studycourseGraduation.equals("Lehramt")) studycourseGraduation = "La";
		
		String subjectName = subject.getName();
		if(subjectName.equals("Mediale Informatik")) subjectName = "\\MEI";
		else if(subjectName.equals("Praktische und Angewandte Informatik")) subjectName = "\\PAI";			
		else if(subjectName.equals("Technische und Systemnahe Informatik")) subjectName = "\\TSI";
		else if(subjectName.equals("Theoretische und Mathematische Methoden der Informatik")) subjectName = "\\TMI";
		else if(subjectName.equals("Medieninformatik")) subjectName = "\\Medieninformatik";
		else if(subjectName.equals("Mathematik")) subjectName = "\\Mathematik";
		else if(subjectName.equals("Angewandte Mathematik")) subjectName = "\\AngewandteMathematik";			
		else if(subjectName.equals("Software-Engineering")) subjectName = "\\SoftwareEngineering";	
		else if(subjectName.equals("Allgemeine Elektrotechnik")) subjectName = "\\AET";			
		else if(subjectName.equals("Ingenieurwissenschaften")) subjectName = "\\Ingwi";	
		else if(subjectName.equals("Automatisierungs- und Energietechnik")) subjectName = "\\AUT";
		else if(subjectName.equals("Communications Engineering")) subjectName = "\\CE";	
		else if(subjectName.equals("Komunikations- und Systemtechnik")) subjectName = "\\KUS";	
		else if(subjectName.equals("Mikroelektronik")) subjectName = "\\Mikro";
		
		return "\\"+studycourseName+"{\\"+studycourseGraduation+"}{\\"+subject.getType()+"}{"+subjectName+"}";
	}
}
