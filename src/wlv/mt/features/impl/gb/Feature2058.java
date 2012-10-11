/**
 *
 */
package wlv.mt.features.impl.gb;

import java.util.HashSet;

import wlv.mt.features.impl.Feature;
import wlv.mt.features.util.*;

/**
 * CMU: pruned search graph nodes
 *
 * @author cat
 *
 */
public class Feature2058 extends Feature {

    public Feature2058() {
        setIndex("2058");
        HashSet<String> res = new HashSet<String>();
        res.add("pruned");
        setResources(res);
        setDescription("CMU: pruned search graph nodes");

    }

    public void run(Sentence source, Sentence target) {
        setValue(new Float((String) source.getValue("pruned")));

    }
}
