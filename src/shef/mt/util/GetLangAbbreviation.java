package shef.mt.util;

public class GetLangAbbreviation {
	public String GetAbbreviation(String fullName) {
		
		String abbreviation = new String();
		
		if (fullName == "english") {abbreviation = "en";}
		else if (fullName == "spanish") {abbreviation = "es";}
		else if (fullName == "german") {abbreviation = "de";}
		else if (fullName == "french") {abbreviation = "fr";}
		else if (fullName == "italian") {abbreviation = "it";}
		else if (fullName == "polish") {abbreviation = "pl";}
		else {System.out.println("Abbreviation of language name '"+fullName+"' not known!");}
		
		return abbreviation;
	}
}
