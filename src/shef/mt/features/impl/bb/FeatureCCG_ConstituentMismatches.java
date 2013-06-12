/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package shef.mt.features.impl.bb;

import shef.mt.features.impl.Feature;
import shef.mt.features.util.Sentence;

/**
 * Feature: Percentage of mismatched CCG constituents in the target
 * @author Hala Almaghout
 */
public class FeatureCCG_ConstituentMismatches  extends Feature{
   
     public FeatureCCG_ConstituentMismatches() {
        setIndex(4388);
        setDescription("Percentage of mismatched CCG constituents in the target");
       
    }
     
     public void run(Sentence sourceSent, Sentence targetSent){
         setValue((Float)targetSent.getValue("CCG.constituentMismatches"));
     }
             
}
