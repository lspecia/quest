/**
 *
 */
package shef.mt.tools;

import shef.mt.features.util.Sentence;
import shef.mt.util.PropertiesManager;

/**
 * Abstract class that is the base class for all classes that process the output
 * files of pre-processing tools
 *
 * @author Catalina Hallett
 *
 */
public abstract class ResourceProcessor {
	private static String resourceName;

    public abstract void processNextSentence(Sentence source);
    
    public abstract void initialize(String sourceFile, String targetFile,
			PropertiesManager propertiesManager,
			String sourceLang, String targetLang);
    
    public String getName(){
    	return this.resourceName;
    }
}
