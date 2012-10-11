package wlv.mt.features.impl.bb;

import java.util.StringTokenizer;

import wlv.mt.features.impl.Feature;
import wlv.mt.features.util.*;

/**
 * ratio of number of tokens in source and target
 *
 * @author Catalina Hallett
 *
 */
public class Feature1003 extends Feature {

    public Feature1003() {
        setIndex(1003);
        setDescription("ratio of number of tokens in source and target");
    }

    @Override
    public void run(Sentence source, Sentence target) {
        // TODO Auto-generated method stub
        int sourceTok = source.getNoTokens();
        int targetTok = target.getNoTokens();
        if (targetTok == 0) {
            setValue(0);
        } else {
            setValue((float) sourceTok / targetTok);
        }
    }
}
