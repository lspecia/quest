/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package shef.mt.features.impl.bb;

import shef.mt.features.impl.Feature;
import shef.mt.features.util.Sentence;

/**
 * Feature: LM log probablity of the target supertags
 * @author Hala Almaghout
 */
public class FeatureCCG_SuperLMProb  extends Feature{
   
     public FeatureCCG_SuperLMProb() {
        setIndex(4389);
        setDescription("LM log probablity of the target supertags");
       
    }
     
     public void run(Sentence sourceSent, Sentence targetSent){
         setValue((Float)targetSent.getValue("CCG.superlogprob"));
     }
             
}
