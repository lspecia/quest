/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package shef.mt.features.impl.bb;

import shef.mt.features.impl.Feature;
import shef.mt.features.util.Sentence;

/**
 * Feature: Minimum number of CCG constituents in the target
 * @author Hala Almaghout
 */
public class FeatureCCG_ConstituentNum  extends Feature{
   
     public FeatureCCG_ConstituentNum() {
        setIndex(4386);
        setDescription("Minimum number of CCG constituents in the target");
       
    }
     
     public void run(Sentence sourceSent, Sentence targetSent){
         setValue((Float)targetSent.getValue("CCG.constituentNum"));
     }
             
}
