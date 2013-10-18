package shef.mt.tools.tok;

import java.util.HashMap;

import shef.mt.features.util.FeatureManager;
import shef.mt.features.util.Sentence;
import shef.mt.tools.ResourceProcessor;
import shef.mt.tools.SingleFeatureGenerator;
import shef.mt.util.PropertiesManager;

public class TokenFeatureGenerator extends SingleFeatureGenerator {

	private HashMap<Integer,String> featureDescriptions;
	
	@Override
	protected HashMap<Integer, String> initFeatureDescriptions() {
		featureDescriptions = new HashMap<Integer,String>();
		featureDescriptions.put(1001, "number of tokens in the source sentence");
		featureDescriptions.put(1002, "number of tokens in the target sentence");
		featureDescriptions.put(1003, "ratio of number of tokens in source and target");
		featureDescriptions.put(1004, "no tokens in the target / no tokens in the source");
		featureDescriptions.put(1005, "absolute difference between no tokens and source and target normalised by source length");
		featureDescriptions.put(1006, "average source token length");
		return featureDescriptions;
	}	
	
	
	@Override
	public HashMap<Integer,Object> getSourceFeatures(Sentence source){
		HashMap<Integer,Object> features = new HashMap<Integer,Object>();
        
        //number of tokens in the source sentence
        int sourceTok = source.getNoTokens();
		features.put(1001, sourceTok);
		
        //average source token length
        features.put(1006, avgTokenLength(source));		
        
		return features;	
	}
	

	@Override
	public HashMap<Integer, Object> getTargetFeatures(Sentence target) {
		HashMap<Integer,Object> features = new HashMap<Integer,Object>();
		
		//number of tokens in the target sentence
		int targetTok = target.getNoTokens();
		features.put(1002, targetTok);
		return features;	
	}
	

	@Override
	public HashMap<Integer, Object> getCombinedFeatures(Sentence source,
			Sentence target) {
		
		HashMap<Integer,Object> features = new HashMap<Integer,Object>();
        int sourceTok = source.getNoTokens();
		int targetTok = target.getNoTokens();

		//ratio of number of tokens in source and target
		features.put(1003, ratio(sourceTok, targetTok));
		
		//no tokens in the target / no tokens in the source
		features.put(1004, ratio(targetTok, sourceTok)); 

		//absolute difference between no tokens and source and target normalised by source length
        features.put(1005, ratio(Math.abs(targetTok - sourceTok), sourceTok));
		
		return null;
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

}
