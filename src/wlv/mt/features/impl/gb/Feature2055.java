/**
 *
 */
package wlv.mt.features.impl.gb;

import java.util.HashSet;

import wlv.mt.features.impl.Feature;
import wlv.mt.features.util.*;

/**
 * CMU: totalHypotheses
 *
 * @author cat
 *
 */
public class Feature2055 extends Feature {

    public Feature2055() {
        setIndex("2055");
        HashSet<String> res = new HashSet<String>();
        res.add("totalHypotheses");
        setResources(res);
        setDescription("CMU: totalHypotheses");

    }

    public void run(Sentence source, Sentence target) {
        setValue(new Float((String) source.getValue("totalHypotheses")));
    }
}
