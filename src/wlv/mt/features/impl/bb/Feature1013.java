package wlv.mt.features.impl.bb;

import java.util.HashSet;

import wlv.mt.features.impl.Feature;
import wlv.mt.features.util.Sentence;

/**
 *
 *
 * perplexity of the target
 */
public class Feature1013 extends Feature {

    public Feature1013() {
        setIndex(1013);
        setDescription("perplexity of the target");
        HashSet res = new HashSet<String>();
        res.add("ppl");
        setResources(res);
    }

    @Override
    public void run(Sentence source, Sentence target) {
        // TODO Auto-generated method stub
        setValue((Float) target.getValue("ppl"));
    }
}
