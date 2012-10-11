package wlv.mt.features.impl.bb;

import java.util.*;

import wlv.mt.features.impl.Feature;
import wlv.mt.features.util.Sentence;

/**
 * number of tokens in the source sentence
 */
public class Feature1001 extends Feature {

    public Feature1001() {
        setIndex(1001);
        setDescription("number of tokens in the source sentence");

    }

    public void run(Sentence source, Sentence target) {
        setValue(source.getNoTokens());
    }
}
