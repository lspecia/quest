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
 * @author lefterav
 */
public class Feature9304 extends Feature {

	/* (non-Javadoc)
	 * @see wlv.mt.features.impl.Feature#run(wlv.mt.features.util.Sentence, wlv.mt.features.util.Sentence)
	 */
	
	public Feature9304(){
		setIndex(9304);
		setDescription("Target PCFG Parse log-likelihood");
        HashSet<String> res = new HashSet<String>();
		res.add("BParser");
		setResources(res);
	}
	
	public void run(Sentence source, Sentence target) {
		setValue(new Float((Double) target.getValue("bparser.loglikelihood")));
	}

}
