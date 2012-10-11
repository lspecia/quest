package wlv.mt.features.impl.bb;

import java.util.HashSet;

import java.util.StringTokenizer;

import wlv.mt.features.impl.Feature;
import wlv.mt.features.util.*;
import wlv.mt.tools.Giza;
import wlv.mt.tools.PosTreeTagger;
import wlv.mt.tools.Resource;

/**
 * no tokens in target
 *
 * @author Catalina Hallett
 *
 *
 */
public class Feature1002 extends Feature {

    public Feature1002() {
        setIndex(1002);
        setDescription("no tokens in target");
        HashSet res = new HashSet<Resource>();
        setResources(res);
    }

    public void run(Sentence source, Sentence target) {
        setValue(target.getNoTokens());
    }
}
