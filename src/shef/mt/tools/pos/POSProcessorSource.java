package shef.mt.tools.pos;

import shef.mt.features.util.FeatureManager;
import shef.mt.features.util.Sentence;
import shef.mt.pipelines.ResourcePipeline;
import shef.mt.util.PropertiesManager;

import java.io.*;


/**
 * The POSProcessor class analyses a file produced by a pos tagger and sets
 * certain values to a Sentence object These values are, currently, the number
 * of nouns, verbs, pronouns and content words
 *
 */
public class POSProcessorSource extends POSProcessor {

    BufferedReader br;
    int sentCount;
//    static String XPOS=".XPOS";
//    BufferedWriter bwXPos;
    public String resourceName = "sourcePosTagger";
    
    public void initialize(PropertiesManager propertiesManager,
			   FeatureManager featureManager) {
    	POSProcessorSource posProcessorSource = new POSProcessorSource();
    	
    	String sourceLang = propertiesManager.getString("sourceLang");
    	String sourceFile = propertiesManager.getString("sourceFile");
    	boolean forceRun = (propertiesManager.get("forceRun")=="true");
    	
    	String sourcePosOutput = runPOS(propertiesManager, forceRun, sourceFile, sourceLang, "source");
    	posProcessorSource.create(sourcePosOutput);
    	
    	ResourcePipeline rp = new ResourcePipeline();
    	rp.addResourceProcessor(posProcessorSource);
    	System.out.println("---POSSource addedinto rp!!!---");
    	//ResourcePipeline.addResourceProcessor(posProcessorSource);
    }
    
    /**
     * runs the part of speech tagger
     *
     * @param file input file
     * @param lang language
     * @param type source or target
     * @return path to the output file of the POS tagger
     */
    public String runPOS(PropertiesManager propertiesManager, boolean forceRun, String file, String lang, String type) {
        String posName = propertiesManager.getString(lang + ".postagger");
        String workDir = System.getProperty("user.dir");
        String input = workDir + File.separator + propertiesManager.getString("input");
        String langResPath = input + File.separator + lang;
        File f = new File("input" + File.separator + file);
        String absoluteSourceFilePath = f.getAbsolutePath();
        String fileName = f.getName();
        String relativeFilePath = langResPath + File.separator + fileName
                + ".pos";
        String absoluteOutputFilePath = (new File(relativeFilePath))
                .getAbsolutePath();
        String posSourceTaggerPath = propertiesManager.getString(lang
                + ".postagger.exePath");
        String outPath = "";
        try {
            Class c = Class.forName(posName);
            PosTagger tagger = (PosTagger) c.newInstance();
            tagger.setParameters(type, posName, posSourceTaggerPath,
                    absoluteSourceFilePath, absoluteOutputFilePath);
            PosTagger.ForceRun(forceRun);
            outPath = tagger.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // returns the path of the output file; this is for convenience only so
        // we do't have to calculate it again
        return outPath;
    }
    
    /**
     * initialise a POSProcessor from an input file The POSProcessor expect an
     * input file in a fixed format, where each line is of the type:<br> <i>word
     * DT	word</i> (tokens separated by tab) <br>
     *
     *
     * @param input the input file
     *
     */
    public void create(String input) {
        try {
            System.out.println("INPUT TO POSPROCESSOR:" + input);
            br = new BufferedReader(new FileReader(input));
//			bwXPos = new BufferedWriter(new FileWriter(input+getXPOS()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        sentCount = 0;
    }

    //public void processNextSentence(Sentence s) {
    //	processSentence(s);
    //}
    
    /**
     * Reads the pos tags associated to a sentence and counts the number of
     * content words The count for each type of content word is addedd as a
     * value to the sentence
     *
     * @see Sentence.setValue()
     * @param sent the sentence to be analysed
     */
    public void processNextSentence(Sentence sent) {
    	try {
	        int tokCount = sent.getNoTokens();
	        String line = br.readLine();
	        int contentWords = 0;
	        int nounWords = 0;
	        int verbWords = 0;
	        int pronWords = 0;
	        int otherContentWords = 0;
	        int count = 0;
	
	        while (line != null && (count < tokCount)) {
	            if (!line.trim().isEmpty()) {
	                String[] split = line.split("\t");
	                String tag = split[1];
	                if (tag.contains("SENT")) {
	                    tag = tag.split(" ")[0];
	                } else if (PosTagger.isNoun(tag)) {
	                    nounWords++;
	//					System.out.println("is noun");
	                } else if (PosTagger.isPronoun(tag)) {
	                    pronWords++;
	                } else if (PosTagger.isVerb(tag)) {
	                    verbWords++;
	                    //					System.out.println("is verb");
	                } else if (PosTagger.isAdditional(tag)) {
	                    otherContentWords++;
	                }
	                //    	  	bwXPos.write(tag);
	                count++;
	            }
	            line = br.readLine();
	        }
	        //   bwXPos.newLine();
	        contentWords = nounWords + verbWords + otherContentWords;
	        sent.setValue("contentWords", contentWords);
	        System.out.println(contentWords);
	        sent.setValue("nouns", nounWords);
	        sent.setValue("verbs", verbWords);
	        sent.setValue("prons", pronWords);
	//        line = br.readLine();
	//        if (line==null) {
	//           	System.out.println("SENTENCE IS NULL: "+sent.getIndex()+"\t"+sent.getText());
	//        	br.close();
	        // 	bwXPos.close();
	//        }
    	} catch (Exception e) {
            //               System.out.println(pplFile+" "+s.getText());
            e.printStackTrace();
        }
    }
    
    public String getName() {
    	return resourceName;
    }
    
    /*      public static String getXPOS(){
     return XPOS;
     }*/
}
