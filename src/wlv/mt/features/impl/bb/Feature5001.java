/**
 *
 */
package wlv.mt.features.impl.bb;

import java.util.HashSet;
import java.util.StringTokenizer;

import wlv.mt.features.impl.Feature;
import wlv.mt.features.util.Sentence;
import wlv.mt.tools.Giza;
import wlv.mt.tools.Giza2;
import wlv.mt.features.wce.StopWord;



/**
 * Number of stopwords between in target sentence
 *
 * @author Luong Ngoc Quang
 *
 *
 */
public class Feature5001 extends Feature {

    //final static Float probThresh = 0.10f;

    public Feature5001() {
        setIndex(5001);
        setDescription("Number of stopwords in the source sentence");
        HashSet res = new HashSet<String>();
        res.add("Giza");
        setResources(res);
    }

    /* (non-Javadoc)
     * @see wlv.mt.features.util.Feature#run(wlv.mt.features.util.Sentence, wlv.mt.features.util.Sentence)
     */
    @Override
    public void run(Sentence source, Sentence target) {
        // TODO Auto-generated method stub
        String text = source.getText();
        StopWord swcalculator = new StopWord("/Users/luongngocquang/Documents/resources/englishstopwords.txt",text);
        int result = swcalculator.calculate();

        setValue(result);
    }
}

