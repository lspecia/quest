/**
 *
 */
package wlv.mt.features.impl.gb;

import java.util.HashSet;

import wlv.mt.features.impl.Feature;
import wlv.mt.features.util.*;

/**
 * CMU: number of recombined graph nodes
 *
 * @author cat
 *
 */
public class Feature2060 extends Feature {

    public Feature2060() {
        setIndex("2060");
        setDescription("number of recombined graph nodes");
        HashSet<String> res = new HashSet<String>();
        res.add("recombined");
        setResources(res);

    }

    public void run(Sentence source, Sentence target) {
        setValue(new Float((String) source.getValue("recombined")));
    }
}
