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

    public ArrayList<ResourceProcessor> resources = new ArrayList<ResourceProcessor>();

    /*public ResourcePipeline() {
        resources = new ArrayList<ResourceProcessor>();
    }*/

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
	 * @param propertiesManager: initialized PropertiesManager object that contains all the parameters specified in the properties file
	 * @param featureManager
     * */
    public void initialize_resources(ArrayList<ResourceProcessor> res, 
						    		 PropertiesManager propertiesManager, FeatureManager featureManager) {
    	for (ResourceProcessor resource:res){
        	resource.initialize(propertiesManager, featureManager);
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
        	System.out.println("ccc");
        	System.out.println(resourceNames.toString());
        	System.out.println(resources.toString());
        	if (resourceNames.contains(resource.getName())){
        		resource.processNextSentence(sent);
        	}
        }
    }
}
