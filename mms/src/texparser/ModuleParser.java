package texparser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import controller.ContentDbController;

import model.content.Module;
import model.content.Subject;

public class ModuleParser {
	
	/**
	 * @param module
	 * @return a string representation of the module in tex syntax
	 */
	public String convertToTex(Module module) {
		String texString = "";
		String newLine = System.getProperty("line.separator");
		
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
		texString+="\\Modulverantwortlicher{"+module.getDirector_email()+"}"+newLine;
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
		texString+=adaptLearningTarget(module.getLearningTarget(), newLine)+newLine;
		texString+="}"+newLine;
		texString+=newLine;
		
		// content
		texString+="\\Inhalt{"+newLine;
		texString+=adaptContent(module.getContent(), newLine)+newLine;
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

	private String adaptTeachingForm(int id) {
		// TODO get Event names and types
		return null;
	}

	private String adaptLiterature(String literature) {
		// TODO replace \buch etc
		return literature;
	}

	private String adaptContent(String content, String newLine) {
		
		// remove <ul> and </ul>
		content = content.replace("<ul>", "");
		content = content.replace("</ul>", "");
		
		content = content.replaceAll("<li>(.*?)</li>\\s?", newLine+"\\\\spiegelstrich{$1}");
		
		return content;
	}

	private String adaptLearningTarget(String learningTarget, String newLine) {
		learningTarget = adaptContent(learningTarget, newLine);
		return learningTarget;
	}

	private String adaptSubjectID(int subjectID) {
		// TODO adapt to model: \Inf{\Ba} ...
		ContentDbController db = new ContentDbController();
		
		Subject subject = db.getSubject(subjectID);
		
		
		
		db.closeConnection();
		
		return "TODO "+subjectID;
	}
}
