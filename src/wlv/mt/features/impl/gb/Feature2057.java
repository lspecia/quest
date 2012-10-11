/**
 *
 */
package wlv.mt.features.impl.gb;

import java.util.HashSet;

import wlv.mt.features.impl.Feature;
import wlv.mt.features.util.*;

/**
 * CMU: percentage of discarded graph nodes
 *
 * @author cat
 *
 */
public class Feature2057 extends Feature {

    public Feature2057() {
        setIndex("2057");
        HashSet<String> res = new HashSet<String>();
        res.add("discarded");
        setDescription("CMU: percentage of discarded graph nodes");
        setResources(res);

    }

    public void run(Sentence source, Sentence target) {
        float total = new Float((String) source.getValue("totalHypotheses"));
        float disc = new Float((String) source.getValue("discarded"));
        setValue(disc / total);

    }
}
