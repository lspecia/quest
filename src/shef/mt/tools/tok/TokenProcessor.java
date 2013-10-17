package shef.mt.tools.tok;

import java.util.HashMap;

import shef.mt.features.util.FeatureManager;
import shef.mt.features.util.Sentence;
import shef.mt.tools.ResourceProcessor;
import shef.mt.util.PropertiesManager;

public class TokenProcessor extends ResourceProcessor {

	public TokenProcessor() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void processNextSentence(Sentence source) {
		// TODO Auto-generated method stub

	}
	
	public HashMap<Integer,String> getFeatureDescriptions(){
		HashMap<Integer,String> featureDescriptions = new <Integer,Object>HashMap();
		
		featureDescriptions.put(1001, "number of tokens in the source sentence");
		featureDescriptions.put(1002, "number of tokens in the target sentence");
		featureDescriptions.put(1003, "ratio of number of tokens in source and target");
		featureDescriptions.put(1004, "no tokens in the target / no tokens in the source");
		featureDescriptions.put(1005, "absolute difference between no tokens and source and target normalised by source length");
		return featureDescriptions;
	}
	
	
	private float ratio(int tok1, int tok2){
		float ratio;
		if (tok2 == 0)
	        ratio = 0;
	    else
	        ratio = (float) tok1 / tok2;
		return ratio;
	}
	
	
	private float avgTokenLength (Sentence sentence) {
		String[] tokens = sentence.getTokens();
        int noChars = 0;
        int noTokens = tokens.length;
        for (String token : tokens) {
            noChars += token.length();
        }
        return (float) noChars / noTokens;
	}
	
	
	private int mismatchedTokens(Sentence source, Sentence target) {
        int sqPlus = 0;
        int roundPlus = 0;
        int curlyPlus = 0;

        String[] result = target.getText().split("^\\[\\]\\(\\)\\{\\}");
        for (String crt : result) {
            if (crt.equals("[")) {
                sqPlus++;
            } else if (crt.equals("]")) {
                sqPlus--;
            } else if (crt.equals("(")) {
                roundPlus++;
            } else if (crt.equals(")")) {
                roundPlus--;
            } else if (crt.equals("{")) {
                curlyPlus++;
            } else if (crt.equals("}")) {
                curlyPlus--;
            }
        }
        return sqPlus + roundPlus + curlyPlus;
    } 
	
	
	private int mismatchedQuotes(Sentence source, Sentence target) {
        // TODO Auto-generated method stub
        int doubleQ = 0;
        int singleQ = 0;

        String[] result = target.getText().split("^\"\'");
        for (String crt : result) {
            if (crt.equals("\"")) {
                doubleQ++;
            } else if (crt.equals("\'")) {
                singleQ++;
            }
        }
        if ((doubleQ % 2 == 0) && (singleQ % 2 == 0)) {
            return 0;
        } else {
            return 1;
        }
    }
	
	
	public HashMap<Integer,Object> getFeatures(Sentence source, Sentence target){
		HashMap<Integer,Object> features = new HashMap<Integer,Object>();
        
        //number of tokens in the source sentence
        int sourceTok = source.getNoTokens();
		features.put(1001, sourceTok);
		
		//number of tokens in the target sentence
		int targetTok = target.getNoTokens();
		features.put(1002, targetTok);
		
		//ratio of number of tokens in source and target
		features.put(1003, ratio(sourceTok, targetTok));
		
		//no tokens in the target / no tokens in the source
		features.put(1004, ratio(targetTok, sourceTok)); 

		//absolute difference between no tokens and source and target normalised by source length
        features.put(1005, ratio(Math.abs(targetTok - sourceTok), sourceTok));
		
        features.put(1006, avgTokenLength(source));
        
        features.put(1007, mismatchedTokens(source, target));
        
		
		return features;
		
		
	}

	@Override
	public void initialize(PropertiesManager propertiesManager,
			FeatureManager featureManager) {
		// TODO Auto-generated method stub

	}

}
