package shef.mt.tools;

import java.util.Map;
import java.util.Iterator;

import shef.mt.features.util.Sentence;
import shef.mt.tools.BParser;
import shef.mt.util.PropertiesManager;
import shef.mt.pipelines.DefaultResourcePipeline;

/**
 * A processor class for the BParser. It loads the 
 * Parser instances, runs those through the sentences 
 * and provides the features
 * 
 * @author Eleftherios Avramids
 *
 */
public class BParserProcessor extends ResourceProcessor {
	
	BParser parser;
	boolean tokenizer;
	public String resourceName;

	public void initialize(String sourceFile, String targetFile,
                        PropertiesManager propertiesManager,
                        String sourceLang, String targetLang) {

		BParserProcessor sourceParserProcessor = new BParserProcessor();
                BParserProcessor targetParserProcessor = new BParserProcessor();

	        sourceParserProcessor.create(sourceFile, propertiesManager, sourceLang);
        	targetParserProcessor.create(targetFile, propertiesManager, targetLang);

		DefaultResourcePipeline drp = new DefaultResourcePipeline();
        	drp.addResourceProcessor(sourceParserProcessor);
	        drp.addResourceProcessor(targetParserProcessor);
	}
	/***
	 * This function initializes a parser object with the desired grammar
	 * into memeory
	 * @param inputFile text file containing the text to be processed
	 * @param rm contains the parameters set in the configuration file
	 * @param language the name of the language that this processor will be
	 *    responsible for
	 */
	public void create(String inputFile, PropertiesManager rm, String language){
		//get the grammar filename from the configuration file
		String grammarFilename = rm.getString(language + ".bparser.grammar");
		String kbest_entry = rm.getString(language + ".bparser.kbest");
		
		int kbest;
		if (kbest_entry == null){
			kbest = 1000;
		} else {
			kbest = Integer.valueOf(kbest_entry);
		}
			
		//initialize the BParser class, which is a wrapper around the native parser
		//a special parameter exists for chinese='true', otherwise this is 'false'
		parser = new BParser(grammarFilename, (language == "chinese"), kbest);
		
		//it has been seen that the English and the Spanish grammar behave better with Moses tokenizer.perl
		//than with the internal berkeley tokenizer 
		tokenizer = false;
		
		//parser initialized, so register the resource, so that the features know it is available
		this.resourceName = "BParser";
		ResourceManager.registerResource(this.resourceName);
	}
	

	@Override
	public void processNextSentence(Sentence s) {
		
		//ask the parser to perform a parse of the sentence
		parser.getParseFeatures(s.getText(), tokenizer);
		
		s.setValue("bparser.parse", parser.getParseTree());
		s.setValue("bparser.loglikelihood", parser.getLoglikelihood());
		s.setValue("bparser.avgConfidence", parser.getAvgConfidence());
		s.setValue("bparser.bestParseConfidence", parser.getBestParseConfidence());
		s.setValue("bparser.n", parser.getParseTreesN());
	}
	
	
}
