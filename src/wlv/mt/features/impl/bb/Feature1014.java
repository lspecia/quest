package wlv.mt.features.impl.bb;

import java.util.HashSet;

import wlv.mt.features.impl.Feature;
import wlv.mt.features.util.Sentence;

/**
 *
 *
 * perplexity of the target sentence without end of sentence marker
 */
public class Feature1014 extends Feature {

    public Feature1014() {
        setIndex(1014);
        setDescription("perplexity of the target sentence without end of sentence marker");
        HashSet res = new HashSet<String>();
        res.add("ppl");
        setResources(res);
    }

    @Override
    public void run(Sentence source, Sentence target) {
        // TODO Auto-generated method stub
        setValue((Float) target.getValue("ppl1"));
    }
}
