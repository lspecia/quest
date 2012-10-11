package wlv.mt.features.impl.bb;

import wlv.mt.features.impl.*;
import java.util.*;
import wlv.mt.features.util.*;

/**
 * coherence
 */
public class Feature1097 extends Feature {

    public Feature1097() {
        setIndex(1097);
        setDescription("coherence");
        addResource("coh");
    }

    public void run(Sentence source, Sentence target) {
        System.out.println(target.getValue("coherence"));
        setValue((Float) target.getValue("coherence"));
    }
}
