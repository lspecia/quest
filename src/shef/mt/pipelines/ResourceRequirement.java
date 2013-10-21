package shef.mt.pipelines;

import java.util.HashMap;

/**
 * Class that bundles together the name of a resource class and the required
 * parameters for its initialization. It is used by Processor subclasses, in 
 * order to specify which resources and which parameters each of them uses.
 * This refers to Resource objects that are common for many processors, 
 * so that they do not have to be initialized many times
 * 
 * @author Eleftherios Avramidis
 */
public class ResourceRequirement {
	private String resourceName;
	private HashMap<String, Object> parameters;
	
	/**
	 * Initialize the ResourceRequirement instance, by setting the name
	 * of the required resource. Most probably the initialization needs
	 * to be followed by one or more resource parameter specifications
	 * @param name The name of the required Resource object
	 */
	public ResourceRequirement(String name){
		resourceName = name;
		parameters = new HashMap<String, Object>();
	}
	
	/**
	 * Specify the required parameters and values that this Resource
	 * needs to be initialized with 
	 * @param name the name of the instance parameter
	 * @param value the value of the instance parameter
	 */
	public void addParameter(String name, Object value){
		parameters.put(name, value);
	}
	
	/**
	 * Return all the parameters and the respective values required
	 * for instantiating the referred Resource class
	 * @return a HashMap of the parameters
	 */
	public HashMap<String, Object> getParameters(){
		return parameters;
	}
	
	/**
	 * Return the name of the Resource
	 * @return the name of the resource
	 */
	public String getResourceName(){
		return resourceName;
	}
	
	
}
