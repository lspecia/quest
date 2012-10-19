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
public class Feature5002 extends Feature {

    //final static Float probThresh = 0.10f;

    public Feature5002() {
        setIndex(5002);
        setDescription("Ratio of number of stopwords in target and source sentence");
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
        //Calculate number of stopwords in source sentence
        String text1 = source.getText();
        StopWord swcalculator1 = new StopWord("./lang_resources/english/englishstopwords.txt",text1);
        int result1 = swcalculator1.calculate();
        //Number of stopwords in the target sentence
        String text2 = target.getText();
        StopWord swcalculator2 = new StopWord("./lang_resources/spanish/spanishstopwords.txt",text2);
        int result2 = swcalculator2.calculate();
        float ratio ;
        if (result1==0) ratio = -1.00f;
        else
        ratio = (float) result2/result1;
        setValue(ratio);
    }
}
