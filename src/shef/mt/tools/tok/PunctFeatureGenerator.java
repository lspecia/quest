package shef.mt.tools.tok;

import java.util.ArrayList;
import java.util.HashMap;

import shef.mt.features.util.FeatureManager;
import shef.mt.features.util.ParallelSentence;
import shef.mt.features.util.Sentence;
import shef.mt.tools.BilingualProcessor;
import shef.mt.tools.Resource;
import shef.mt.tools.ResourceProcessor;
import shef.mt.tools.SingleProcessor;
import shef.mt.util.PropertiesManager;

public class PunctFeatureGenerator extends BilingualProcessor {


	@Override
	public HashMap<String, String> getFeatureDescriptions() {
		HashMap<String,String> featureDescriptions = new HashMap<String,String>();		
		featureDescriptions.put("1001", "number of tokens in the source sentence");
		return featureDescriptions;
	}

	

	@Override
	public HashMap<String, Object> getSourceFeatures(Sentence source) {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public HashMap<String, Object> getTargetFeatures(Sentence target) {
		// TODO Auto-generated method stub
		return null;
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
	
	private float ratio(int tok1, int tok2){
		float ratio;
		if (tok2 == 0)
	        ratio = 0;
	    else
	        ratio = (float) tok1 / tok2;
		return ratio;
	}



	@Override
	public HashMap<String, Object> getParallelFeatures(Sentence sourceSentence,
			Sentence targetSentence, ParallelSentence parallelsentence) {
		HashMap<String,Object> features = new HashMap<String,Object>();
        features.put("1007", mismatchedTokens(sourceSentence, targetSentence));
        features.put("1008", mismatchedQuotes(sourceSentence, targetSentence));
        return features;
	}



	@Override
	public void initialize(HashMap<String, Resource> initializedResources) {
		// TODO Auto-generated method stub
		
	}



	@Override
	protected HashMap<String, String> defineFeatureDescriptions() {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	protected ArrayList<String> defineRequiredProcessors() {
		// TODO Auto-generated method stub
		return null;
	}
	

}
