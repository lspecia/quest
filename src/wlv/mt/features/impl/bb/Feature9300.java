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
public class Feature9300 extends Feature {

	/* (non-Javadoc)
	 * @see wlv.mt.features.impl.Feature#run(wlv.mt.features.util.Sentence, wlv.mt.features.util.Sentence)
	 */
	
	public Feature9300(){
		setIndex(9300);
		setDescription("Source PCFG Parse log-likelihood");
        HashSet<String> res = new HashSet<String>();
		res.add("BParser");
		setResources(res);
	}
	
	public void run(Sentence source, Sentence target) {
		setValue(new Float((Double) source.getValue("bparser.loglikelihood")));
	}

}
