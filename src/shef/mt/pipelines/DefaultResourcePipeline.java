
package shef.mt.pipelines;
import java.util.ArrayList;

import shef.mt.tools.BParserProcessor;
import shef.mt.tools.MorphAnalysisProcessor;
import shef.mt.tools.NGramProcessor;
import shef.mt.tools.PPLProcessor;
import shef.mt.tools.ResourceProcessor;
import shef.mt.tools.TopicDistributionProcessor;
import shef.mt.tools.TriggersProcessor;
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
	 * @param propertiesManager: initialized PropertiesManager object that contains all the parameters specified in the properties file
	 * @param sourceLang: source language (e.g. 'english')
	 * @param targetLang: target language (e.g. 'spanish')
	 */
	public DefaultResourcePipeline(PropertiesManager propertiesManager, String sourceLang, String targetLang) {
		
		res = new ArrayList<ResourceProcessor>();
		ResourceProcessor bParser = new BParserProcessor();
		ResourceProcessor topicDistribution = new TopicDistributionProcessor();
		ResourceProcessor morphAnalysis = new MorphAnalysisProcessor();
		ResourceProcessor triggers = new TriggersProcessor();
		ResourceProcessor ngramProcessor = new NGramProcessor();
		ResourceProcessor pplProcessor = new PPLProcessor();
		
		res.add(bParser);
		res.add(topicDistribution);
		res.add(morphAnalysis);
		res.add(triggers);
		res.add(ngramProcessor);
		res.add(pplProcessor);
		initialize_resources(res, propertiesManager, sourceLang, targetLang);
   		// store the parameters into private class variables so that they can be used by initialize_resources of the superclass
	}
	
	public DefaultResourcePipeline() {}
}
