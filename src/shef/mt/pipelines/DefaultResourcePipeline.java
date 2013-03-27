/**
 * 
 */
package shef.mt.pipelines;
import shef.mt.tools.BParserProcessor;
import shef.mt.tools.TopicDistributionProcessor;
import shef.mt.util.PropertiesManager;



/**
 * This objects defines a default pipeline, with all available resource processors
 * in an order. New ResourceProcessor implementations should be added here. 
 * @date 28/02/2013
 * @author Eleftherios Avramidis
 */
public class DefaultResourcePipeline extends ResourcePipeline {

	/**
	 * Initialize here the ResourceProcessor instances and declare 
	 * them with this.addResourceProcessor(resourceInstance).
	 * When a new ResourceProcessor is added, it should be initialized and added in the local list here.
	 * TODO: This is a draft implementation, since the cleanest way would be the resource processor objects
	 * to be initialized within their own classes. But this requires modifying the interface and all the existing
	 * ResourceProcessor classes, so we will proceed the draft way. Future implementations are encouraged
	 * to do as much of initialization in the object classes, so transition to the clean models is smoother 
	 * (when/ if it happens). 
	 * @param sourceFile: file containing already preprocessed source sentences to be annotated
	 * @param targetFile: file containing already preprocessed target sentences to be annotated
	 * @param propertiesManager: initialized PropertiesManager object that contains all the parameters specified in the properties file
	 * @param sourceLang: source language name
	 * @param targetLang: target language name
	 */
	
	ArrayList resources;

	public DefaultResourcePipeline(sourceFile, targetFile, resourceManager, sourceLang, targetLang) {
		resources = new ArrayList<ResourceProcessor>();
		ResourceProcessor bParser = new BParser();
		//the same for TopicProcessor
		resources.add(bParser);
   		// store the parameters into private class variables so that thez can be used by initialize_resources of the superclass
	}	
	//Remove this function as it is going to be run in the superclass through a loop
	public void initialize_resources() {
	/*
	 * Berkeley Parser 
	 */
	BParserProcessor.initialize(String sourceFile, String targetFile, 
		PropertiesManager propertiesManager,
		String sourceLang, String targetLang);

        /*
         * Topic Model
         */
	TopicDistributionProcessor.initialize(String sourceFile, String targetFile, 
		PropertiesManager propertiesManager, 
		String sourceLang, String targetLang);
        }
}
