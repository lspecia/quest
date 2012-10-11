package wlv.mt.features.impl.gb;

import wlv.mt.features.util.*;
import wlv.mt.features.impl.*;
import java.util.*;

/**
 * CMU: proportion of unknown words
 *
 * @author Catalina Hallett
 *
 */
public class Feature2043 extends Feature {

    public Feature2043() {
        setIndex("2043");
        HashSet res = new HashSet<String>();
        res.add("unknown");
        setResources(res);

        setDescription("CMU: proportion of unknown words");
    }

    public void run(Sentence source, Sentence target) {
        float unk = new Float((String) source.getValue("unknown")).floatValue();
        int total = source.getNoTokens();
        setValue(unk / total);
    }
}
