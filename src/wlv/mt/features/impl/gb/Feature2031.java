/**
 *
 */
package wlv.mt.features.impl.gb;

import wlv.mt.features.impl.Feature;
import wlv.mt.features.util.Sentence;
import wlv.mt.features.util.*;
import java.util.*;

/**
 * IBM m1t2s
 *
 * @author cat
 *
 */
public class Feature2031 extends Feature {

    public Feature2031() {
        setIndex("2031");
        HashSet<String> res = new HashSet<String>();
        res.add("m1t2s");
        setResources(res);
    }

    /* (non-Javadoc)
     * @see wlv.mt.features.impl.Feature#run(wlv.mt.features.util.Sentence, wlv.mt.features.util.Sentence)
     */
    @Override
    public void run(Sentence source, Sentence target) {
        // TODO Auto-generated method stub
        Translation best = source.getBest();
        float value = Float.parseFloat(best.getAttribute("m1t2s"));
        setValue(value);
    }
}
