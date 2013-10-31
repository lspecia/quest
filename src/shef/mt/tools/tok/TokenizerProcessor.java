package shef.mt.tools.tok;

import java.util.ArrayList;
import java.util.HashMap;

import shef.mt.features.util.Sentence;
import shef.mt.tools.Resource;
import shef.mt.tools.SingleProcessor;

public class TokenizerProcessor extends SingleProcessor {

	private Tokenizer tokenizer;
	
	public TokenizerProcessor() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public HashMap<String, Object> getFeatures(Sentence sentence) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void initialize(HashMap<String, Resource> initializedResources) {
		// TODO Auto-generated method stub

	}

	@Override
	protected HashMap<String, String> defineFeatureDescriptions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected ArrayList<String> defineRequiredProcessors() {
		// TODO Auto-generated method stub
		return null;
	}

}
