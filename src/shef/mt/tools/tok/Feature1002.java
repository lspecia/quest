package shef.mt.tools.tok;

import shef.mt.features.util.Sentence;
import java.util.HashSet;

import java.util.StringTokenizer;

import shef.mt.features.impl.Feature;
import shef.mt.tools.Resource;
import shef.mt.tools.giza.Giza;
import shef.mt.tools.pos.PosTreeTagger;

/**
 * no tokens in target
 *
 * @author Catalina Hallett
 *
 *
 */
public class Feature1002 extends Feature {

    public Feature1002() {
        setIndex(1002);
        setDescription("no tokens in target");
        HashSet res = new HashSet<Resource>();
        setResources(res);
    }

    public void run(Sentence source, Sentence target) {
        setValue(target.getNoTokens());
    }
}
