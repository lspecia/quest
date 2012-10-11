package wlv.mt.features.impl.bb;

import java.util.HashSet;

import wlv.mt.features.impl.Feature;
import wlv.mt.features.util.*;

/**
 * source sentence log probability
 *
 * @author Catalina Hallett
 *
 */
public class Feature1009 extends Feature {

    public Feature1009() {
        setIndex(1009);
        setDescription("source sentence log probability");
        HashSet res = new HashSet<String>();
        res.add("ppl");
    }

    @Override
    public void run(Sentence source, Sentence target) {
        // TODO Auto-generated method stub
        setValue((Float) source.getValue("logprob"));
    }
}
