package shef.mt.features.util.io;

import java.util.Set;

import shef.mt.features.util.ParallelSentence;

public interface FeatureWriter {

	public abstract void write(ParallelSentence parallelSentence,
			Set<String> requiredFeatures);
	
	public abstract void close();
}
