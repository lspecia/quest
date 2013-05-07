package wlv.mt.features.util;

import java.util.*;
import edu.stanford.nlp.trees.*;
import edu.stanford.nlp.process.*;
import wlv.mt.util.*;

/**
 * Wrap one source sentence object with many target sentence objects
 * @author Eleftherios Avramidis 
 */
public class ParallelSentence {
    private Sentence sourceSentence;
    private List<Sentence> targetSentences;   
    private HashMap<String, Object> attributes;

    public ParallelSentence(Sentence sourceSentence, ArrayList<Sentence> targetSentence) {
        this.sourceSentence = sourceSentence;
        this.targetSentences = targetSentences;
        this.attributes = new HashMap<String, Object>();
    }

    public ParallelSentence(Sentence sourceSentence, ArrayList<Sentence> targetSentence, HashMap<String, Object> attributes){
        this.sourceSentence = sourceSentence;
        this.targetSentences = targetSentences;
        this.attributes = attributes
    }

    public Sentence getSource() {
        return this.sourceSentence;
    }

    public Sentence getTarget() {
        return this.targetSentence;
    }

    public HashMap<String,Object>() {
        return this.attributes;
    }


}
