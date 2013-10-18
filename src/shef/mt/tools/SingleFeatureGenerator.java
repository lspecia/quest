/**
 * 
 */
package shef.mt.tools;

import java.util.HashMap;
import shef.mt.features.util.Sentence;


/**
 * @author Eleftherios Avramidis
 *
 */
public abstract class SingleFeatureGenerator {
	
	public SingleFeatureGenerator(){
		featureDescriptions = new HashMap<Integer,String>();
		initFeatureDescriptions();
	}

	private HashMap<Integer,String> featureDescriptions;
	
	public HashMap<Integer,String> getFeatureDescriptions(){
		return featureDescriptions;
	}
	
	protected abstract HashMap<Integer,String> initFeatureDescriptions();
	
	public abstract HashMap<Integer,Object> getSourceFeatures(Sentence source);
	
	public abstract HashMap<Integer,Object> getTargetFeatures(Sentence target);
	
	public abstract HashMap<Integer,Object> getCombinedFeatures(Sentence source, Sentence target);
	
}
