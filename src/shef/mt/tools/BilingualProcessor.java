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
 * operates on two language sides: it analyzes a sentence (both
 * source or target) and generates features for both. 
 * It is intended to be used for dictionaries, IBM-models etc.
 * 
 * @author Eleftherios Avramidis
 *
 */
public abstract class BilingualProcessor extends Processor {
	
	private String sourceLanguage;
	private String targetLanguage;
	
	/**
	 * Initialize processor, by passing the object carrying the configuration 
	 * settings and the language names that this processor will be operating on.
	 * @param propertiesManager
	 * @param sourceLang source language name 
	 * @param targetLang target language name
	 */
	public BilingualProcessor(PropertiesManager propertiesManager, String sourceLang, String targetLang){
		super(propertiesManager);

		sourceLanguage = sourceLang;
		targetLanguage = targetLang;
		
	}
	
	/**
	 * Provide the source language name that this processor can operate on
	 * @return a language code
	 */
	public String getSourceLanguage(){
		return sourceLanguage;
	}
	
	/**
	 * Provide the target language name that this processor can operate on
	 * @return a language code
	 */
	public String getTargetLanguage(){
		return targetLanguage;
	}
		

	/**
	 * This method can be optionally overridden, if this class 
	 * needs to modify the source sentence object, further than just adding some features
	 * This may refer to preprocessing tasks, such as a tokenizer
	 * or a truecaser, which may want to modify sentence text, etc.
	 * @param sourceSentence The sentence object that needs to be modified
	 * @param parallelSentence an object containing all other sentences (target sentences, features, etc) that are mapped to the current source sentence
	 * @return a modified sentence object
	 */
	public Sentence processSourceSentence(Sentence sourceSentence, ParallelSentence parallelSentence){
		return sourceSentence;	
	}
	

	/**
	 * This method can be optionally overridden, if this class 
	 * needs to modify the target sentence object, further than just adding some features
	 * This may refer to preprocessing tasks, such as a tokenizer
	 * or a truecaser, which may want to modify sentence text, etc.
	 * @param sentence The sentence object that needs to be modified
	 * @param parallelSentence an object containing all other sentences (source sentences, features, etc) that are mapped to the current source sentence
	 * @return a modified sentence object
	 */
	public Sentence processTargetSentence(Sentence targetSentence, ParallelSentence parallelSentence){
		return targetSentence;
	}
	
	
	
	/**
	 * This is a method that may be overridden by the subclasses, as
	 * it analyzes the given source sentence and provides a hashmap of source features 
	 * keys and values
	 * @param sourceSentence The sentence object that will be analyzed 
	 * @return a hashmap of features produced out of the analysis. HashMap keys
	 * should map the definition given in defineFeatureDescriptions
	 */
	public HashMap<String,Object> getSourceFeatures(Sentence sourceSentence){
		return new HashMap<String,Object>();
	}
	
	public HashMap<String,Object> getTargetFeatures(Sentence targetSentence){
		return new HashMap<String,Object>();
	}

	/**
	 * This is a method that has to be implemented by the subclasses, as
	 * it analyzes the given source and target sentences and provides a hashmap of features 
	 * keys and values
	 * @param sourceSentence The sentence object that will be analyzed
	 * @param targetSentence A parallelsentence object containing target sentences 
	 * @return a hashmap of features produced out of the analysis. HashMap keys
	 * should map the definition given in defineFeatureDescriptions
	 */
	public abstract HashMap<String,Object> getParallelFeatures(Sentence sourceSentence, Sentence targetSentence);
	
}
