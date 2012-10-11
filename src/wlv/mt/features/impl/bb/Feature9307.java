/**
 * 
 */
package wlv.mt.features.impl.bb;

import java.util.HashSet;
import wlv.mt.features.impl.Feature;
import wlv.mt.features.util.Sentence;

/**
 * PCFG Parse log likelihood
 * 
 * @author Eleftherios Avramidis
 */
public class Feature9307 extends Feature {

	/* (non-Javadoc)
	 * @see wlv.mt.features.impl.Feature#run(wlv.mt.features.util.Sentence, wlv.mt.features.util.Sentence)
	 */
	
	public Feature9307(){
		setIndex(9303);
		setDescription("Count of possible target PCFG parses (n)");
        HashSet<String> res = new HashSet<String>();
		res.add("BParser");
		setResources(res);
	}
	
	public void run(Sentence source, Sentence target) {
		setValue(new Float((Integer) target.getValue("bparser.n")));
	}

}
