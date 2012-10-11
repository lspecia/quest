/**
 *
 */
package wlv.mt.features.impl.gb;

import wlv.mt.features.impl.Feature;
import wlv.mt.features.util.Sentence;
import wlv.mt.features.util.*;
import java.util.*;

/**
 * CMU phrase probability phrase_prob_feat_5
 *
 * @author cat
 *
 */
public class Feature2025 extends Feature {

    public Feature2025() {
        setIndex("2025");
        setDescription("CMU phrase probability phrase_prob_feat_5");
        HashSet<String> res = new HashSet<String>();
        res.add("phrase_prob_feat_5");
        setResources(res);
    }

    /* (non-Javadoc)
     * @see wlv.mt.features.impl.Feature#run(wlv.mt.features.util.Sentence, wlv.mt.features.util.Sentence)
     */
    @Override
    public void run(Sentence source, Sentence target) {
        // TODO Auto-generated method stub
        Translation best = source.getBest();
        float value = Float.parseFloat(best.getAttribute("phrase_prob_feat_5"));
        setValue(value);
    }
}
