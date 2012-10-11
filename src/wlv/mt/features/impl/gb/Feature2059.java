/**
 *
 */
package wlv.mt.features.impl.gb;

import java.util.HashSet;

import wlv.mt.features.impl.Feature;
import wlv.mt.features.util.*;

/**
 * CMU: proportion of pruned search graph nodes
 *
 * @author cat
 *
 */
public class Feature2059 extends Feature {

    public Feature2059() {
        setIndex("2059");
        HashSet<String> res = new HashSet<String>();
        res.add("pruned");
        setResources(res);
        setDescription("proportion of pruned search graph nodes ");

    }

    public void run(Sentence source, Sentence target) {
        float total = new Float((String) source.getValue("totalHypotheses"));
        float pruned = new Float((String) source.getValue("pruned"));
        setValue(pruned / total);

    }
}
