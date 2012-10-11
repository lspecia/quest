package wlv.mt.features.impl.bb;

import java.util.HashSet;

import wlv.mt.features.impl.Feature;
import wlv.mt.features.util.*;

/**
 * absolute difference between number of periods in source and target
 *
 * @author Catalina Hallett
 *
 */
public class Feature1062 extends Feature {

    public Feature1062() {
        setIndex(1062);
        setDescription("absolute difference between number of periods in source and target");
    }

    @Override
    public void run(Sentence source, Sentence target) {
        // TODO Auto-generated method stub

        float sourceCount;
        float targetCount;
        if (!source.isSet("count_.")) {
            sourceCount = source.countChar('.');
        } else {
            sourceCount = (Integer) source.getValue("count_.");
        }

        if (!target.isSet("count_.")) {
            targetCount = target.countChar('.');
        } else {
            targetCount = (Integer) target.getValue("count_.");
        }


        setValue(Math.abs(sourceCount - targetCount));
    }
}
