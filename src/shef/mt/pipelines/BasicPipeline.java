package shef.mt.pipelines;

import shef.mt.tools.BilingualProcessor;
import shef.mt.tools.Processor;
import shef.mt.tools.SingleProcessor;
import shef.mt.tools.pcfg.BParserProcessor;
import shef.mt.tools.tok.TokenProcessor;
import shef.mt.util.PropertiesManager;

public class BasicPipeline extends ProcessorPipeline {

	private void addProcessor(SingleProcessor singleProcessor, PropertiesManager propertiesMan, String lang){
		singleProcessor.prepare(propertiesMan, lang);
		processors.add(singleProcessor);
	}
	
	private void addProcessor(BilingualProcessor processor, PropertiesManager propertiesMan, String sourceLang, String targetLang){
		
		processor.prepare(propertiesMan, sourceLang, targetLang);
		processors.add(processor);
	}
	
	public BasicPipeline(PropertiesManager propertiesMan, String sourceLang, String targetLang) {
		super();
		addProcessor(new TokenProcessor(), propertiesMan, sourceLang, targetLang);
		addProcessor(new BParserProcessor(), propertiesMan, sourceLang);
		addProcessor(new BParserProcessor(), propertiesMan, targetLang);
	}

}
