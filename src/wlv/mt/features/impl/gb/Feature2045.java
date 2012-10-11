/**
 *
 */
package wlv.mt.features.impl.gb;

import wlv.mt.features.impl.Feature;
import wlv.mt.features.util.*;
import java.util.*;

/**
 * averaged target word statistics: score-weighted relative frequency of the
 * word in the n-best list
 *
 * @author Catalina Hallett
 *
 */
public class Feature2045 extends Feature {

    public Feature2045() {
        setIndex("2045");
        HashSet res = new HashSet<String>();
//		res.add("prob");
        setResources(res);
        setDescription("averaged target word statistics: score-weighted relative frequency of the word in the n-best list ");
    }

    /* (non-Javadoc)
     * @see wlv.mt.features.impl.Feature#run(wlv.mt.features.util.Sentence, wlv.mt.features.util.Sentence)
     */
    @Override
    public void run(Sentence source, Sentence target) {
        // TODO Auto-generated method stub
        Translation best = source.getBest();
//		float score = Float.parseFloat(best.getAttribute("log_prob_feat"));
        float score = Float.parseFloat(best.getAttribute("prob"));
        float relFreq = (Float) source.getValue("nbestRelWordFreq");
        setValue(score * relFreq);

    }
}
