/**
 *
 */
package wlv.mt.features.impl.bb;

import wlv.mt.features.impl.Feature;
import wlv.mt.features.util.*;

/**
 * percentage of numbers in the target
 *
 * @author Catalina Hallett
 *
 */
public class Feature1078 extends Feature {

    /* (non-Javadoc)
     * @see wlv.mt.features.impl.Feature#run(wlv.mt.features.util.Sentence, wlv.mt.features.util.Sentence)
     */
    public Feature1078() {
        setIndex(1078);
        setDescription("percentage of numbers in the target sentence");
    }

    @Override
    public void run(Sentence source, Sentence target) {
        // TODO Auto-generated method stub
        float noTokens = target.getNoTokens();

        int count = 0;
        String[] tokens = target.getTokens();
        for (String token : tokens) {
            if (StringOperations.isNumber(token)) {
                count++;
            }
        }
        target.setValue("noNumbers", count);
        setValue(count / noTokens);
    }
}
