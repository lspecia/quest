/**
 * 
 */
package shef.mt.pipelines;
import java.util.ArrayList;

import shef.mt.tools.BParserProcessor;
import shef.mt.tools.ResourceProcessor;
import shef.mt.tools.TopicDistributionProcessor;
import shef.mt.tools.MorphAnalysisProcessor;
import shef.mt.util.PropertiesManager;



/**
 * This objects defines a default pipeline, with all available resource processors
 * in an order. New ResourceProcessor implementations should be added here. 
 * @date 28/02/2013
 * @author Eleftherios Avramidis
 */
public class DefaultResourcePipeline extends ResourcePipeline {

	ArrayList<ResourceProcessor> res;
	
	/**
	 * Creates resource processor objects that should be initialized.
	 * When a new ResourceProcessor is added, it should be initialized and added in the local list here
	 *  
	 * @param sourceFile: file containing already preprocessed source sentences to be annotated
	 * @param targetFile: file containing already preprocessed target sentences to be annotated
	 * @param propertiesManager: initialized PropertiesManager object that contains all the parameters specified in the properties file
	 * @param sourceLang: source language name
	 * @param targetLang: target language name
	 */
	public DefaultResourcePipeline(String sourceFile, String targetFile,
			PropertiesManager propertiesManager,
			String sourceLang, String targetLang) {
		
		res = new ArrayList<ResourceProcessor>();
		ResourceProcessor bParser = new BParserProcessor();
		ResourceProcessor topicDistribution = new TopicDistributionProcessor();
		ResourceProcessor morphAnalysis = new MorphAnalysisProcessor();
		res.add(bParser);
		res.add(topicDistribution);
		res.add(morphAnalysis);
		initialize_resources(res, sourceFile, targetFile, propertiesManager, sourceLang, targetLang);
   		// store the parameters into private class variables so that they can be used by initialize_resources of the superclass
	}
}
