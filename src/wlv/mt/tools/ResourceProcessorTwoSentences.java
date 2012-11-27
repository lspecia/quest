/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wlv.mt.tools;
import wlv.mt.features.util.*;

/**
 *
 * @author David Langlois
 */


//for dealing with Processor requiring source and target sentences to compute the score
public abstract class ResourceProcessorTwoSentences extends ResourceProcessor {
    public abstract void processNextParallelSentences(Sentence source,Sentence target);
}
