/**
 *
 */
package shef.mt.tools;

import shef.mt.features.util.Sentence;
import shef.mt.util.PropertiesManager;
import shef.mt.features.util.FeatureManager;

/**
 * Abstract class that is the base class for all classes that process the output
 * files of pre-processing tools
 *
 * @author Catalina Hallett
 *
 */
public abstract class ResourceProcessor {
	private static String resourceName = new String();

    public abstract void processNextSentence(Sentence source);
    
    public abstract void initialize(PropertiesManager propertiesManager,
    								FeatureManager featureManager);
    
    public String getName(){
    	return this.resourceName;
    }
}
