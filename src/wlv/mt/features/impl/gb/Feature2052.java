/**
 *
 */
package wlv.mt.features.impl.gb;

import wlv.mt.features.impl.Feature;
import wlv.mt.features.util.Sentence;
import wlv.mt.features.util.*;
import java.util.*;

/**
 * edit distance of the current hypothesis to the center hypothesis
 *
 * @author cat
 *
 */
public class Feature2052 extends Feature {

    public Feature2052() {
        setIndex("2052");
        setDescription("edit distance of the current hypothesis to the center hypothesis ");

    }

    /* (non-Javadoc)
     * @see wlv.mt.features.impl.Feature#run(wlv.mt.features.util.Sentence, wlv.mt.features.util.Sentence)
     */
    @Override
    public void run(Sentence source, Sentence target) {
        // TODO Auto-generated method stub

        TreeSet<Translation> nbest = source.getTranslations();
        Translation best = source.getBest();
        int size = nbest.size();
        if (size <= 1) {
            setValue(0);
            return;
        }
        Translation center = source.getCenter();
        int editDist = StringOperations.editDistance(best.getText(), center.getText());
        best.setAttribute("editDist", editDist + "");
        setValue(editDist);

    }
}