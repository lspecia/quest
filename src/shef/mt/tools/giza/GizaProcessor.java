/**
 * 
 */
package shef.mt.tools.giza;

import java.util.ArrayList;
import java.util.HashMap;

import shef.mt.features.util.ParallelSentence;
import shef.mt.features.util.Sentence;
import shef.mt.tools.BilingualProcessor;
import shef.mt.tools.Resource;

/**
 * @author dupo
 *
 */
public class GizaProcessor extends BilingualProcessor {

	private Giza giza;
	/**
	 * 
	 */
	public GizaProcessor() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see shef.mt.tools.BilingualProcessor#getParallelFeatures(shef.mt.features.util.Sentence, shef.mt.features.util.Sentence, shef.mt.features.util.ParallelSentence)
	 */
	@Override
	public HashMap<String, Object> getParallelFeatures(Sentence sourceSentence,
			Sentence targetSentence, ParallelSentence parallelsentence) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see shef.mt.tools.Processor#initialize(java.util.HashMap)
	 */
	@Override
	public void initialize(HashMap<String, Resource> initializedResources) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see shef.mt.tools.Processor#defineFeatureDescriptions()
	 */
	@Override
	protected HashMap<String, String> defineFeatureDescriptions() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see shef.mt.tools.Processor#defineRequiredProcessors()
	 */
	@Override
	protected ArrayList<String> defineRequiredProcessors() {
		// TODO Auto-generated method stub
		return null;
	}

}
