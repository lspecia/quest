/**
 *
 */
package wlv.mt.features.impl.gb;

import wlv.mt.features.impl.Feature;
import wlv.mt.features.util.Sentence;
import wlv.mt.features.util.*;
import java.util.*;

/**
 * CMU: distortion feature 2
 *
 * @author cat
 *
 */
public class Feature2015 extends Feature {

    public Feature2015() {
        setIndex(2015);
        setDescription("");
        HashSet<String> res = new HashSet<String>();
        res.add("dist_feat_2");
        setResources(res);
    }

    /* (non-Javadoc)
     * @see wlv.mt.features.impl.Feature#run(wlv.mt.features.util.Sentence, wlv.mt.features.util.Sentence)
     */
    @Override
    public void run(Sentence source, Sentence target) {
        // TODO Auto-generated method stub
        float value = Float.parseFloat(source.getTranslationAttribute("dist_feat_2"));
        setValue(value);
    }
}
