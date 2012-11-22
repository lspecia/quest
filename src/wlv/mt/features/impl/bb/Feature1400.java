package wlv.mt.features.impl.bb;

import java.util.HashSet;
import java.util.StringTokenizer;

import wlv.mt.features.impl.Feature;
import wlv.mt.features.util.Sentence;
import wlv.mt.tools.Giza;
import wlv.mt.tools.GlobalLexicon;

/**
 * geometric average of target word probabilities under Global Lexicon Model
 * 
 * @author Christian Buck
 */

public class Feature1400 extends Feature {

    final static Float probThresh = 0.01f;

    public Feature1400() {
        setIndex(1400);
        setDescription("geometric average of target word probabilities under Global Lexicon Model");
        HashSet res = new HashSet<String>();
        res.add("GlobalLexicon");
        setResources(res);
    }

    public static Double geometricMean(Double[] probs) {
        Double sum = 0.0;
        for (Double w: probs) {
            sum += w;
        }
        return Math.pow(sum, 1.0 / probs.length);
    }
        
    @Override
    public void run(Sentence source, Sentence target) {
        String[] s_tokens = source.getTokens();
        String[] t_tokens = target.getTokens();
        Double[] probs = new Double[t_tokens.length];
        
        Integer prob_idx = 0;
        for (String tw : t_tokens) {
            Double p = GlobalLexicon.getBias(tw);
            for (String sw : s_tokens) {
                p += GlobalLexicon.get(sw, tw);
            }
            p = 1./(1.+ Math.exp(-p));
            probs[prob_idx] = p;
            prob_idx++;
        }
        setValue(geometricMean(probs).floatValue());
    }
}
