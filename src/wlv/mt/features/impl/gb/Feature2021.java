/**
 *
 */
package wlv.mt.features.impl.gb;

import wlv.mt.features.impl.Feature;
import wlv.mt.features.util.Sentence;
import wlv.mt.features.util.*;
import java.util.*;

/**
 * CMU phrase probability phrase_prob_feat_1
 *
 * @author cat
 *
 */
public class Feature2021 extends Feature {

    public Feature2021() {
        setIndex("2021");
        setDescription("CMU phrase probability phrase_prob_feat_1");
        HashSet<String> res = new HashSet<String>();
        res.add("phrase_prob_feat_1");
        setResources(res);
    }

    /* (non-Javadoc)
     * @see wlv.mt.features.impl.Feature#run(wlv.mt.features.util.Sentence, wlv.mt.features.util.Sentence)
     */
    @Override
    public void run(Sentence source, Sentence target) {
        // TODO Auto-generated method stub
        Translation best = source.getBest();
        float value = Float.parseFloat(best.getAttribute("phrase_prob_feat_1"));
        setValue(value);
    }
}
