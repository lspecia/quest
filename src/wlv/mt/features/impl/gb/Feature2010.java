/**
 *
 */
package wlv.mt.features.impl.gb;

import wlv.mt.features.impl.Feature;
import wlv.mt.features.util.Sentence;
import wlv.mt.features.util.*;
import java.util.*;

/**
 * -using n-best for training LM: sentence 3-gram log-probability
 *
 * @author cat
 *
 */
public class Feature2010 extends Feature {

    public Feature2010() {
        setIndex("2010");
        setDescription("using n-best for training LM: sentence 3-gram log-probability");
    }

    /* (non-Javadoc)
     * @see wlv.mt.features.impl.Feature#run(wlv.mt.features.util.Sentence, wlv.mt.features.util.Sentence)
     */
    @Override
    public void run(Sentence source, Sentence target) {
        // TODO Auto-generated method stub
        float value = (Float) source.getValue("3_nb_logprob");
        setValue(value);
    }
}
