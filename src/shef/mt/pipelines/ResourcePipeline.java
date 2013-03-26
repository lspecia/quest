/**
 *
 */
package shef.mt.pipelines;

import shef.mt.features.util.Sentence;
import shef.mt.tools.ResourceProcessor;

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
     * Run only resourceprocessors whose resourcename is specified in the list
     * This will work onl for ResourceProcessors that clearly define this.resourceName 
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
