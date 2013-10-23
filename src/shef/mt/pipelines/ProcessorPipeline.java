/**
 *
 */
package shef.mt.pipelines;

import shef.mt.features.util.ParallelSentence;
import shef.mt.features.util.Sentence;
import shef.mt.tools.BilingualProcessor;
import shef.mt.tools.Processor;
import shef.mt.tools.Resource;
import shef.mt.tools.SingleProcessor;
import shef.mt.util.PropertiesManager;
import shef.mt.features.util.FeatureManager;

import java.util.*;

/**
 * @author Eleftherios Avramidis
 *
 */
public class ProcessorPipeline implements Cloneable {

    protected ArrayList<Processor> processors = new ArrayList<Processor>();

    /**
     * Let the pipeline keep only the processors that implement the given features
     * @param requiredFeatures the feature names, for which we want the processors
     */
    public void restrictToFeatures(Set<String> requiredFeatures){
    	Set<Processor> filteredProcessors = new HashSet<Processor>(); 
    	for (Processor processor:processors){
    		Set<String> processorFeatures = processor.getFeatureDescriptions().keySet();
    		//get the intersection of the required features and
    		//the features implemented by this processor
    		Set<String> intersection = new HashSet<String>(requiredFeatures);
    		intersection.retainAll(processorFeatures);	
    		//if features intersect, this processor implements some of the required features
    		if (!intersection.isEmpty()){
    			filteredProcessors.add(processor);
    		}
    	
		}
    	processors = new ArrayList<Processor>(filteredProcessors);

    	//display error message if no processor was left
    	if (processors.size()==0){
			System.err.println("None of the features you requested can be implemented by known processors. Please add processors to the pipeline or check the declared features");
			System.exit(1);
		}
	}

    public void initialize_resources(){
    	
    }
   

    public void addProcessor(Processor proc) {
        processors.add(proc);
    }
    
    public ArrayList<Processor> getProcessors(){
    	return processors;    	
    }

    
//    public void prepare(PropertiesManager propertiesMan, String sourceLang, String targetLang){
//    	
//    	ArrayList<Processor> modifiedProcessors = new ArrayList<Processor>();
//    	for (Processor processor:processors){
//    		if (processor instanceof SingleProcessor){
//    			//if it is a monolingual processor, create one instance for the source and one for the target
//    			SingleProcessor sourceProcessor = (SingleProcessor) processor;
//    			sourceProcessor.prepare(propertiesMan, sourceLang);
//    			modifiedProcessors.add(sourceProcessor);
//    			
//    			SingleProcessor targetProcessor = (SingleProcessor) processor.clone();
//    			targetProcessor.prepare(propertiesMan, targetLang);
//    			modifiedProcessors.add(targetProcessor);
//    		} else if (processor instanceof BilingualProcessor){
//    			BilingualProcessor biProcessor = (BilingualProcessor) processor;
//    			biProcessor.prepare(propertiesMan, sourceLang, targetLang);
//    			modifiedProcessors.add(biProcessor);
//    		}
//    		
//    	}
//    	processors = modifiedProcessors;
//    	
//    }
    
    
    
    public void initialize(HashMap<String, Resource> initializedResources){
    	for (Processor processor:processors){
    		processor.initialize(initializedResources);    		
    	}
    }
    
    
    
    
    public ParallelSentence processSentence(ParallelSentence parallelsentence, String sourceLang, String targetLang) {
    	ParallelSentence processedParallelSentence = parallelsentence;
    	
    	for (Processor processor:processors){
    		
    		Sentence sourceSentence = parallelsentence.getSource();
			if (processor instanceof SingleProcessor){
				//if it is a monolingual processor, create one instance for the source and one for the target
				SingleProcessor singleprocessor = (SingleProcessor) processor;
				if (singleprocessor.getLanguage() == sourceLang){
					sourceSentence = singleprocessor.processSentence(sourceSentence);
					sourceSentence.setValues(singleprocessor.getFeatures(sourceSentence));
					//create a copy of the existing parallelsentence by modifying the source
					processedParallelSentence = new ParallelSentence(sourceSentence, parallelsentence.getTargetSentences(), parallelsentence.getAttributes());
					
				} else if (singleprocessor.getLanguage() == targetLang){
					ArrayList<Sentence> processedTargetSentences = new ArrayList<Sentence>();
					for (Sentence targetSentence:parallelsentence.getTargetSentences()){
						targetSentence = singleprocessor.processSentence(targetSentence);
						targetSentence.setValues(singleprocessor.getFeatures(targetSentence));
						processedTargetSentences.add(targetSentence);
					}
					//create a copy of the existing parallelsentence by modifying the targets
					processedParallelSentence = new ParallelSentence(parallelsentence.getSource(), processedTargetSentences, parallelsentence.getAttributes());

				}
				
			} else if (processor instanceof BilingualProcessor){
				BilingualProcessor biProcessor = (BilingualProcessor) processor;
				sourceSentence = biProcessor.processSourceSentence(sourceSentence, parallelsentence);
				sourceSentence.setValues(biProcessor.getSourceFeatures(sourceSentence));
				ArrayList<Sentence> processedTargetSentences = new ArrayList<Sentence>();

				for (Sentence targetSentence:parallelsentence.getTargetSentences()){
					targetSentence = biProcessor.processTargetSentence(targetSentence, parallelsentence);
					targetSentence.setValues(biProcessor.getTargetFeatures(targetSentence));
					targetSentence.setValues(biProcessor.getParallelFeatures(sourceSentence, targetSentence, parallelsentence));
					processedTargetSentences.add(targetSentence);
				}
				processedParallelSentence = new ParallelSentence(sourceSentence, processedTargetSentences, parallelsentence.getAttributes());
			}
				
	
    	}
		return processedParallelSentence;
    }

    
    /**
     * Run only Processors whose resourcename is specified in the list
     * This will work only for Processors that clearly define this.resourceName 
     * as a class variable
     * @param sent
     * @param resourceNames
     */
//    public void processSentence(Sentence sent, Set<String> resourceNames) {
//    	for (Processor resource:resources){
//        	System.out.println("ccc");
//        	System.out.println(resourceNames.toString());
//        	System.out.println(resources.toString());
//        	if (resourceNames.contains(resource.getName())){
//        		resource.processNextSentence(sent);
//        	}
//        }
//    }
}
