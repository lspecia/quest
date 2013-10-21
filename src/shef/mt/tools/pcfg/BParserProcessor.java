package shef.mt.tools.pcfg;

import java.util.ArrayList;
import java.util.HashMap;

import shef.mt.features.util.Sentence;
import shef.mt.tools.Resource;
import shef.mt.tools.ResourceManager;
import shef.mt.tools.ResourceProcessor;
import shef.mt.tools.SingleProcessor;
import shef.mt.tools.pcfg.BParser;
import shef.mt.util.PropertiesManager;
import shef.mt.pipelines.ResourcePipeline;
import shef.mt.pipelines.ResourceRequirement;
import shef.mt.features.util.FeatureManager;

/**
 * A processor class for the BParser. It loads the 
 * Parser instances, runs those through the sentences 
 * and provides the features
 * 
 *
 * @author Eleftherios Avramids
 */
public class BParserProcessor extends SingleProcessor {
	
	BParser parser;
	boolean tokenizer;
	public String resourceName = "BParser";
	
	public BParserProcessor(PropertiesManager propertiesManager, String lang) {
		super(propertiesManager, lang);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected ArrayList<ResourceRequirement> defineRequiredResources() {
		ArrayList<ResourceRequirement> requiredResources = new ArrayList<ResourceRequirement>();
		
		//There is only one required resource, the berkley parser connector
		//which we here implement so that it can be shared with other processors
		ResourceRequirement bParser = new ResourceRequirement("shef.mt.tools.pcfg.BParser");
		String grammarFilename = propertiesManager.getString(this.language + ".bparser.grammar" );
		bParser.addParameter("grammarFilename", grammarFilename);
		
		String kbest_entry = propertiesManager.getString(language + ".bparser.kbest");
		bParser.addParameter("kbest", kbest_entry);
		
		bParser.addParameter("chinese", false);		
		
		requiredResources.add(bParser);
		
		return requiredResources;
	}
	


	@Override
	protected HashMap<String, String> defineFeatureDescriptions() {
		HashMap<String,String> featureDescriptions = new HashMap<String,String>();
		
		featureDescriptions.put("pcfg.parse", "Bracketed version of the PCFG parse tree of the sentence");
		featureDescriptions.put("pcfg.loglikelihood", "Log-likelihood of the sentence PCFG parse");
		featureDescriptions.put("pcfg.avgConfidence", "Average confidence of all PCFG parses");
		featureDescriptions.put("pcfg.bestParseConfidence", "Confidence for the best PCFG tree");
		featureDescriptions.put("pcfg.n", "Number of k-best PCFG trees generated");
				
		// TODO Auto-generated method stub
		return featureDescriptions;
	}




	@Override
	protected ArrayList<String> defineRequiredProcessors() {
		ArrayList<String> reqProcessors = new ArrayList<String>();
		reqProcessors.add("Tokenizer");
		reqProcessors.add("Truecaser");
		return reqProcessors;
	}
    

	
	/***
	 * This function initializes a parser object with the desired grammar
	 * into memeory
	 * @param inputFile text file containing the text to be processed
	 * @param rm contains the parameters set in the configuration file
	 * @param language the name of the language that this processor will be
	 *    responsible for
	 */
	@Override
	public void initialize(HashMap<String, Resource> initializedResources) {
		//get the grammar filename from the configuration file
		
		
		//initialize the BParser class, which is a wrapper around the native parser
		//a special parameter exists for chinese='true', otherwise this is 'false'
		parser = (BParser) initializedResources.get("shef.mt.tools.pcfg.BParser");
		
		//it has been seen that the English and the Spanish grammar behave better with Moses tokenizer.perl
		//than with the internal berkeley tokenizer 
		tokenizer = false;
	}

	
	
	@Override
	public HashMap<String, Object> getFeatures(Sentence sentence) {
		HashMap<String,Object> features = new HashMap<String,Object>();
		parser.getParseFeatures(sentence.getText(), tokenizer);
		
		features.put("pcfg.parse", parser.getParseTree());
		features.put("pcfg.loglikelihood", parser.getLoglikelihood());
		features.put("pcfg.avgConfidence", parser.getAvgConfidence());
		features.put("pcfg.bestParseConfidence", parser.getBestParseConfidence());
		features.put("pcfg.n", parser.getParseTreesN());
		
		return features;
	}

	

	






}
