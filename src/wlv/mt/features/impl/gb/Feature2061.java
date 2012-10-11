/**
 *
 */
package wlv.mt.features.impl.gb;

import java.util.HashSet;

import wlv.mt.features.impl.Feature;
import wlv.mt.features.util.*;

/**
 * CMU: proportion of recombined graph nodes
 *
 * @author cat
 *
 */
public class Feature2061 extends Feature {

    public Feature2061() {
        setIndex("2061");
        HashSet<String> res = new HashSet<String>();
        res.add("recombined");
        setResources(res);
        setDescription("proportion of recombined graph nodes");
    }

    public void run(Sentence source, Sentence target) {
        float total = new Float((String) source.getValue("totalHypotheses"));
        float disc = new Float((String) source.getValue("recombined"));
        setValue(disc / total);

    }
}
