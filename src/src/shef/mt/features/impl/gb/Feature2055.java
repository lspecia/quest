/**
 *
 */
package shef.mt.features.impl.gb;

import shef.mt.features.util.Sentence;
import java.util.HashSet;

import shef.mt.features.impl.Feature;

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
