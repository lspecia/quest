package shef.mt.tools.tok;

import java.util.HashMap;

import shef.mt.features.util.FeatureManager;
import shef.mt.features.util.Sentence;
import shef.mt.tools.ResourceProcessor;
import shef.mt.tools.SingleProcessor;
import shef.mt.util.PropertiesManager;

public class PunctFeatureGenerator extends SingleProcessor {


	@Override
	protected HashMap<Integer, String> getFeatureDescriptions() {
		HashMap<Integer,String> featureDescriptions = new HashMap<Integer,String>();		
		featureDescriptions.put(1001, "number of tokens in the source sentence");
		return featureDescriptions;
	}

	

	@Override
	public HashMap<Integer, Object> getSourceFeatures(Sentence source) {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public HashMap<Integer, Object> getTargetFeatures(Sentence target) {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public HashMap<Integer, Object> getCombinedFeatures(Sentence source,
			Sentence target) {
		
		HashMap<Integer,Object> features = new HashMap<Integer,Object>();
        features.put(1007, mismatchedTokens(source, target));
        features.put(1008, mismatchedQuotes(source, target));
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
	

}
