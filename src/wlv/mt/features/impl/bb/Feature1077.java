/**
 *
 */
package wlv.mt.features.impl.bb;

import java.util.StringTokenizer;

import wlv.mt.features.impl.Feature;
import wlv.mt.features.util.*;

/**
 * percentage of numbers in the source
 *
 * @author Catalina Hallett
 *
 */
public class Feature1077 extends Feature {

    /* (non-Javadoc)
     * @see wlv.mt.features.impl.Feature#run(wlv.mt.features.util.Sentence, wlv.mt.features.util.Sentence)
     */
    public Feature1077() {
        setIndex(1077);
        setDescription("percentage of numbers in the source");
    }

    @Override
    public void run(Sentence source, Sentence target) {
        // TODO Auto-generated method stub
        float noTokens;
        StringTokenizer st = new StringTokenizer(source.getText());

        noTokens = source.getNoTokens();
        String token;
        int count = 0;
        while (st.hasMoreTokens()) {
            token = st.nextToken();
            if (StringOperations.isNumber(token)) {
                count++;
            }
        }
        source.setValue("noNumbers", count);
        setValue((float) count / noTokens);
    }
}
