/**
 *
 */
package wlv.mt.features.impl.gb;

import java.util.HashSet;

import wlv.mt.features.impl.Feature;
import wlv.mt.features.util.*;

/**
 * CMU: discarded search graph nodes
 *
 * @author cat
 *
 */
public class Feature2056 extends Feature {

    public Feature2056() {
        setIndex("2056");
        HashSet<String> res = new HashSet<String>();
        res.add("discarded");
        setResources(res);
        setDescription("discarded search graph nodes");

    }

    public void run(Sentence source, Sentence target) {
        setValue(new Float((String) source.getValue("discarded")));
    }
}
