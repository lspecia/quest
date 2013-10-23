package shef.mt.features.util;

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
    
    public ParallelSentence(){
    	this.targetSentences = new ArrayList<Sentence>();
    }

    public ParallelSentence(Sentence sourceSentence, List<Sentence> targetSentences) {
        this.sourceSentence = sourceSentence;
        this.targetSentences = targetSentences;
        this.attributes = new HashMap<String, Object>();
    }

    public ParallelSentence(Sentence sourceSentence, List<Sentence> targetSentences, HashMap<String, Object> attributes){
        this.sourceSentence = sourceSentence;
        this.targetSentences = targetSentences;
        this.attributes = attributes;
    }

    public void setSource(Sentence sourceSentence) {
    	this.sourceSentence = sourceSentence;
    }
    
    public void addTarget(Sentence targetSentence) {
    	this.targetSentences.add(targetSentence);
    }

    
    public Sentence getSource() {
        return this.sourceSentence;
    }

    public List<Sentence> getTargetSentences() {
        return this.targetSentences;
    }

    public HashMap<String,Object> getAttributes() {
        return this.attributes;
    }


}
