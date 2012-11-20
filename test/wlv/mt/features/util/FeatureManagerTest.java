/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wlv.mt.features.util;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author zk
 */
public class FeatureManagerTest {

    public FeatureManagerTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
//        Sentence src1 = new Sentence("A b c d.", 1);
//        Sentence tgt1 = new Sentence("1 2 3 4 5.", 1);
//        Sentence src2 = new Sentence("W x, y-z.", 2);
//        Sentence tgt2 = new Sentence("10 11 12 13.", 2);


    }

    @After
    public void tearDown() {
    }


    /**
     * Test of runFeatures method, of class FeatureManager.
     */
    @Test
    public void testRunFeaturesDynamicOneSent() {
        System.out.println("runFeatures");
        Sentence source = new Sentence("A b c d.", 1);
        Sentence target = new Sentence("1 2 3 4 5.", 1);
        FeatureManager instance = new FeatureManager("config/features/features_test_dynamic_feats.xml");
        instance.setFeatureList("all");
        instance.loadAllFeatures();
        instance.registerFeatures();


        String expResult = "4.0\t5.0\t0.8\tsrc_1\ttgt_1\t";
        String result = instance.runFeatures(source, target);
        assertEquals(expResult, result);
    }


    /**
     * Test of runFeatures method, of class FeatureManager.
     */
    @Test
    public void testRunFeaturesDynamicTwoSent() {
        System.out.println("runFeatures");

        Sentence s1 = new Sentence("A b c d.", 1);
        Sentence t1 = new Sentence("1 2 3 4 5.", 1);
        Sentence s2 = new Sentence("W x, y-z.", 2);
        Sentence t2 = new Sentence("10 11 12 13.", 2);

        FeatureManager instance = new FeatureManager("config/features/features_test_dynamic_feats.xml");
        instance.setFeatureList("all");
        instance.loadAllFeatures();
        instance.registerFeatures();


        String expResult = "4.0\t5.0\t0.8\tsrc_1\ttgt_1\t";
        String result = instance.runFeatures(s1, t1);
        assertEquals(expResult, result);


        result = instance.runFeatures(s2, t2);
        expResult = "3.0\t4.0\t0.75\tsrc_2\ttgt_2\t";
        assertEquals(expResult, result);
    }


}
