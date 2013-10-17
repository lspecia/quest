/**
 *
 */
package shef.mt.tools.stf;

import java.util.HashSet;

import shef.mt.features.impl.Feature;
import shef.mt.features.util.Sentence;

/**
 * absolute difference between the depth of the syntactic tree for the source
 * and the depth of the syntactic tree for the target
 *
 * @author Catalina Hallett
 *
 *
 */
public class Feature1101 extends Feature {

    public Feature1101() {
        setIndex(1101);
        setDescription("absolute difference between the depth of the syntactic tree for the source and the depth of the syntactic tree for the target");
        HashSet res = new HashSet();
        //requires named entities
        res.add("stf");
        setResources(res);
    }

    public void run(Sentence source, Sentence target) {
        float dptSource = (Float) source.getValue("depth");
        float dptTarget = (Float) target.getValue("depth");
        setValue(Math.abs(dptSource - dptTarget));
    }
}
