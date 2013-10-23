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
 import shef.mt.tools.Processor;
 import shef.mt.tools.TopicDistributionProcessor;
 import shef.mt.tools.TriggersProcessor;
 */
import shef.mt.util.PropertiesManager;

public class DynamicPipeline extends ProcessorPipeline {

	ArrayList<Processor> matchedResources = new ArrayList<Processor>();

	public DynamicPipeline(PropertiesManager propertiesManager, FeatureManager featureManager) {

		Set<String> requiredResourceNames = featureManager.getFeatureResources();
		
		// initialization of all existing processors automatically
		ArrayList<Processor> Processors = new ArrayList<Processor>();
		Reflections reflections = new Reflections("shef.mt.tools");
		Set<Class<? extends Processor>> subTypes = reflections
				.getSubTypesOf(Processor.class);

		// for loop iterates through all classes that are extended by Processor
		for (Class<? extends Processor> subType : subTypes) {
			// if condition excludes abstract classes
			if (!Modifier.isAbstract(subType.getModifiers())) {				
				try {
					// initialize the class and add into ArrayList
					Processors.add(subType.newInstance());
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		for (Processor processor : Processors) {
			String resourceName = processor.getClass().getName();
			if (requiredResourceNames.contains(resourceName)) {
				matchedResources.add(processor);
			}
		}
		initialize_processors(matchedResources, propertiesManager, featureManager);

		/**
		 * Processor bParser = new BParserProcessor(); Processor
		 * topicDistribution = new TopicDistributionProcessor();
		 * Processor morphAnalysis = new MorphAnalysisProcessor();
		 * Processor triggers = new TriggersProcessor();
		 * Processor ngramProcessor = new NGramProcessor();
		 * Processor pplProcessor = new PPLProcessor();
		 * Processor posProcessor = new POSProcessor();
		 * 
		 * Processors.add(bParser);
		 * Processors.add(topicDistribution);
		 * Processors.add(morphAnalysis);
		 * Processors.add(triggers);
		 * Processors.add(ngramProcessor);
		 * Processors.add(pplProcessor);
		 * Processors.add(posProcessor);
		 * //Processors.add(mtOutputProcessor);
		 * //Processors.add(nerProcessor);
		 */
	}
}