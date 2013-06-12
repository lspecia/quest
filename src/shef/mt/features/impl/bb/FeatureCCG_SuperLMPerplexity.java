/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package shef.mt.features.impl.bb;

import shef.mt.features.impl.Feature;
import shef.mt.features.util.Sentence;

/**
 * Feature: LM log perplexity of the target supertags
 * @author Hala Almaghout
 */
public class FeatureCCG_SuperLMPerplexity  extends Feature{
   
     public FeatureCCG_SuperLMPerplexity() {
        setIndex(4390);
        setDescription("LM log perplexity of the target supertags");
       
    }
     
     public void run(Sentence sourceSent, Sentence targetSent){
         setValue((Float)targetSent.getValue("CCG.superppl"));
     }
             
}
