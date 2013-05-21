package shef.mt.util;

public class GetLangAbbreviation {
	public String GetAbbreviation(String fullName) {
		
		String abbreviation = new String();
		
		if (fullName.equals("english")) {abbreviation = "en";}
		else if (fullName.equals("spanish")) {abbreviation = "es";}
		else if (fullName.equals("german")) {abbreviation = "de";}
		else if (fullName.equals("french")) {abbreviation = "fr";}
		else if (fullName.equals("italian")) {abbreviation = "it";}
		else if (fullName.equals("polish")) {abbreviation = "pl";} 
		else {System.out.println("Abbreviation of language name '"+fullName+"' not known!");}
		
		return abbreviation;
	}
}
