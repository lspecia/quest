package shef.mt.tools;

import shef.mt.pipelines.ProcessorPipeline;
import shef.mt.tools.pcfg.BParserProcessor;
import shef.mt.tools.tok.TokenFeatureGenerator;

public class BasicPipeline extends ProcessorPipeline {

	public BasicPipeline() {
		super();
		processors.add(new TokenFeatureGenerator());
		processors.add(new BParserProcessor());
	}

}
