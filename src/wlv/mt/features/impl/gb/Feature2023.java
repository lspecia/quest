/**
 *
 */
package wlv.mt.features.impl.gb;

import wlv.mt.features.impl.Feature;
import wlv.mt.features.util.Sentence;
import wlv.mt.features.util.*;
import java.util.*;

/**
 * CMU phrase probability phrase_prob_feat_3
 *
 * @author cat
 *
 */
public class Feature2023 extends Feature {

    public Feature2023() {
        setIndex("2023");
        setDescription("CMU phrase probability phrase_prob_feat_3");
        HashSet<String> res = new HashSet<String>();
        res.add("phrase_prob_feat_3");
        setResources(res);
    }

    /* (non-Javadoc)
     * @see wlv.mt.features.impl.Feature#run(wlv.mt.features.util.Sentence, wlv.mt.features.util.Sentence)
     */
    @Override
    public void run(Sentence source, Sentence target) {
        // TODO Auto-generated method stub
        Translation best = source.getBest();
        float value = Float.parseFloat(best.getAttribute("phrase_prob_feat_3"));
        setValue(value);
    }
}
