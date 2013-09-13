package shef.mt.pipelines;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Set;

import org.reflections.Reflections;

import shef.mt.features.util.FeatureManager;
import shef.mt.tools.*;
/**
 import shef.mt.tools.MorphAnalysisProcessor;
 import shef.mt.tools.NGramProcessor;
 import shef.mt.tools.POSProcessor;
 import shef.mt.tools.PPLProcessor;
 import shef.mt.tools.ResourceProcessor;
 import shef.mt.tools.TopicDistributionProcessor;
 import shef.mt.tools.TriggersProcessor;
 */
import shef.mt.util.PropertiesManager;

public class DynamicPipeline extends ResourcePipeline {

	ArrayList<ResourceProcessor> matchedResources = new ArrayList<ResourceProcessor>();

	public DynamicPipeline(PropertiesManager propertiesManager, FeatureManager featureManager) {

		Set<String> requiredResourceNames = featureManager.getFeatureResources();
		
		// initialization of all existing processors automatically
		ArrayList<ResourceProcessor> resourceProcessors = new ArrayList<ResourceProcessor>();
		Reflections reflections = new Reflections("shef.mt.tools");
		Set<Class<? extends ResourceProcessor>> subTypes = reflections
				.getSubTypesOf(ResourceProcessor.class);

		// for loop iterates through all classes that are extended by ResourceProcessor
		for (Class<? extends ResourceProcessor> subType : subTypes) {
			// if condition excludes abstract classes
			if (!Modifier.isAbstract(subType.getModifiers())) {				
				try {
					// initialize the class and add into ArrayList
					resourceProcessors.add(subType.newInstance());
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		System.err.println("Available processors: " + resourceProcessors);
		for (ResourceProcessor resourceProcessor : resourceProcessors) {
			String resourceName = resourceProcessor.getName();
			if (requiredResourceNames.contains(resourceName)) {
				matchedResources.add(resourceProcessor);
			} else {
				System.err.println("Resource "+resourceName+" is not going to be run because it is not needed");
			}
		}
		System.err.println("Resources that will run: "+ matchedResources);
		initialize_resources(matchedResources, propertiesManager, featureManager);

		/**
		 * ResourceProcessor bParser = new BParserProcessor(); ResourceProcessor
		 * topicDistribution = new TopicDistributionProcessor();
		 * ResourceProcessor morphAnalysis = new MorphAnalysisProcessor();
		 * ResourceProcessor triggers = new TriggersProcessor();
		 * ResourceProcessor ngramProcessor = new NGramProcessor();
		 * ResourceProcessor pplProcessor = new PPLProcessor();
		 * ResourceProcessor posProcessor = new POSProcessor();
		 * 
		 * resourceProcessors.add(bParser);
		 * resourceProcessors.add(topicDistribution);
		 * resourceProcessors.add(morphAnalysis);
		 * resourceProcessors.add(triggers);
		 * resourceProcessors.add(ngramProcessor);
		 * resourceProcessors.add(pplProcessor);
		 * resourceProcessors.add(posProcessor);
		 * //resourceProcessors.add(mtOutputProcessor);
		 * //resourceProcessors.add(nerProcessor);
		 */
	}
}