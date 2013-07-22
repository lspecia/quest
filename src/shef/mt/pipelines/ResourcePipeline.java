/**
 *
 */
package shef.mt.pipelines;

import shef.mt.features.util.Sentence;
import shef.mt.tools.ResourceProcessor;
import shef.mt.util.PropertiesManager;
import shef.mt.features.util.FeatureManager;

import java.util.*;

/**
 * @author Catalina Hallett
 *
 */
public class ResourcePipeline {

    private ArrayList<ResourceProcessor> resources;

    public ResourcePipeline() {
        resources = new ArrayList<ResourceProcessor>();
    }

    public void addResourceProcessor(ResourceProcessor proc) {
        resources.add(proc);
    }

    public void processSentence(Sentence sent) {
        for (ResourceProcessor resource:resources){ 
            resource.processNextSentence(sent);
        }
    }
    
    /**
     * Initialize resources that are listed in the variable res.
     * @param res: resources to be initialized
     * @param sourceFile: file containing already preprocessed source sentences to be annotated
	 * @param targetFile: file containing already preprocessed target sentences to be annotated
	 * @param propertiesManager: initialized PropertiesManager object that contains all the parameters specified in the properties file
	 * @param sourceLang: source language name
	 * @param targetLang: target language name
	 * @param sourceFile: 
	 * @param targetFile: 
	 * @param forceRun: 
     * */
    public void initialize_resources(ArrayList<ResourceProcessor> res, 
						    		 PropertiesManager propertiesManager, FeatureManager featureManager, String sourceLang, String targetLang,
									 String sourceFile, String targetFile, boolean forceRun) {
    	for (ResourceProcessor resource:res){ 
        	resource.initialize(propertiesManager, featureManager, sourceLang, targetLang, sourceFile, targetFile, forceRun);
        	}
    }
    
    /**
     * Run only resourceprocessors whose resourcename is specified in the list
     * This will work only for ResourceProcessors that clearly define this.resourceName 
     * as a class variable
     * @param sent
     * @param resourceNames
     */
    public void processSentence(Sentence sent, Set<String> resourceNames) {
        for (ResourceProcessor resource:resources){ 
        	if (resourceNames.contains(resource.getName())){
        		resource.processNextSentence(sent);
        	}
        }
    }

    
    
}
