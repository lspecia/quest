/**
 * 
 */
package shef.mt.tools;

import java.util.HashMap;

import shef.mt.features.util.FeatureManager;
import shef.mt.features.util.ParallelSentence;
import shef.mt.features.util.Sentence;
import shef.mt.util.PropertiesManager;


/**
 * This abstract class describes the functionality of a processor which
 * only operates on one language side: it analyzes a sentence (either
 * source or target) and generates features only for this, without
 * looking on the other language. It is intended to be used for 
 * tokenizers, parsers etc. 
 * 
 * @author Eleftherios Avramidis
 *
 */
public abstract class SingleProcessor extends Processor {
	
	protected String language;
	
	/**
	 * Initialize processor, by passing the object carrying the configuration 
	 * settings and the language name that this processor will be linked with.
	 * @param propertiesManager
	 * @param lang
	 */
	public SingleProcessor(PropertiesManager propertiesManager, String lang){
		super(propertiesManager);
		
		language = lang;
	}
	
	/**
	 * Provide the language name that this processor can operate on
	 * @return a language code
	 */
	public String getLanguage(){
		return language;
	}

	/**
	 * This method can be optionally overridden, if this class 
	 * needs to modify the sentence object, further than just adding some features
	 * This may refer to preprocessing tasks, such as a tokenizer
	 * or a truecaser, which may want to modify sentence text, etc.
	 * @param sentence The sentence object that needs to be modified
	 * @return a modified sentence object
	 */
	public Sentence processSentence(Sentence sentence){
		return sentence;
	}
	
	/**
	 * This is a method that has to be implemented by the subclasses, as
	 * it analyzes the given sentence and provides a hashmap of features 
	 * keys and values
	 * @param sentence The sentence object that will be analyzed 
	 * @return a hashmap of features produced out of the analysis. HashMap keys
	 * should map the definition given in defineFeatureDescriptions
	 */
	public abstract HashMap<String,Object> getFeatures(Sentence sentence);
	
	
}
