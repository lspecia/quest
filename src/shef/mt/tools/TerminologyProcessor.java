package shef.mt.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;

import shef.mt.features.util.FeatureManager;
import shef.mt.features.util.Sentence;
import shef.mt.util.PropertiesManager;

public class TerminologyProcessor extends ResourceProcessor {
	
	private HashSet<String> termLexicon;

	public TerminologyProcessor() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void processNextSentence(Sentence sentence) {
		// TODO Auto-generated method stub
		int value = 0;
		String[] sentence_text = sentence.getTokens();
            
		for (String token:sentence_text){
			if (termLexicon.contains(token)){
				value++;
			}
			
		}
                sentence.setValue("terminology", value);
		
	}

	public void initialize(PropertiesManager propertiesManager,
			FeatureManager featureManager) {
		// TODO Auto-generated method stub
		String filename = propertiesManager.getProperty("terminology.batchfile");
		FileInputStream fstream;
		
		termLexicon = new HashSet<String>(); 
		
		try {
			fstream = new FileInputStream(filename);
				
			
			BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
	
			String strLine;
	
			//Read File Line By Line
			while ((strLine = br.readLine()) != null)   {
				
				termLexicon.add(strLine);
			}
	
			//Close the input stream
			br.close();
		
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	

	}

}
