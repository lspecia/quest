/**
 *
 */
package wlv.mt.features.impl.gb;

import wlv.mt.features.impl.Feature;
import wlv.mt.features.util.Sentence;
import wlv.mt.features.util.*;
import java.util.*;

/**
 * CMU phrase probability phrase_prob_feat_4
 *
 * @author cat
 *
 */
public class Feature2024 extends Feature {

    public Feature2024() {
        setIndex("2024");
        setDescription("CMU phrase probability phrase_prob_feat_4");
        HashSet<String> res = new HashSet<String>();
        res.add("phrase_prob_feat_4");
        setResources(res);
    }

    /* (non-Javadoc)
     * @see wlv.mt.features.impl.Feature#run(wlv.mt.features.util.Sentence, wlv.mt.features.util.Sentence)
     */
    @Override
    public void run(Sentence source, Sentence target) {
        // TODO Auto-generated method stub
        Translation best = source.getBest();
        float value = Float.parseFloat(best.getAttribute("phrase_prob_feat_4"));
        setValue(value);
    }
}
