package shef.mt.tools;

import java.util.ArrayList;
import java.util.HashMap;

import shef.mt.pipelines.ResourceRequirement;
import shef.mt.util.PropertiesManager;

/**
 * @author elav01
 *
 */
public abstract class Processor {

	protected HashMap<String,String> featureDescriptions;
	protected ArrayList<ResourceRequirement> resourceRequirements;
	protected ArrayList<String> requiredProcessors;
	protected PropertiesManager propertiesManager;
	
	/**
	 * Initialize the processor, by reading some structural properties
	 * defined by particular methods of the subclasses. In particular,
	 * we are setting here the descriptions of the features, a list of
	 * the required resources that will be used by this processor and 
	 * a list of the required processors that need to have run beforehand
	 * @param propertiesManager an object that contains all the configuration settings by the user   
	 */
	public Processor(PropertiesManager propertiesMan){
		propertiesManager = propertiesMan;
		featureDescriptions = defineFeatureDescriptions();
		if (featureDescriptions == null){
			featureDescriptions = new HashMap<String,String>();
		}
		resourceRequirements = defineRequiredResources();
		if (resourceRequirements == null){
			resourceRequirements = new ArrayList<ResourceRequirement>();
		}
		requiredProcessors = defineRequiredProcessors();
		if (requiredProcessors == null){
			requiredProcessors = new ArrayList<String>();
		}
	}
	
	/**
	 * Return the value of the protected map of feature descriptions in 
	 * a public way
	 * @return a hash-map of feature keys and descriptions
	 */
	public HashMap<String,String> getFeatureDescriptions(){
		return featureDescriptions;
	}
	
	/**
	 * Return the value of the protected list of required resources
	 * @return a list of required resources
	 */
	public ArrayList<ResourceRequirement> getRequiredResources(){
		return resourceRequirements;
	}
	
	/**
	 * Return the value of the protected list of required preprocessors
	 * @return a list of required resources
	 */
	public ArrayList<String> getRequiredProcessors(){
		return requiredProcessors;
	}
	
	
	/**
	 * This method is fired in the initialization (early) phase of the annotation
	 * process and allows the processor to load the necessary data and resources
	 * that will be used for extracting particular sentences.  
	 *  
	 * @param resources required resources indexed by their unique name
	 */
	public abstract void initialize(HashMap<String, Resource> initializedResources);
	
	/**
	 * It is a method that needs to be implemented by all subclasses, which 
	 * should provide a description of all features resulting from this processing 
	 * task
	 * @return a unique key and a description for each generated feature
	 */
	protected abstract HashMap<String,String> defineFeatureDescriptions();
	
	
	/**
	 * It is an method that can be optionally overridden by subclasses, which 
	 * may provide their requirements on Resources that may be shared 
	 * with other processors, and the exact parameterization of each of them. 
	 * @return a list of objects that specify the name and the parameters of each resource 
	 */
	protected ArrayList<ResourceRequirement> defineRequiredResources(){
		ArrayList<ResourceRequirement> requiredResources = new ArrayList<ResourceRequirement>();
		return requiredResources;
	}

	/**
	 * It is a method that needs to be implemented by all subclasses, which 
	 * should provide their requirements from previous Processors that may need
	 * to run before the current processor.  
	 * @return a list of objects that specify the name of the required processors 
	 */
	protected abstract ArrayList<String> defineRequiredProcessors();

}