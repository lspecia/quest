package shef.mt.features.impl.bb;

import shef.mt.features.impl.Feature;
import shef.mt.features.util.Sentence;

public class Feature9987 extends Feature {

	public Feature9987() {
		// TODO Auto-generated constructor stub
		setIndex(9987);
		
	}

	@Override
	public void run(Sentence source, Sentence target) {
		setValue(new Float((Integer) source.getValue("terminology")));

	}

}
